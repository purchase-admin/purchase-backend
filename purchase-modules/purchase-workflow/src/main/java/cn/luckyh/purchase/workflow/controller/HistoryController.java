package cn.luckyh.purchase.workflow.controller;


import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.workflow.service.history.HistoryQueryService;
import cn.luckyh.purchase.workflow.vo.history.HistoryRecordResult;
import cn.luckyh.purchase.workflow.vo.query.TaskInstanceQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 历史流程.
 *
 * @author heng.wang
 * @since 2021/04/06 11:05
 */
@Slf4j
@RestController
@Api(tags = "历史记录")
@RequestMapping(value = "/workflow/history")
public class HistoryController {

    @Resource
    private HistoryQueryService historyQueryService;

    /**
     * GET /workflow/history/record/{processInstanceId} -> 流程记录
     */
    @ApiOperation(value = "流程记录")
    @GetMapping("/record/{processInstanceId}")
    public R record(@PathVariable @ApiParam(value = "流程实例id", name = "processInstanceId") String processInstanceId) {
        HistoryRecordResult result = historyQueryService.historyProcessRecordQuery(processInstanceId);
        return R.success(result);
    }

    /**
     * GET /workflow/history/task/record/{taskId} -> 流程记录
     */
    @ApiOperation(value = "流程记录")
    @GetMapping("/task/record/{taskId}")
    public R taskRecords(@PathVariable @ApiParam(value = "流程实例id", name = "processInstanceId") String taskId) {
        HistoryRecordResult result = historyQueryService.historyTaskRecordQuery(taskId);
        return R.success(result);
    }

    /**
     * GET /workflow/history/records -> 历史流程记录
     */
    @GetMapping("/records")
    public TableDataInfo historyRecords(TaskInstanceQuery query) {
        return historyQueryService.listHistoryRecord(query);
    }
}
