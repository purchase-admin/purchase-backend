package cn.luckyh.purchase.workflow.service.runtime;

import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.system.service.ISysUserService;
import cn.luckyh.purchase.workflow.exception.NotFoundException;
import cn.luckyh.purchase.workflow.vo.runtime.CreateProcessInstanceRepresentation;
import cn.luckyh.purchase.workflow.vo.runtime.MigrationRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.BpmnEngineEntityConstants;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.migration.ProcessInstanceMigrationBuilder;
import org.flowable.engine.migration.ProcessInstanceMigrationValidationResult;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * process instance service.
 * start process instance.
 *
 * @author heng.wang
 * @since 2021/04/20 17:15
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ProcessInstancesService {

    protected final HistoryService historyService;
    protected final ProcessEngineConfiguration processEngineConfiguration;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RepositoryService repositoryService;

    private final ProcessMigrationService processMigrationService;

    public ProcessInstancesService(ProcessEngineConfiguration processEngineConfiguration, HistoryService historyService, ISysUserService userService, ProcessMigrationService processMigrationService) {
        this.processEngineConfiguration = processEngineConfiguration;
        this.taskService = processEngineConfiguration.getTaskService();
        this.runtimeService = processEngineConfiguration.getRuntimeService();
        this.repositoryService = processEngineConfiguration.getRepositoryService();
        this.historyService = historyService;
        this.processMigrationService = processMigrationService;
    }

    //    public String startNewProcessInstance(@NotEmpty String key, String businessKey, String title, HashMap<String, Object> hashMap) {

    /**
     * 流程发起,发起人默认为当期登录人
     *
     * @param dto 流程启动Rest对象
     * @return 流程实例ID
     */
    public String startNewProcessInstance(CreateProcessInstanceRepresentation dto) {
        return startNewProcessInstance(dto, SecurityUtils.getUsername());
    }

    /**
     * 流程发起
     *
     * @param dto           流程启动Rest对象
     * @param startUserName 发起人
     * @return 流程实例ID
     */
    public String startNewProcessInstance(CreateProcessInstanceRepresentation dto, String startUserName) {
        //设置发起人
        System.out.println(dto.toString());
        Authentication.setAuthenticatedUserId(startUserName);
        return startProcessInstance(dto);
    }

    public String startProcessInstance(CreateProcessInstanceRepresentation representation) {
        //发起流程
        HashMap<String, Object> staticVariablesMap = new HashMap<>();
        //启用跳过表达式
        staticVariablesMap.put("_FLOWABLE_SKIP_EXPRESSION_ENABLED", true);
        //start variables
        HashMap<String, Object> variables = new HashMap<>(staticVariablesMap);
        if (Objects.nonNull(representation.getVariables())) {
            variables.putAll(representation.getVariables());
        }
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance processInstance = processInstanceBuilder
                .processDefinitionKey(representation.getProcessDefinitionKey())
                .variables(variables)
                .predefineProcessInstanceId(generateNewProcessInstanceId())
                .businessKey(representation.getBusinessKey())
                .name(representation.getTitle())
                .start();
        //处理第一步办理人
        handleFirstStep(processInstance.getId());

        //返回流程实例ID
        return processInstance.getProcessInstanceId();
    }

    private void handleFirstStep(String id) {
        List<Task> list = taskService.createTaskQuery().processInstanceId(id).list();
        if (!list.isEmpty()) {
            Task task = list.get(0);
            String taskDefinitionId = task.getTaskDefinitionKey();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            FlowElement flowElement = bpmnModel.getFlowElement(taskDefinitionId);
            UserTask userTask = (UserTask) flowElement;
            if ("${INITIATOR}".equalsIgnoreCase(userTask.getAssignee())) {
                taskService.complete(task.getId());
            }

        }

    }

    public String generateNewProcessInstanceId() {
        if (processEngineConfiguration.isUsePrefixId()) {
            return BpmnEngineEntityConstants.BPMN_ENGINE_ID_PREFIX + processEngineConfiguration.getIdGenerator().getNextId();
        } else {
            return processEngineConfiguration.getIdGenerator().getNextId();
        }
    }

    public void delete(String processInstanceId, String reason) {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).list();
        if (list.isEmpty()) {
            log.warn("流程:{}已结束", processInstanceId);
            return;
        }
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }

    public HistoricProcessInstance findProcessInstanceById(String processInstanceId) {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).list();
        if (list.isEmpty()) {
            log.error("找不到历史流程实例流程实例ID为:{}", processInstanceId);
            throw new NotFoundException("找不到流程实例ID");
        }
        return list.get(0);
    }

    public boolean migrateProcessInstance(MigrationRepresentation migrationRepresentation) {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processInstanceId(migrationRepresentation.getProcessInstanceId()).list();
        if (list.isEmpty()) {
            throw new NotFoundException("未找到流程");
        }
        ProcessInstance processInstance = list.get(0);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(migrationRepresentation.getMigrationDefinitionId()).singleResult();
        if (processDefinition == null) {
            throw new NotFoundException("未找到流程定义信息");
        }
        ProcessInstanceMigrationBuilder processInstanceMigrationBuilder = processMigrationService.createProcessInstanceMigrationBuilder().migrateToProcessDefinition(processDefinition.getId());
        ProcessInstanceMigrationValidationResult migrationValidationResult = processInstanceMigrationBuilder.validateMigration(processInstance.getId());
        if (migrationValidationResult.isMigrationValid()) {
            log.debug("流程迁移校验通过");
        } else {
            List<String> validationMessages = migrationValidationResult.getValidationMessages();
            log.error("流程迁移校验失败:\n");
            validationMessages.forEach(log::error);
        }
        processInstanceMigrationBuilder.migrate(processInstance.getId());

        List<Execution> executionList = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list();
        boolean result = executionList.stream().map(execution -> (ExecutionEntity) execution).anyMatch(executionEntity ->
                processDefinition.getId().equals(executionEntity.getProcessDefinitionId())
        );
        return result;
    }
}
