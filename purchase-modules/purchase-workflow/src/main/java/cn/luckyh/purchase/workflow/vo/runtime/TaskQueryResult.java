package cn.luckyh.purchase.workflow.vo.runtime;

import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

import java.util.Date;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/6/3 0003 14:47
 */


public class TaskQueryResult extends TaskEntityImpl {

    private String processInstanceName;

    private Date endTime;

    private Date startTime;

    private String processInstanceStartUserId;

    private String businessKey;

    public String getProcessInstanceName() {
        return processInstanceName;
    }

    public void setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getProcessInstanceStartUserId() {
        return processInstanceStartUserId;
    }

    public void setProcessInstanceStartUserId(String processInstanceStartUserId) {
        this.processInstanceStartUserId = processInstanceStartUserId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
}
