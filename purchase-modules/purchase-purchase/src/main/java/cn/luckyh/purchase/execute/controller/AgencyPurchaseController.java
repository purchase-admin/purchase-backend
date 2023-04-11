package cn.luckyh.purchase.execute.controller;

import java.util.List;

import cn.luckyh.purchase.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.execute.domain.AgencyPurchase;
import cn.luckyh.purchase.execute.service.IAgencyPurchaseService;
import cn.luckyh.purchase.common.utils.poi.ExcelUtil;
import cn.luckyh.purchase.common.core.page.TableDataInfo;

/**
 * 代理采购 Controller
 *
 * @author purchase
 * @date 2021-03-24
 */
@RestController
@RequestMapping("/agencyPurchase/agencyPurchase")
public class AgencyPurchaseController extends BaseController {
    @Autowired
    private IAgencyPurchaseService agencyPurchaseService;

    /**
     * 查询代理采购 列表
     */
    //@PreAuthorize("@ss.hasPermi('agencyPurchase:agencyPurchase:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgencyPurchase agencyPurchase) {
        startPage();
        List<AgencyPurchase> list = agencyPurchaseService.selectAgencyPurchaseList(agencyPurchase);
        return getDataTable(list);
    }

    /**
     * 导出代理采购 列表
     */
    //@PreAuthorize("@ss.hasPermi('agencyPurchase:agencyPurchase:export')")
    @Log(title = "代理采购 ", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public R export(AgencyPurchase agencyPurchase) {
        List<AgencyPurchase> list = agencyPurchaseService.selectAgencyPurchaseList(agencyPurchase);
        ExcelUtil<AgencyPurchase> util = new ExcelUtil<AgencyPurchase>(AgencyPurchase.class);
        return util.exportExcel(list, "agencyPurchase");
    }

    /**
     * 获取代理采购 详细信息
     */
    //@PreAuthorize("@ss.hasPermi('agencyPurchase:agencyPurchase:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        return R.success(agencyPurchaseService.selectAgencyPurchaseById(id));
    }

    /**
     * 新增代理采购
     */
    //@PreAuthorize("@ss.hasPermi('agencyPurchase:agencyPurchase:add')")
    @Log(title = "代理采购 ", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody AgencyPurchase agencyPurchase) {
        return toAjax(agencyPurchaseService.insertAgencyPurchase(agencyPurchase));
    }

    /**
     * 修改代理采购
     */
    //@PreAuthorize("@ss.hasPermi('agencyPurchase:agencyPurchase:edit')")
    @Log(title = "代理采购 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody AgencyPurchase agencyPurchase) {
        return toAjax(agencyPurchaseService.updateAgencyPurchase(agencyPurchase));
    }

    /**
     * 删除代理采购
     */
    //@PreAuthorize("@ss.hasPermi('agencyPurchase:agencyPurchase:remove')")
    @Log(title = "代理采购 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(agencyPurchaseService.deleteAgencyPurchaseByIds(ids));
    }
}
