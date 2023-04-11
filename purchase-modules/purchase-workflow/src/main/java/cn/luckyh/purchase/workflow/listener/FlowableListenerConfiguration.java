package cn.luckyh.purchase.workflow.listener;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventDispatcher;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/09/08 0008 15:33
 */
@Component
public class FlowableListenerConfiguration implements ApplicationListener<ContextRefreshedEvent> {


    @Autowired
    private SpringProcessEngineConfiguration processEngineConfiguration;


    @Autowired
    private GlobalTaskCompletedListener taskCompletedListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        FlowableEventDispatcher eventDispatcher = processEngineConfiguration.getEventDispatcher();
        //任务结束监听
        eventDispatcher.addEventListener(taskCompletedListener, FlowableEngineEventType.TASK_COMPLETED);
    }
}
