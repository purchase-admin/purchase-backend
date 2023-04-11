package cn.luckyh.purchase.workflow.controller;

import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.workflow.service.runtime.TasksService;
import cn.luckyh.purchase.workflow.vo.query.TaskInstanceQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * tasks controller.
 * query task list.
 *
 * @author heng.wang
 * @since 2021/04/19 16:06
 */
@Slf4j
@RestController
@Api(tags = "Runtime流程")
@RequestMapping("/workflow")
public class TasksController {

    @Autowired
    private TasksService tasksService;

    /**
     * GET /workflow/run/tasks -> 获取待办任务
     */
    @Deprecated
    @GetMapping("/run/tasks")
    @ApiOperation(value = "查询待办任务")
    public TableDataInfo getTasks(TaskInstanceQuery query) {
        return tasksService.queryTasks(query);
    }

    @GetMapping("/run/tasks2")
    @ApiOperation(value = "查询待办任务")
    public TableDataInfo listTasks(TaskInstanceQuery query) {
        return tasksService.listTasks(query);
    }
}
