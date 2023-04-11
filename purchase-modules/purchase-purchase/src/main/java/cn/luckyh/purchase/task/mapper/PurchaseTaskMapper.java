package cn.luckyh.purchase.task.mapper;

import cn.luckyh.purchase.task.domain.PurchaseTask;
import cn.luckyh.purchase.task.vo.PurchaseTaskVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 采购任务 Mapper接口
 *
 * @author purchase
 * @date 2021-03-22
 */
public interface PurchaseTaskMapper extends BaseMapper<PurchaseTask> {

    /**
     * 查询采购任务 列表
     *
     * @param purchaseTask 采购任务
     * @return 采购任务 集合
     */
    List<PurchaseTaskVo> selectPurchaseTaskList(PurchaseTask purchaseTask);


    /**
     * 作废
     *
     * @param taskId
     * @return
     */
    int cancelPurchaseTask(String taskId);

    /**
     * 删除采购任务
     *
     * @param id 采购任务 ID
     * @return 结果
     */
    int deletePurchaseTaskById(String id);

    /**
     * 批量删除采购任务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deletePurchaseTaskByIds(String[] ids);
}
