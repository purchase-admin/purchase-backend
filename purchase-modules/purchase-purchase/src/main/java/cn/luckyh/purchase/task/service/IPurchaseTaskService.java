package cn.luckyh.purchase.task.service;

import cn.luckyh.purchase.task.domain.PurchaseTask;
import cn.luckyh.purchase.task.vo.PurchaseTaskDto;
import cn.luckyh.purchase.task.vo.PurchaseTaskVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 采购任务 Service接口
 *
 * @author purchase
 * @date 2021-03-22
 */
public interface IPurchaseTaskService extends IService<PurchaseTask> {
    /**
     * 查询采购任务
     *
     * @param id 采购任务 ID
     * @return 采购任务
     */
    PurchaseTask selectPurchaseTaskById(String id);

    /**
     * 查询采购任务 列表
     *
     * @param purchaseTask 采购任务
     * @return 采购任务 集合
     */
    List<PurchaseTaskVo> selectPurchaseTaskList(PurchaseTask purchaseTask);

    /**
     * 新增采购任务
     *
     * @param purchaseTask 采购任务
     * @return 结果
     */
    int insertPurchaseTask(PurchaseTask purchaseTask);

    /**
     * 修改采购任务
     *
     * @param purchaseTask 采购任务
     * @return 结果
     */
    int updatePurchaseTask(PurchaseTask purchaseTask);

    int onlyUpdatePurchaseTask(PurchaseTask purchaseTask);

    /**
     * 作废采购任务
     *
     * @param taskId 采购任务
     * @return 结果
     */
    int cancelPurchaseTask(String taskId);

    /**
     * 批量删除采购任务
     *
     * @param ids 需要删除的采购任务 ID
     * @return 结果
     */
    int deletePurchaseTaskByIds(String[] ids);

    /**
     * 删除采购任务 信息
     *
     * @param id 采购任务 ID
     * @return 结果
     */
    int deletePurchaseTaskById(String id);

    /**
     * 公开招标作废
     *
     * @param taskId
     * @return
     */
    int cancel(String taskId);

    void chooseSupplier(PurchaseTaskDto purchaseTaskDto);

}
