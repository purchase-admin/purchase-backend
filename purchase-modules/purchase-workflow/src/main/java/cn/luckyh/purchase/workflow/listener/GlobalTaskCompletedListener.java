package cn.luckyh.purchase.workflow.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/09/08 0008 14:15
 */
@Slf4j
@Component
@SuppressWarnings("SpellCheckingInspection")
public class GlobalTaskCompletedListener extends AbstractFlowableEngineEventListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    protected void taskCompleted(FlowableEngineEntityEvent event) {
        log.debug("全局监听:监听类型:{};流程实例ID:{};流程定义ID:{}}", event.getType().name(), event.getProcessInstanceId(), event.getProcessDefinitionId());
        String processInstanceId = event.getProcessInstanceId();
        TaskEntity entity = (TaskEntity) event.getEntity();
        HashMap<String, String> map = new HashMap<>();
        map.put("flowid", event.getProcessInstanceId());
        map.put("userid", entity.getAssignee());
        rabbitTemplate.convertAndSend("weaver", "delete", map);
    }

}
