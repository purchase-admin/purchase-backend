package cn.luckyh.purchase.workflow.controller;

import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.workflow.service.runtime.ProcessInstanceQueryService;
import cn.luckyh.purchase.workflow.vo.query.ProcessInstanceQuery;
import cn.luckyh.purchase.workflow.vo.runtime.ProcessInstanceRepresentation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/03/31 08:59
 */
@Slf4j
@RestController
@Api(tags = "Runtime流程")
@RequestMapping("/workflow")
public class ProcessInstancesController {

    private final ProcessInstanceQueryService processInstanceQueryService;

    public ProcessInstancesController(ProcessInstanceQueryService processInstanceQueryService) {
        this.processInstanceQueryService = processInstanceQueryService;
    }

    /**
     * GET /workflow/run/process-instances -> 流程实例查询
     */
    @GetMapping("/run/process-instances")
    @ApiOperation(value = "流程实例查询")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = ProcessInstanceRepresentation.class)
    })
    public TableDataInfo getProcessInstances(ProcessInstanceQuery query) {
        return processInstanceQueryService.getProcessInstances(query);
    }

}
