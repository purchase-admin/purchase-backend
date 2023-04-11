package cn.luckyh.purchase.common.events.workflow;

import org.springframework.context.ApplicationEvent;

/**
 * Event for start process.
 *
 * @author heng.wang
 * @since 2021/04/27 11:38
 */
public class StartProcessEvent extends ApplicationEvent {

    private String businessKey;
    private String processKey;

    private String name;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     * which the event is associated (never {@code null})
     * @param businessKey
     * @param processKey
     */
    public StartProcessEvent(Object source, String businessKey, String processKey, String name) {
        super(source);
        this.businessKey = businessKey;
        this.processKey = processKey;
        this.name = name;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
