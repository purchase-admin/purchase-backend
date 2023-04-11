package cn.luckyh.purchase.workflow.service.model;

import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.workflow.mapper.ModelMapper;
import cn.luckyh.purchase.workflow.vo.model.ModelDto;
import cn.luckyh.purchase.workflow.vo.model.ModelRestVo;
import cn.luckyh.purchase.workflow.vo.runtime.ProcessDefinitionRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.util.io.BytesStreamSource;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * model service resolve.
 *
 * @author heng.wang
 * @since 2021/03/24 09:09
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ModelsService {

    private final RepositoryService repositoryService;

    private final ModelMapper modelMapper;

    public ModelsService(RepositoryService repositoryService, ModelMapper modelMapper) {
        this.repositoryService = repositoryService;
        this.modelMapper = modelMapper;
    }

    public List<ModelRestVo> list() {
        return modelMapper.list();
    }

    public Model saveModel(ModelDto dto) {
        Model model;
        if (StringUtils.hasText(dto.getId())) {
            // 更新
            model = repositoryService.createModelQuery().modelId(dto.getId()).singleResult();
        } else {
            // 新建
            model = repositoryService.newModel();
        }
        model.setKey(dto.getKey());
        model.setName(dto.getName());
        repositoryService.saveModel(model);
        repositoryService.addModelEditorSource(model.getId(), dto.getXmlStr().getBytes(StandardCharsets.UTF_8));
        // Add a new model
        log.debug("保存模型{}成功", model);
        return model;
    }

    public String deployModel(String modelId) {
        Model model = repositoryService.createModelQuery().modelId(modelId).singleResult();
        //        Assert.isNull(model, "模型不存在");
        byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(new BytesStreamSource(modelEditorSource), false, false);

        List<FlowElement> eventRegistryElements = new ArrayList<>();
        Map<String, StartEvent> noneStartEventMap = new HashMap<>();
        postProcessFlowElements(eventRegistryElements, noneStartEventMap, bpmnModel);

        for (Process process : bpmnModel.getProcesses()) {
            processUserTasks(process.getFlowElements(), process, noneStartEventMap);
        }

        String id = repositoryService.createDeployment().addBpmnModel(bpmnModel.getMainProcess().getName() + ".bpmn20.xml", bpmnModel)
                .name(model.getName())
                .key(model.getKey())
                .deploy().getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(id).singleResult();
        log.debug(processDefinition.toString());
        model.setDeploymentId(processDefinition.getDeploymentId());
        repositoryService.saveModel(model);
        return id;
    }

    public Model getModel(String modelId) {
        return repositoryService.createModelQuery().modelId(modelId).singleResult();
    }

    public TableDataInfo getHistoryList(String modelId, Integer pageSize, Integer pageNum) {
        Model model = getModel(modelId);
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.processDefinitionKeyLike(model.getKey()).orderByProcessDefinitionVersion().desc()
                .listPage((pageNum - 1) * pageSize, pageSize);
        List<ProcessDefinitionRepresentation> definitionRepresentationList = convertDefinitionList(processDefinitionList);
        return TableDataInfo.success(definitionRepresentationList, processDefinitionQuery.count(), HttpStatus.OK);

    }

    protected List<ProcessDefinitionRepresentation> convertDefinitionList(List<ProcessDefinition> definitions) {
        List<ProcessDefinitionRepresentation> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(definitions)) {
            // Links to other resources
            definitions.forEach(processDefinition -> {
                ProcessDefinitionRepresentation response = new ProcessDefinitionRepresentation();
                response.setId(processDefinition.getId());
                response.setKey(processDefinition.getKey());
                response.setVersion(processDefinition.getVersion());
                response.setCategory(processDefinition.getCategory());
                response.setName(processDefinition.getName());
                response.setDescription(processDefinition.getDescription());
                response.setSuspended(processDefinition.isSuspended());
                response.setStartFormDefined(processDefinition.hasStartFormKey());
                response.setGraphicalNotationDefined(processDefinition.hasGraphicalNotation());
                response.setTenantId(processDefinition.getTenantId());
                response.setDeploymentId(processDefinition.getDeploymentId());
                response.setResource(processDefinition.getResourceName());
                if (processDefinition.getDiagramResourceName() != null) {
                    response.setDiagramResource(processDefinition.getDiagramResourceName());
                }
                result.add(response);
            });
        }
        return result;
    }

    protected void postProcessFlowElements(List<FlowElement> eventRegistryElements, Map<String, StartEvent> noneStartEventMap, BpmnModel bpmnModel) {
        for (Process process : bpmnModel.getProcesses()) {
            postProcessFlowElements(process.getFlowElements(), eventRegistryElements, noneStartEventMap, process.getId(), bpmnModel);
        }
    }

    protected void postProcessFlowElements(Collection<FlowElement> traverseElementList, List<FlowElement> eventRegistryElements,
            Map<String, StartEvent> noneStartEventMap, String processId, BpmnModel bpmnModel) {

        for (FlowElement flowElement : traverseElementList) {
            if (flowElement instanceof Event) {

                List<ExtensionElement> eventTypeElements = flowElement.getExtensionElements().get("eventType");
                if (eventTypeElements != null && eventTypeElements.size() > 0) {
                    eventRegistryElements.add(flowElement);
                }

                if (flowElement instanceof StartEvent) {
                    StartEvent startEvent = (StartEvent) flowElement;
                    if (CollectionUtils.isEmpty(startEvent.getEventDefinitions())) {
                        if (org.apache.commons.lang3.StringUtils.isEmpty(startEvent.getInitiator())) {
                            startEvent.setInitiator("initiator");
                        }
                        noneStartEventMap.put(processId, startEvent);
                    }
                }

            } else if (flowElement instanceof SendEventServiceTask) {
                SendEventServiceTask task = (SendEventServiceTask) flowElement;
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(task.getEventType())) {
                    eventRegistryElements.add(flowElement);
                }

            } else if (flowElement instanceof SubProcess) {
                SubProcess subProcess = (SubProcess) flowElement;
                postProcessFlowElements(subProcess.getFlowElements(), eventRegistryElements, noneStartEventMap, processId, bpmnModel);
            }
        }
    }

    protected void processUserTasks(Collection<FlowElement> flowElements, Process process, Map<String, StartEvent> startEventMap) {

        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                if ("$INITIATOR".equals(userTask.getAssignee())) {
                    if (startEventMap.get(process.getId()) != null) {
                        userTask.setAssignee("${" + startEventMap.get(process.getId()).getInitiator() + "}");
                    }
                }

            } else if (flowElement instanceof SubProcess) {
                processUserTasks(((SubProcess) flowElement).getFlowElements(), process, startEventMap);
            }
        }
    }

}
