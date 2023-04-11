package cn.luckyh.purchase.workflow.listener;

import cn.luckyh.purchase.common.enums.OperationEnum;
import cn.luckyh.purchase.common.events.BaseEvent;
import cn.luckyh.purchase.common.events.PurchaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/05/07 16:43
 */
@Component("PurchaseListener")
@Slf4j
public class PurchaseListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    public PurchaseListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * update status.
     * ${PurchaseListener.createEvent(execution,status)}.
     * status allows to 0,1,2,3.{@link cn.luckyh.purchase.budget.domain.BudgetBatch#status}
     *
     * @param execution execution
     *                  {@link cn.luckyh.purchase.budget.listener.BudgetEventListener#onApplicationEvent(cn.luckyh.purchase.common.events.BudgetEvent)}
     */
    @SuppressWarnings({"unused", "JavadocReference"})
    public void createEvent(DelegateExecution execution, String status) {
        if (log.isDebugEnabled()) {
            log.debug("=========采购申请=====执行监听器=========");
        }
        String processInstanceBusinessKey = execution.getProcessInstanceBusinessKey();
        //创建事件
        PurchaseEvent event = new PurchaseEvent(processInstanceBusinessKey, status, execution.getProcessInstanceId());
        BaseEvent<PurchaseEvent> baseEvent = new BaseEvent<>(this, event, OperationEnum.UPDATE);
        if (log.isDebugEnabled()) {
            log.debug("=========采购申请====发布事件:{}=========", baseEvent);
        }
        applicationEventPublisher.publishEvent(baseEvent);
    }
}
