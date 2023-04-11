package cn.luckyh.purchase.common.events;

import lombok.Data;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/05/07 11:31
 */
@Data
public class PurchaseEvent {

    private String businessKey;
    private String status;
    private String procId;

    public PurchaseEvent(String businessKey, String status, String procId) {
        this.businessKey = businessKey;
        this.status = status;
        this.procId = procId;
    }
}
