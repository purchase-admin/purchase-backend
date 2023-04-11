package cn.luckyh.purchase.purchase.controller;

import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.purchase.domain.PurchaseApplyProjects;
import cn.luckyh.purchase.purchase.service.IPurchaseApplyProjectsService;
import cn.luckyh.purchase.purchase.vo.PurchaseApplyProjectsVo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 采购申请项目 Controller
 *
 * @author purchase
 * @date 2021-03-22
 */
@RestController
@RequestMapping("/purchaseProject/purchaseProjects")
public class PurchaseApplyProjectsController extends BaseController {
    @Autowired
    private IPurchaseApplyProjectsService purchaseApplyProjectsService;

    /**
     * 查询采购申请项目 列表
     */
    //@PreAuthorize("@ss.hasPermi('purchaseProject:purchaseProjects:list')")
    @GetMapping("/list")
    public TableDataInfo list(PurchaseApplyProjects purchaseApplyProjects) {
        startPage();
        List<PurchaseApplyProjectsVo> list = purchaseApplyProjectsService.selectPurchaseApplyProjectsList(purchaseApplyProjects);
        return getDataTable(list);
    }


    /**
     * 获取采购申请项目 详细信息
     */
    //@PreAuthorize("@ss.hasPermi('purchaseProject:purchaseProjects:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        return R.success(purchaseApplyProjectsService.selectPurchaseApplyProjectsById(id));
    }

    /**
     * 新增采购申请项目
     */
    //@PreAuthorize("@ss.hasPermi('purchaseProject:purchaseProjects:add')")
    @Log(title = "采购申请项目 ", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody @Valid PurchaseApplyProjects purchaseApplyProjects) {
        if ("预算内".equals(purchaseApplyProjects.getPurchaseType())&&
                StringUtil.isEmpty(purchaseApplyProjects.getProjectId())) {
            return R.error("projectId不能为空");
        }
        if (purchaseApplyProjects.getBudgetCost() == null) {
            return R.error("采购预算金额不能为空");
        }
        if (purchaseApplyProjects.getNum() == null) {
            return R.error("采购项目数量不能为空");
        }
        Map<String, Object> verifyResult = purchaseApplyProjectsService.verifyMoneyAndNum(purchaseApplyProjects);
        if (!(Boolean) verifyResult.get("isOk")) {
            return R.error("校验不通过:" + verifyResult.get("errorMsg"));
        }
        return toAjax(purchaseApplyProjectsService.insertPurchaseApplyProjects(purchaseApplyProjects));
    }

    /**
     * 修改采购申请项目
     */
    //@PreAuthorize("@ss.hasPermi('purchaseProject:purchaseProjects:edit')")
    @Log(title = "采购申请项目 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody PurchaseApplyProjects purchaseApplyProjects) {
        return purchaseApplyProjectsService.updatePurchaseApplyProjects(purchaseApplyProjects);
    }

    /**
     * 删除采购申请项目
     */
    //@PreAuthorize("@ss.hasPermi('purchaseProject:purchaseProjects:remove')")
    @Log(title = "采购申请项目 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(purchaseApplyProjectsService.deletePurchaseApplyProjectsByIds(ids));
    }
}
