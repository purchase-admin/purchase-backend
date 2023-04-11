package cn.luckyh.purchase.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.workflow.service.runtime.TaskActionService;
import cn.luckyh.purchase.workflow.vo.runtime.TaskCompleteRepresentation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * task action controller.
 *
 * @author heng.wang
 * @since 2021/04/20 17:04
 */
@Slf4j
@RestController
@Api(tags = "task action")
@RequestMapping("/workflow")
public class TaskActionController {

    @Autowired
    private TaskActionService taskActionService;

    /**
     * POST /workflow/run/tasks/{taskId}/submit -> 流程提交
     */
    @ApiOperation(value = "流程提交")
    @PostMapping("/run/tasks/{taskId}/submit")
    public R submit(@PathVariable @ApiParam(value = "任务id", name = "taskId") String taskId,
            @RequestBody TaskCompleteRepresentation completeRepresentation) {
        taskActionService.submit(taskId, completeRepresentation);
        return R.success();
    }

    /**
     * POST /workflow/run/tasks/{taskId}/delTask ->作废流程
     */
    @ApiOperation(value = "流程作废")
    @PostMapping("/run/tasks/{taskId}/delTask")
    public R delTask(@PathVariable @ApiParam(value = "任务id", name = "taskId") String taskId){
        taskActionService.delTask(taskId);
        return R.success("删除成功");
    }
}
