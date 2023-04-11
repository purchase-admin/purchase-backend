package cn.luckyh.purchase.budget.listener;

import cn.luckyh.purchase.budget.service.IBudgetBatchService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/04/26 14:50
 */
@Slf4j
@Component("BudgetEventListener")
public class BudgetEventListener {

    private final IBudgetBatchService budgetBatchService;

    public BudgetEventListener(IBudgetBatchService budgetBatchService) {
        this.budgetBatchService = budgetBatchService;
    }


    /**
     * 采购预算流程流程结束-执行监听器结束表达式
     * ${BudgetEventListener.endProcess(execution,"9")}
     * @param execution 执行实例
     * @param status 状态
     */
    @SuppressWarnings("unused")
    public void endProcess(DelegateExecution execution, String status) {
        String businessKey = execution.getProcessInstanceBusinessKey();
        budgetBatchService.updateBudgetBatchStatusById(businessKey, status, null);

    }
}
