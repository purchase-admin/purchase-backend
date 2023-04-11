package cn.luckyh.purchase.workflow.servicetask;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.delegate.ActivityBehavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/04/28 15:05
 */
@Slf4j
@Component(value = "SuspendServiceTask")
public class SuspendServiceTask implements JavaDelegate, ActivityBehavior {

    @Autowired
    private RuntimeService runtimeServiceBean;

    @Override
    public void execute(DelegateExecution execution) {
        log.debug("==========测试服务方法挂起流程==========");
        runtimeServiceBean.suspendProcessInstanceById(execution.getProcessInstanceId());
    }
}
