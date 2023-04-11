package cn.luckyh.purchase.common.events;

import lombok.Data;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/04/26 14:47
 */
@Data
public class BudgetEvent {

    private String businessKey;
    private String status;
    private String procId;

    public BudgetEvent(String businessKey, String Status, String procId) {
        this.businessKey = businessKey;
        this.status = Status;
        this.procId = procId;
    }

}
