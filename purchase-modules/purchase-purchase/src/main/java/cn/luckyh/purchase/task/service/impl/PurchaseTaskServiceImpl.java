package cn.luckyh.purchase.task.service.impl;

import cn.luckyh.purchase.common.utils.bean.BeanUtils;
import cn.luckyh.purchase.execute.domain.InquiryPurchase;
import cn.luckyh.purchase.execute.mapper.InquiryPurchaseMapper;
import cn.luckyh.purchase.task.domain.PurchaseTask;
import cn.luckyh.purchase.task.mapper.PurchaseTaskMapper;
import cn.luckyh.purchase.task.service.IPurchaseTaskService;
import cn.luckyh.purchase.task.vo.PurchaseTaskDto;
import cn.luckyh.purchase.task.vo.PurchaseTaskVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 采购任务 Service业务层处理
 *
 * @author purchase
 * @date 2021-03-22
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class PurchaseTaskServiceImpl extends ServiceImpl<PurchaseTaskMapper, PurchaseTask> implements IPurchaseTaskService {
    @Resource
    private PurchaseTaskMapper purchaseTaskMapper;

    @Resource
    private InquiryPurchaseMapper inquiryPurchaseMapper;

    /**
     * 查询采购任务
     *
     * @param id 采购任务 ID
     * @return 采购任务
     */
    @Override
    public PurchaseTask selectPurchaseTaskById(String id) {
        return purchaseTaskMapper.selectById(id);
    }

    /**
     * 查询采购任务 列表
     *
     * @param purchaseTask 采购任务
     * @return 采购任务
     */
    @Override
    public List<PurchaseTaskVo> selectPurchaseTaskList(PurchaseTask purchaseTask) {
        return purchaseTaskMapper.selectPurchaseTaskList(purchaseTask);
    }

    /**
     * 新增采购任务
     *
     * @param purchaseTask 采购任务
     * @return 结果
     */
    @Override
    public int insertPurchaseTask(PurchaseTask purchaseTask) {
        //return purchaseTaskMapper.insertPurchaseTask(purchaseTask);
        return save(purchaseTask) ? 1 : 0;
    }

    /**
     * 修改采购任务
     *
     * @param purchaseTask 采购任务
     * @return 结果
     */
    @Override
    public int updatePurchaseTask(PurchaseTask purchaseTask) {
        LambdaQueryWrapper<InquiryPurchase> wrapper = new LambdaQueryWrapper();
        wrapper.eq(InquiryPurchase::getTaskId, purchaseTask.getId());
        inquiryPurchaseMapper.delete(wrapper);
        return purchaseTaskMapper.updateById(purchaseTask);
    }

    public int onlyUpdatePurchaseTask(PurchaseTask purchaseTask) {
        return purchaseTaskMapper.updateById(purchaseTask);
    }

    /**
     * 作废采购任务
     *
     * @param taskId 采购任务
     * @return 结果
     */
    @Override
    public int cancelPurchaseTask(String taskId) {
        return purchaseTaskMapper.cancelPurchaseTask(taskId);
    }

    /**
     * 批量删除采购任务
     *
     * @param ids 需要删除的采购任务 ID
     * @return 结果
     */
    @Override
    public int deletePurchaseTaskByIds(String[] ids) {
        return purchaseTaskMapper.deletePurchaseTaskByIds(ids);
    }

    /**
     * 删除采购任务 信息
     *
     * @param id 采购任务 ID
     * @return 结果
     */
    @Override
    public int deletePurchaseTaskById(String id) {
        return purchaseTaskMapper.deletePurchaseTaskById(id);
    }

    @Override
    public int cancel(String taskId) {
        LambdaQueryWrapper<InquiryPurchase> wrapper = new LambdaQueryWrapper();
        wrapper.eq(InquiryPurchase::getTaskId, taskId);
        inquiryPurchaseMapper.delete(wrapper);
        return purchaseTaskMapper.cancelPurchaseTask(taskId);
    }

    @Override
    public void chooseSupplier(PurchaseTaskDto purchaseTaskDto) {

        PurchaseTask purchaseTask = getById(purchaseTaskDto.getTaskId());
        if (purchaseTask == null) {
            throw new RuntimeException("taskId为" + purchaseTaskDto.getTaskId() + "的数据不存在");
        }
        purchaseTask.setSureContent(purchaseTaskDto.getSureContent());
        BeanUtils.copyProperties(purchaseTaskDto, purchaseTask, "id");
        onlyUpdatePurchaseTask(purchaseTask);
        // InquiryPurchase byId = inquiryPurchaseService.getById(purchaseTaskDto.getSupplierId());
        // if (byId == null) {
        //     throw new RuntimeException("id为" + purchaseTaskDto.getSupplierId() + "的数据不存在");
        // }
        // byId.setIsCheck("0");
        // inquiryPurchaseService.updateById(byId);
    }
}
