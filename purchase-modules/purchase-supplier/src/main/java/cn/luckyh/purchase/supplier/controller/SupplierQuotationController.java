package cn.luckyh.purchase.supplier.controller;

import java.util.List;
import java.util.Map;

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
import cn.luckyh.purchase.supplier.domain.SupplierQuotation;
import cn.luckyh.purchase.supplier.service.ISupplierQuotationService;
import cn.luckyh.purchase.common.utils.poi.ExcelUtil;
import cn.luckyh.purchase.common.core.page.TableDataInfo;

/**
 * 供应商报价记录 询价采购记录Controller
 *
 * @author purchase
 * @date 2021-03-23
 */
@RestController
@RequestMapping("/supplierQuotation/quotation")
public class SupplierQuotationController extends BaseController {
    @Autowired
    private ISupplierQuotationService supplierQuotationService;

/**
 * 查询供应商报价记录 询价采购记录列表
 */
//@PreAuthorize("@ss.hasPermi('supplierQuotation:quotation:list')")
@GetMapping("/list")
        public TableDataInfo list(SupplierQuotation supplierQuotation) {
        startPage();
        List<SupplierQuotation> list = supplierQuotationService.selectSupplierQuotationList(supplierQuotation);
        return getDataTable(list);
    }
    
    /**
     * 导出供应商报价记录 询价采购记录列表
     */
    //@PreAuthorize("@ss.hasPermi('supplierQuotation:quotation:export')")
    @Log(title = "供应商报价记录 询价采购记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public R export(SupplierQuotation supplierQuotation) {
        List<SupplierQuotation> list = supplierQuotationService.selectSupplierQuotationList(supplierQuotation);
        ExcelUtil<SupplierQuotation> util = new ExcelUtil<SupplierQuotation>(SupplierQuotation. class);
        return util.exportExcel(list, "quotation");
    }

    /**
     * 获取供应商报价记录 询价采购记录详细信息
     */
    //@PreAuthorize("@ss.hasPermi('supplierQuotation:quotation:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        return R.success(supplierQuotationService.selectSupplierQuotationById(id));
    }

    /**
     * 新增供应商报价记录 询价采购记录
     */
    //@PreAuthorize("@ss.hasPermi('supplierQuotation:quotation:add')")
    @Log(title = "供应商报价记录 询价采购记录", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody SupplierQuotation supplierQuotation) {
        return toAjax(supplierQuotationService.insertSupplierQuotation(supplierQuotation));
    }

    /**
     * 修改供应商报价记录 询价采购记录
     */
    //@PreAuthorize("@ss.hasPermi('supplierQuotation:quotation:edit')")
    @Log(title = "供应商报价记录 询价采购记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody SupplierQuotation supplierQuotation) {
        return toAjax(supplierQuotationService.updateSupplierQuotation(supplierQuotation));
    }

    /**
     * 删除供应商报价记录 询价采购记录
     */
    //@PreAuthorize("@ss.hasPermi('supplierQuotation:quotation:remove')")
    @Log(title = "供应商报价记录 询价采购记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(supplierQuotationService.deleteSupplierQuotationByIds(ids));
    }

    /**
     * 供应商查看采购公告
     */
    //@PreAuthorize("@ss.hasPermi('supplierQuotation:quotation:list')")
    @GetMapping("/notice")
    public TableDataInfo notice() {
        startPage();
        List<Map<String,Object>> list = supplierQuotationService.selectNoticeList();
        return getDataTable(list);
    }
}
