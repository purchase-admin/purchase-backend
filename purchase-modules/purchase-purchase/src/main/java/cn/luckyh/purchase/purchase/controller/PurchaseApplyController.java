package cn.luckyh.purchase.purchase.controller;

import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.domain.Result;
import cn.luckyh.purchase.common.core.domain.ResultUtil;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.common.utils.poi.ExcelUtil;
import cn.luckyh.purchase.purchase.domain.PurchaseApply;
import cn.luckyh.purchase.purchase.service.IPurchaseApplyService;
import cn.luckyh.purchase.purchase.vo.PurchaseApplyListVo;
import cn.luckyh.purchase.purchase.vo.PurchaseApplyVo;
import cn.luckyh.purchase.workflow.vo.runtime.CreateProcessInstanceRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 采购申请 Controller
 *
 * @author zhouwenchao
 * @date 2021-03-20
 */
@RestController
@RequestMapping("/purchase/apply")
public class PurchaseApplyController extends BaseController {
    @Autowired
    private IPurchaseApplyService purchaseApplyService;

    /**
     * 查询采购申请 列表
     */
    //@PreAuthorize("@ss.hasPermi('purchase:apply:list')")
    @GetMapping("/list")
    public TableDataInfo list(PurchaseApply purchaseApply) {
        startPage();
        List<PurchaseApplyListVo> list = purchaseApplyService.selectPurchaseApplyList(purchaseApply);
        return getDataTable(list);
    }

    /**
     * 导出采购申请 列表
     */
    //@PreAuthorize("@ss.hasPermi('purchase:apply:export')")
    @Log(title = "采购申请 ", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public R export(PurchaseApply purchaseApply) {
        List<PurchaseApplyListVo> list = purchaseApplyService.selectPurchaseApplyList(purchaseApply);
        ExcelUtil<PurchaseApplyListVo> util = new ExcelUtil<PurchaseApplyListVo>(PurchaseApplyListVo.class);
        return util.exportExcel(list, "apply");
    }

    /**
     * 获取采购申请 详细信息
     */
    //@PreAuthorize("@ss.hasPermi('purchase:apply:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        return R.success(purchaseApplyService.selectPurchaseApplyById(id));
    }

    /**
     * 新增采购申请
     */
    //@PreAuthorize("@ss.hasPermi('purchase:apply:add')")
    @Log(title = "采购申请 ", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody PurchaseApply purchaseApply) {
        return toAjax(purchaseApplyService.insertPurchaseApply(purchaseApply));
    }

    /**
     * 新增采购申请和批次数数据
     */
    //@PreAuthorize("@ss.hasPermi('purchase:apply:add')")
    @Log(title = "部门采购申请新增 ", businessType = BusinessType.INSERT)
    @PostMapping("/saveBatch")
    public R add(@RequestBody List<PurchaseApply> purchaseApplys) {
        if(purchaseApplyService.insertPurchaseApplys(purchaseApplys)){
            return R.success("采购申请数据保存成功");
        }
        return R.error("采购申请数据保存失败，请联系管理员");
    }

    /**
     * 修改采购申请
     */
    //@PreAuthorize("@ss.hasPermi('purchase:apply:edit')")
    @Log(title = "采购申请 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody PurchaseApply purchaseApply) {
        return toAjax(purchaseApplyService.updatePurchaseApply(purchaseApply));
    }

    /**
     * 删除采购申请
     */
    //@PreAuthorize("@ss.hasPermi('purchase:apply:remove')")
    @Log(title = "采购申请 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(purchaseApplyService.deletePurchaseApplyByIds(ids));
    }

    /**
     * 合同选择对应的项目
     * @param vo
     * @return
     */
    //@PreAuthorize("@ss.hasPermi('purchase:apply:list')")
    @GetMapping("/contractProjectList")
    public TableDataInfo contractProjectList(PurchaseApplyVo vo) {
        startPage();
        List<PurchaseApplyVo> list = purchaseApplyService.selectContractProjectList(vo);
        return getDataTable(list);
    }


    /**
     * POST /purchase/apply/start -> 采购申请提交
     */
    @PostMapping("/start")
    public Result<String> start(@RequestBody CreateProcessInstanceRepresentation representation) {
        String processInstanceId = purchaseApplyService.startProcess(representation);
        return ResultUtil.data(processInstanceId, "流程发起成功");
    }
}
