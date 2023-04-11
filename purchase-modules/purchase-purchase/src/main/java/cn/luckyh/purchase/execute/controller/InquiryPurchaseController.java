package cn.luckyh.purchase.execute.controller;

import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.domain.Result;
import cn.luckyh.purchase.common.core.domain.ResultUtil;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.execute.domain.InquiryPurchase;
import cn.luckyh.purchase.execute.service.IInquiryPurchaseService;
import cn.luckyh.purchase.execute.vo.ChooseSupplierDto;
import cn.luckyh.purchase.execute.vo.InquiryPurchaseVo;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 询价采购 Controller
 *
 * @author purchase
 * @date 2021-03-24
 */
@RestController
@RequestMapping("/inquiryPurchase/purchase")
public class InquiryPurchaseController extends BaseController {
    @Autowired
    private IInquiryPurchaseService inquiryPurchaseService;

    /**
     * 查询询价采购 列表
     */
    //@PreAuthorize("@ss.hasPermi('inquiryPurchase:purchase:list')")
    @GetMapping("/list")
    public TableDataInfo list(InquiryPurchase inquiryPurchase) {
        startPage();
        List<InquiryPurchaseVo> list = inquiryPurchaseService.selectInquiryPurchaseList(inquiryPurchase);
        return getDataTable(list);
    }

    /**
     * 导出询价采购 列表
     */
    // //@PreAuthorize("@ss.hasPermi('inquiryPurchase:purchase:export')")
    // @Log(title = "询价采购 ", businessType = BusinessType.EXPORT)
    // @GetMapping("/export")
    // public R export(InquiryPurchase inquiryPurchase) {
    //     List<InquiryPurchase> list = inquiryPurchaseService.selectInquiryPurchaseList(inquiryPurchase);
    //     ExcelUtil<InquiryPurchase> util = new ExcelUtil<InquiryPurchase>(InquiryPurchase.class);
    //     return util.exportExcel(list, "purchase");
    // }

    /**
     * 获取询价采购 详细信息
     */
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        return R.success(inquiryPurchaseService.selectInquiryPurchaseById(id));
    }

    /**
     * 新增询价采购
     */
    // //@PreAuthorize("@ss.hasPermi('inquiryPurchase:purchase:add')")
    @Log(title = "询价采购 ", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody InquiryPurchase inquiryPurchase) {
        if (inquiryPurchase != null && inquiryPurchase.getEndTime() == null) {
            return R.error("请设置询价截止时间");
        }
        return toAjax(inquiryPurchaseService.insertInquiryPurchase(inquiryPurchase));
    }

    /**
     * 修改询价采购
     */
    // //@PreAuthorize("@ss.hasPermi('inquiryPurchase:purchase:edit')")
    @Log(title = "询价采购 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody InquiryPurchase inquiryPurchase) {
        inquiryPurchaseService.updateInquiryPurchase(inquiryPurchase);
        return R.success();
    }

    /**
     * 删除询价采购
     */
    // //@PreAuthorize("@ss.hasPermi('inquiryPurchase:purchase:remove')")
    @Log(title = "询价采购 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(inquiryPurchaseService.deleteInquiryPurchaseByIds(ids));
    }


    /**
     * 询价采购作废
     */
    // //@PreAuthorize("@ss.hasPermi('inquiryPurchase:purchase:remove')")
    @Log(title = "询价采购作废 ", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(String taskId) {
        if (StrUtil.isBlank(taskId)) return R.error("任务Id不能为空");
        return toAjax(inquiryPurchaseService.deleteInquiryPurchaseByTaskId(taskId));
    }

    /**
     * 挑选中标供应商
     *
     * @see #chooseSupplier2(ChooseSupplierDto)
     */
    //@PreAuthorize("@ss.hasPermi('inquiryPurchase:purchase:edit')")
    @Log(title = "挑选中标供应商 ", businessType = BusinessType.UPDATE)
    @PostMapping("/chooseSupplier")
    @Deprecated
    public R chooseSupplier(@RequestBody InquiryPurchase inquiryPurchase) {
        if (!inquiryPurchaseService.checkEndTime(inquiryPurchase)) {
            return R.error("询价截止日期还未到，禁止提前选择中标供应商！");
        }
        return toAjax(inquiryPurchaseService.chooseSupplier(inquiryPurchase));
    }

    /**
     * 挑选中标供应商
     */
    //@PreAuthorize("@ss.hasPermi('inquiryPurchase:purchase:edit')")
    @Log(title = "挑选中标供应商 ", businessType = BusinessType.UPDATE)
    @PostMapping("/chooseSupplier2")
    public Result<String> chooseSupplier2(@RequestBody ChooseSupplierDto chooseSupplierDto) {
        if (!inquiryPurchaseService.checkEndTime(chooseSupplierDto.getTaskId())) {
            return ResultUtil.error("询价截止日期还未到，禁止提前选择中标供应商！");
        }
        inquiryPurchaseService.chooseSupplier(chooseSupplierDto);
        return ResultUtil.success("操作成功");
    }

    /**
     * POST /inquiryPurchase/purchase/cancel -> 取消中标
     */
    @PostMapping("/cancel")
    @ApiOperation(value = "取消中标")
    public Result<String> cancelSupplier(String taskId, String supplier) {
        inquiryPurchaseService.cancelSupplier(taskId, supplier);
        return ResultUtil.success("操作成功");
    }


    /**
     * 参与报价
     */
    // //@PreAuthorize("@ss.hasPermi('inquiryPurchase:purchase:edit')")
    @Log(title = "参与报价 ", businessType = BusinessType.UPDATE)
    @PostMapping("/join")
    public R join(@RequestBody InquiryPurchase inquiryPurchase) {
        if (StrUtil.isBlank(inquiryPurchase.getTaskId()) || StrUtil.isBlank(inquiryPurchase.getSupplier())) {
            return R.error("taskId和supplier不能为空");
        }
        inquiryPurchase = inquiryPurchaseService.getOnlyOne(inquiryPurchase.getTaskId(), inquiryPurchase.getSupplier());
        inquiryPurchase.setIsJoin("1");
        inquiryPurchaseService.updateInquiryPurchase(inquiryPurchase);
        return R.success();
    }
}
