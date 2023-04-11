package cn.luckyh.purchase.budget.controller;

import cn.luckyh.purchase.budget.domain.BudgetBatch;
import cn.luckyh.purchase.budget.service.IBudgetBatchService;
import cn.luckyh.purchase.budget.vo.BackTaskVo;
import cn.luckyh.purchase.budget.vo.BudgetBatchVo;
import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.constant.WorkflowConstants;
import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.domain.Result;
import cn.luckyh.purchase.common.core.domain.ResultUtil;
import cn.luckyh.purchase.common.core.domain.entity.SysDept;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.common.utils.poi.ExcelUtil;
import cn.luckyh.purchase.system.service.impl.SysDeptServiceImpl;
import cn.luckyh.purchase.workflow.vo.runtime.CreateProcessInstanceRepresentation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 预算申请批Controller
 *
 * @author purchase
 * @since 2021-04-15
 */
@RestController
@RequestMapping("/budgetBatch/batch")
public class BudgetBatchController extends BaseController {

    @Autowired
    private IBudgetBatchService budgetBatchService;

    @Autowired
    private SysDeptServiceImpl deptService;

    /**
     * 查询预算申请批列表
     */
    //@PreAuthorize("@ss.hasPermi('budgetBatch:batch:list')")
    @GetMapping("/list")
    public TableDataInfo list(BudgetBatch budgetBatch) {
        startPage();
        List<BudgetBatchVo> list = budgetBatchService.selectBudgetBatchList(budgetBatch);
        return getDataTable(list);
    }

    /**
     * 导出预算申请批列表
     */
    //@PreAuthorize("@ss.hasPermi('budgetBatch:batch:export')")
    @Log(title = "预算申请批", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public R export(BudgetBatch budgetBatch) {
        List<BudgetBatchVo> list = budgetBatchService.selectBudgetBatchList(budgetBatch);
        ExcelUtil<BudgetBatchVo> util = new ExcelUtil<>(BudgetBatchVo.class);
        return util.exportExcel(list, "batch");
    }

    /**
     * 获取预算申请批详细信息
     */
    //@PreAuthorize("@ss.hasPermi('budgetBatch:batch:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        BudgetBatch data = budgetBatchService.selectBudgetBatchById(id);
        return R.success(data);
    }

    /**
     * 新增预算申请批
     */
    //@PreAuthorize("@ss.hasPermi('budgetBatch:batch:add')")
    @Log(title = "预算申请批", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody @Valid BudgetBatch budgetBatch) {
        //        if (StringUtil.isEmpty(budgetBatch.getDeptId()) || StringUtil.isEmpty(budgetBatch.getYear())) {
        //            return R.error("部门或年份不能为空！");
        //        }
        LambdaQueryWrapper<BudgetBatch> wrapper = Wrappers.lambdaQuery(BudgetBatch.class);
        wrapper.eq(BudgetBatch::getYear, budgetBatch.getYear());
        wrapper.eq(BudgetBatch::getDeptId, budgetBatch.getDeptId());
        BudgetBatch budget = budgetBatchService.getOne(wrapper);//同年同部门只能生成一个批数据
        if (budget == null) {
            SysDept dept = deptService.selectDeptById(Long.parseLong(budgetBatch.getDeptId()));
            String title = String.format("%s年度采购预算申报", budgetBatch.getYear());
//            budgetBatch.setTitle( budgetBatch.getYear() + "年度采购预算申报");
            budgetBatch.setTitle(title);
            budgetBatch.setStatus(WorkflowConstants.DRAFT);
            return toAjax(budgetBatchService.insertBudgetBatch(budgetBatch));
        }
        return R.error(budgetBatch.getYear() + "年采购预算已经存");
    }

    /**
     * 修改预算申请批
     */
    //@PreAuthorize("@ss.hasPermi('budgetBatch:batch:edit')")
    @Log(title = "预算申请批", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody BudgetBatch budgetBatch) {
        return toAjax(budgetBatchService.updateBudgetBatch(budgetBatch));
    }

    /**
     * 删除预算申请批
     */
    //@PreAuthorize("@ss.hasPermi('budgetBatch:batch:remove')")
    @Log(title = "预算申请批", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R remove(@PathVariable String id, String reason) {
        return toAjax(budgetBatchService.deleteBudgetBatchById(id, reason));
    }

    /*
    //@PreAuthorize("@ss.hasPermi('budgetBatch:batch:remove')")
    @Log(title = "预算申请批", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(budgetBatchService.deleteBudgetBatchByIds(ids));
    }
     */

    /**
     * POST /budgetBatch/batch/start -> 采购预算提交
     */
    @PostMapping("/start")
    @ApiOperation(value = "采购预算提交")
    public R start(@RequestBody CreateProcessInstanceRepresentation representation) {
        String processInstanceId = budgetBatchService.startProcess(representation);
        return R.success("流程发起成功", processInstanceId);
    }

    /**
     * POST /budgetBatch/batch/back-to-resubmit -> 退回采购预算审批
     */
    @PostMapping("/back-to-resubmit")
    @ApiOperation(value = "退回采购预算审批")
    public Result<String> backToReSubmit(@RequestBody @Valid BackTaskVo backTaskVo) {
        budgetBatchService.backToReSubmit(backTaskVo.getId(), backTaskVo.getIds(), backTaskVo.getComment());
        return ResultUtil.success("退回成功");
    }
}
