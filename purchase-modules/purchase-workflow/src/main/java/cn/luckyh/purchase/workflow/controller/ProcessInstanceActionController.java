package cn.luckyh.purchase.workflow.controller;

import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.domain.Result;
import cn.luckyh.purchase.common.core.domain.ResultUtil;
import cn.luckyh.purchase.workflow.service.runtime.ProcessInstancesService;
import cn.luckyh.purchase.workflow.vo.runtime.CreateProcessInstanceRepresentation;
import cn.luckyh.purchase.workflow.vo.runtime.MigrationRepresentation;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.Collections;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/04/20 17:42
 */
@Slf4j
@RestController
@Api(tags = "Runtime流程")
@RequestMapping("/workflow")
public class ProcessInstanceActionController {

    private final ProcessInstancesService processInstancesService;
    private final RuntimeService runtimeService;

    @Autowired
    protected ProcessEngineConfiguration processEngineConfiguration;

    @Autowired
    protected RepositoryService repositoryService;

    public ProcessInstanceActionController(ProcessInstancesService processInstancesService, RuntimeService runtimeService) {
        this.processInstancesService = processInstancesService;
        this.runtimeService = runtimeService;
    }

    /**
     * POST /workflow/run/start -> 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/run/start")
    public R start(@RequestBody CreateProcessInstanceRepresentation dto) {
        String s = processInstancesService.startNewProcessInstance(dto);
        return R.success("流程发起成功", s);
    }

    /**
     * DELETE /workflow/run/delete/{processInstanceId} ->
     */
    @ApiOperation(value = "删除流程")
    @DeleteMapping("/run/delete/{processInstanceId}")
    public R delete(@PathVariable String processInstanceId, String reason) {
        processInstancesService.delete(processInstanceId, reason);
        return R.success("删除成功");
    }


    /**
     * POST /workflow/run/migration -> 迁移流程
     */
    @ApiModelProperty(value = "迁移流程到指定版本")
    @PostMapping("/run/migration")
    public Result<String> migrationProcessInstance(@RequestBody @Valid MigrationRepresentation migrationRepresentation) {
        boolean b = processInstancesService.migrateProcessInstance(migrationRepresentation);
        return b ? ResultUtil.success("迁移成功") : ResultUtil.error("迁移失败");
    }


    @ApiOperation(value = "Get diagram for a process instance", tags = {"Process Instances"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Indicates the process instance was found and the diagram was returned."),
            @ApiResponse(code = 400, message = "Indicates the requested process instance was not found but the process does not contain any graphical information (BPMN:DI) and no diagram can be created."),
            @ApiResponse(code = 404, message = "Indicates the requested process instance was not found.")
    })
    @GetMapping(value = "/run/process-instances/{processInstanceId}/diagram")
    public ResponseEntity<byte[]> getProcessInstanceDiagram(@ApiParam(name = "processInstanceId") @PathVariable String processInstanceId, HttpServletResponse response) {
        ProcessInstance processInstance = getProcessInstanceFromRequest(processInstanceId);

        ProcessDefinition pde = repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        if (pde != null && pde.hasGraphicalNotation()) {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(pde.getId());
            ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
            InputStream resource = diagramGenerator.generateDiagram(bpmnModel, "png", runtimeService.getActiveActivityIds(processInstance.getId()), Collections.emptyList(),
                    processEngineConfiguration.getActivityFontName(), processEngineConfiguration.getLabelFontName(),
                    processEngineConfiguration.getAnnotationFontName(), processEngineConfiguration.getClassLoader(), 1.0, processEngineConfiguration.isDrawSequenceFlowNameWithNoLabelDI());

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Content-Type", "image/png");
            try {
                return new ResponseEntity<>(IOUtils.toByteArray(resource), responseHeaders, HttpStatus.OK);
            } catch (Exception e) {
                throw new FlowableIllegalArgumentException("Error exporting diagram", e);
            }

        } else {
            throw new FlowableIllegalArgumentException("Process instance with id '" + processInstance.getId() + "' has no graphical notation defined.");
        }
    }


    protected ProcessInstance getProcessInstanceFromRequest(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance == null) {
            throw new FlowableObjectNotFoundException("Could not find a process instance with id '" + processInstanceId + "'.");
        }


        return processInstance;
    }
}
