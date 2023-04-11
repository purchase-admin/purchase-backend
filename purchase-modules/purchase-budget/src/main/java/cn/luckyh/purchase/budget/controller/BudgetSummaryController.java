package cn.luckyh.purchase.budget.controller;

import cn.luckyh.purchase.budget.domain.BudgetBatch;
import cn.luckyh.purchase.budget.domain.BudgetSummary;
import cn.luckyh.purchase.budget.service.BudgetSummaryService;
import cn.luckyh.purchase.budget.service.IBudgetBatchService;
import cn.luckyh.purchase.budget.vo.BudgetBatchQuery;
import cn.luckyh.purchase.budget.vo.BudgetSummaryQuery;
import cn.luckyh.purchase.budget.vo.BudgetSummaryVo;
import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.domain.Result;
import cn.luckyh.purchase.common.core.domain.ResultUtil;
import cn.luckyh.purchase.common.core.page.PageUtil;
import cn.luckyh.purchase.common.core.page.PageVo;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.common.exception.NotFoundException;
import cn.luckyh.purchase.workflow.vo.runtime.CreateProcessInstanceRepresentation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

/**
 * 预算汇总 controller.
 *
 * @author heng.wang
 * @since 2021/05/08 10:02
 */
@Slf4j
@RestController
@Api(tags = "预算汇总")
@RequestMapping(value = "/budget/summary")
public class BudgetSummaryController {

    @Resource
    private BudgetSummaryService summaryService;

    @Resource
    private IBudgetBatchService budgetBatchService;

    /**
     * GET /budget/summary -> 查询年度预算汇总
     */
    @GetMapping
    @ApiOperation(value = "查询年度预算汇总")
    public TableDataInfo getSummary(BudgetSummaryQuery query) {
        IPage<BudgetSummaryVo> list = summaryService.getSummary(query, PageUtil.initMpPage(query.getPage()));
        return TableDataInfo.data(list);
    }

    /**
     * GET /budget/summary/{id} -> 获取采购预选汇总批数据
     */
    @GetMapping("/{id}")
    @ApiOperation("获取汇总数据")
    public Result<BudgetSummary> getById(@PathVariable String id) {
        return ResultUtil.data(summaryService.getById(id));
    }

    /**
     * GET /budget/summary/detail/{id} -> 查询年度预算汇总详细数据
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "查询年度预算汇总")
    public TableDataInfo getSummaryData(@PathVariable String id, PageVo pageVo) {
        BudgetSummary byId = summaryService.getById(id);
        if (Objects.isNull(byId)) {
            log.error("年度预算汇总详细业务数据不存在,主键为{}", id);
            throw new NotFoundException("年度预算汇总详细业务数据不存在");
        }
        String budgetBatchIds = byId.getBudgetBatchIds();
        String replace = StringUtils.replace(budgetBatchIds, ",", "','");
        IPage<BudgetBatch> budgetBatchIPage = budgetBatchService.pageSelectInIds(replace, PageUtil.initMpPage(pageVo));
        return TableDataInfo.data(budgetBatchIPage);
    }

    /**
     * POST /budget/summary -> 新建预算汇总
     */
    @PostMapping
    @Log(title = "新建年度汇总", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新建年度汇总")
    public R addSummary(@RequestBody @Valid BudgetSummary summary) {
        summaryService.saveSummary(summary);
        return R.success();
    }

    /**
     * PUT /budget/summary/{id} -> 修改预算汇总
     */
    @PutMapping("/{id}")
    @Log(title = "修改年度汇总", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改年度汇总")
    public R updateSummary(@PathVariable("id") String id, @RequestBody @Valid BudgetSummary summary) {
        summaryService.updateSummary(id, summary);
        return R.success();
    }

    /**
     * DELETE /budget/summary/{id} -> 删除预算汇总
     */
    @DeleteMapping("/{ids}")
    @Log(title = "删除年度汇总数据", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除年度汇总数据")
    public R deleteSummary(@PathVariable String ids) {
        summaryService.deleteSummary(ids);
        return R.success();
    }

    /**
     * DELETE /budget/summary/{id}/data/{ids} -> 删除年度汇总中某一条或多条关联数据
     */
    @DeleteMapping("/{id}/data/{ids}")
    @ApiOperation(value = "删除年度汇总中某一条或多条关联数据")
    public R deleteSummaryBatchData(@PathVariable String id, @PathVariable String ids) {
        summaryService.deleteSummaryBatchData(id, ids);
        return R.success();
    }

    /**
     * POST /budget/summary/{id}/data/{ids} -> 删除年度汇总中某一条或多条关联数据
     */
    @PostMapping("/{id}/data/{ids}")
    @ApiOperation(value = "添加年度汇总中某一条或多条关联数据")
    public R updateSummaryBatchData(@PathVariable String id, @PathVariable String ids) {
        summaryService.deleteSummaryBatchData(id, ids);
        return R.success();
    }

    /**
     * POST /budget/summary/start -> 发起年度预选汇总审批
     */
    @PostMapping("/start")
    @ApiOperation(value = "发起年度预选汇总审批")
    public Result<String> start(@RequestBody @Valid CreateProcessInstanceRepresentation representation) {
        String s = summaryService.startProcessInstance(representation);
        return ResultUtil.data(s, "提交成功");
    }

    /**
     * POST /budget/summary/not-join-summary -> 查询未参与年度预算汇总的预算数据
     */
    @PostMapping("/not-join-summary")
    @ApiOperation(value = "未参与年度预算汇总的预算数据")
    public TableDataInfo budgetBatchList(@RequestBody BudgetBatchQuery query) {
        IPage<BudgetBatch> page = budgetBatchService.getNotJoinSummaryList(query, PageUtil.initMpPage(query.getPage()));
        return TableDataInfo.data(page);
    }
}
