package cn.luckyh.purchase.task.controller;

import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.domain.Result;
import cn.luckyh.purchase.common.core.domain.ResultUtil;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.execute.domain.InquiryPurchase;
import cn.luckyh.purchase.execute.service.IInquiryPurchaseService;
import cn.luckyh.purchase.task.domain.PurchaseTask;
import cn.luckyh.purchase.task.service.IPurchaseTaskService;
import cn.luckyh.purchase.task.vo.PurchaseTaskDto;
import cn.luckyh.purchase.task.vo.PurchaseTaskVo;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 采购任务 Controller
 *
 * @author purchase
 * @date 2021-03-22
 */
@RestController
@RequestMapping("/purchaseTask/task")
public class PurchaseTaskController extends BaseController {
    @Autowired
    private IPurchaseTaskService purchaseTaskService;

    @Autowired
    private IInquiryPurchaseService inquiryPurchaseService;

    /**
     * 查询采购任务 列表
     */
    //@PreAuthorize("@ss.hasPermi('purchaseTask:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(PurchaseTask purchaseTask) {
        startPage();
        List<PurchaseTaskVo> list = purchaseTaskService.selectPurchaseTaskList(purchaseTask);
        return getDataTable(list);
    }


    /**
     * 获取采购任务 详细信息
     */
    //@PreAuthorize("@ss.hasPermi('purchaseTask:task:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        return R.success(purchaseTaskService.selectPurchaseTaskById(id));
    }

    /**
     * 新增采购任务
     */
    //@PreAuthorize("@ss.hasPermi('purchaseTask:task:add')")
    @Log(title = "采购任务 ", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody PurchaseTask purchaseTask) {
        return toAjax(purchaseTaskService.insertPurchaseTask(purchaseTask));
    }

    /**
     * 修改采购任务
     */
    //@PreAuthorize("@ss.hasPermi('purchaseTask:task:edit')")
    @Log(title = "采购任务 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody PurchaseTask purchaseTask) {
        return toAjax(purchaseTaskService.updatePurchaseTask(purchaseTask));
    }

    /**
     * 公开招标任务作废
     */
    //@PreAuthorize("@ss.hasPermi('purchaseTask:task:edit')")
    @Log(title = "公开招标任务作废 ", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel")
    public R cancel(@RequestBody PurchaseTask purchaseTask) {
        if (StrUtil.isBlank(purchaseTask.getTaskId())) return R.error("taskId不能为空");
        return toAjax(purchaseTaskService.cancel(purchaseTask.getTaskId()));
    }

    /**
     * 删除采购任务
     */
    //@PreAuthorize("@ss.hasPermi('purchaseTask:task:remove')")
    @Log(title = "采购任务 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(purchaseTaskService.deletePurchaseTaskByIds(ids));
    }

    /**
     * 查询当前登录人待办的采购任务
     */
    //@PreAuthorize("@ss.hasPermi('purchaseTask:task:list')")
    @GetMapping("/getPurchaseTaskToDo")
    public TableDataInfo getPurchaseTaskToDo(PurchaseTask purchaseTask) {
        purchaseTask.setExecutor(SecurityUtils.getUsername());
        startPage();
        List<PurchaseTaskVo> list = purchaseTaskService.selectPurchaseTaskList(purchaseTask);
        return getDataTable(list);
    }

    /**
     * @see #isSure2(PurchaseTaskDto)
     * 填写定标结果说明和定标结果附件
     */
    //@PreAuthorize("@ss.hasPermi('purchaseTask:task:edit')")
    @Log(title = "填写定标结果说明和定标结果附件 ", businessType = BusinessType.UPDATE)
    @PostMapping("/isSure")
    @Deprecated
    public R isSure(@RequestBody PurchaseTask purchaseTask) {
        int result = 0;
        if (StrUtil.isBlank(purchaseTask.getId())) {
            return R.error("id不能为空");
        }
        if (StrUtil.isBlank(purchaseTask.getTaskId())) {
            return R.error("taskId不能为空");
        }
        try {
            PurchaseTask pt = purchaseTaskService.getById(purchaseTask.getTaskId());
            if (pt == null) {
                return R.error("taskId为" + purchaseTask.getTaskId() + "的数据不存在");
            }
            pt.setSureContent(purchaseTask.getSureContent());
            // pt.setSureFileName(purchaseTask.getSureFileName());
            // pt.setSureFilePath(purchaseTask.getSureFilePath());
            // pt.setSureFileId(purchaseTask.getSureFileId());
            result = purchaseTaskService.onlyUpdatePurchaseTask(pt);
        } catch (RuntimeException e) {
            e.printStackTrace();
            InquiryPurchase byId = inquiryPurchaseService.getById(purchaseTask.getId());
            if (byId == null) {
                return R.error("id为" + purchaseTask.getId() + "的数据不存在");
            }
            byId.setIsCheck("0");
            inquiryPurchaseService.updateById(byId);
        }
        return toAjax(result);
    }

    /**
     * 填写定标结果说明和定标结果附件
     */
    //@PreAuthorize("@ss.hasPermi('purchaseTask:task:edit')")
    @Log(title = "填写定标结果说明和定标结果附件 ", businessType = BusinessType.UPDATE)
    @PostMapping("/isSure2")
    public Result<String> isSure2(@RequestBody @Valid PurchaseTaskDto purchaseTaskDto) {
        purchaseTaskService.chooseSupplier(purchaseTaskDto);
        return ResultUtil.success("保存成功");
    }


}
