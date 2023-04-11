package cn.luckyh.purchase.purchase.listener;

import javax.annotation.Resource;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import cn.luckyh.purchase.common.constant.WorkflowConstants;
import cn.luckyh.purchase.common.events.BaseEvent;
import cn.luckyh.purchase.common.events.PurchaseEvent;
import cn.luckyh.purchase.purchase.domain.PurchaseApply;
import cn.luckyh.purchase.purchase.service.IPurchaseApplyService;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/05/07 11:24
 */
@Component
@Slf4j
public class PurchaseEventListener {

    @Resource
    private IPurchaseApplyService purchaseApplyService;

    @EventListener
    public void onApplicationEvent(BaseEvent<PurchaseEvent> event) {
        PurchaseEvent data = event.getData();
        log.debug("采购申请接收事件.操作类型:{},数据:{}", event.getOperationEnum().name(), data);
        switch (event.getOperationEnum()) {
            case UPDATE:
                PurchaseApply byId = purchaseApplyService.getById(data.getBusinessKey());
                byId.setStatus(WorkflowConstants.RUNNING);
                byId.setProcId(data.getProcId());
                purchaseApplyService.updateById(byId);
                break;
            case DELETE:
                purchaseApplyService.deletePurchaseApplyById(data.getBusinessKey());
                break;
        }
    }
}
