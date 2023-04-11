package cn.luckyh.purchase.budget.controller;

import cn.luckyh.purchase.budget.domain.Budget;
import cn.luckyh.purchase.budget.service.IBudgetService;
import cn.luckyh.purchase.budget.vo.BudgetCollectVo;
import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 采购预算 Controller
 *
 * @author purchase
 * @since 2021-03-19
 */
@RestController
@RequestMapping("/budget/budget")
public class BudgetController extends BaseController {

    @Autowired
    private IBudgetService budgetService;

    /**
     * 查询采购预算 列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Budget budget) {
        startPage();
        List<Budget> list = budgetService.selectBudgetList(budget);
        return getDataTable(list);
    }

    /**
     * 导出采购预算 列表
     */
//    //@PreAuthorize("@ss.hasPermi('budget:budget:export')")
    @Log(title = "采购预算", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public R export(Budget budget) {
        List<Budget> list = budgetService.selectBudgetList(budget);
        ExcelUtil<Budget> util = new ExcelUtil<>(Budget.class);
        return util.exportExcel(list, "budget");
    }

    /**
     * 导入采购预算列表
     *
     * @param file excel
     * @return 导入状态
     */
//    //@PreAuthorize("@ss.hasPermi('budget:budget:import')")
    @Log(title = "采购预算 ", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public R importExcel(@RequestParam(value = "file") MultipartFile file) {
        return budgetService.importExcel(file);
    }

    /**
     * 获取采购预算 详细信息
     */
//    //@PreAuthorize("@ss.hasPermi('budget:budget:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        return R.success(budgetService.selectBudgetById(id));
    }

    /**
     * 新增采购预算
     */
//    //@PreAuthorize("@ss.hasPermi('budget:budget:add')")
    @Log(title = "采购预算 ", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody Budget budget) {
        return toAjax(budgetService.insertBudget(budget));
    }

    /**
     * 修改采购预算
     */
//    //@PreAuthorize("@ss.hasPermi('budget:budget:edit')")
    @Log(title = "采购预算 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody Budget budget) {
        return toAjax(budgetService.updateBudget(budget));
    }

    /**
     * 修改采购预算状态
     */
//    //@PreAuthorize("@ss.hasPermi('budget:budget:edit')")
    @Log(title = "采购预算 ", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/updateBudgetByProcInstId")
    public R updateBudgetByProcInstId(@RequestBody Budget budget) {
        return toAjax(budgetService.updateBudgetByProcInstId(budget));
    }

    /**
     * 删除采购预算
     */
//    //@PreAuthorize("@ss.hasPermi('budget:budget:remove')")
    @Log(title = "采购预算 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(budgetService.deleteBudgetByIds(ids));
    }

    /**
     * 查询采购预算汇总列表
     */
//    //@PreAuthorize("@ss.hasPermi('budget:budget:list')")
    @GetMapping("/budgetCollectList")
    public TableDataInfo budgetCollectList(Budget budget) {
        startPage();
        List<BudgetCollectVo> list = budgetService.selectBudgetCollectList(budget);
        return getDataTable(list);
    }


}
