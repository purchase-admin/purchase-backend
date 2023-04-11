package cn.luckyh.purchase.workflow.vo.history;

import cn.luckyh.purchase.common.core.domain.model.UserRepresentation;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.history.HistoricProcessInstance;

import java.util.Date;
import java.util.List;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/04/21 16:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRecordResult {
    @ApiModelProperty(value = "流程实例ID")
    private String processInstanceId;

    @ApiModelProperty(value = "业务数据ID")
    private String businessKey;

    @ApiModelProperty(value = "发起人")
    private UserRepresentation processStartUser;

    @ApiModelProperty(value = "流程名称")
    private String processInstanceName;

    @ApiModelProperty(value = "流程发起时间")
    private Date startTime;

    @ApiModelProperty(value = "流程结束时间")
    private Date endTime;

    /**
     * 0:否.
     * 1:是.
     */
    @ApiModelProperty(value = "是否第一个节点", allowableValues = "0,1")
    private Integer isFirst;

    @ApiModelProperty(value = "作废原因")
    private String deleteReason;

    @ApiModelProperty(value = "流程节点记录")
    private List<HistoryRecord> records;

    @ApiModelProperty(value = "当前流程所有节点")
    private List<FlowElement> activities;

    public HistoryRecordResult(HistoricProcessInstance historicProcessInstance, UserRepresentation starterRepresentation) {
        this.processInstanceName = historicProcessInstance.getName();
        this.businessKey = historicProcessInstance.getBusinessKey();
        this.startTime = historicProcessInstance.getStartTime();
        this.endTime = historicProcessInstance.getEndTime();
        this.processInstanceId = historicProcessInstance.getId();
        this.deleteReason = historicProcessInstance.getDeleteReason();
        this.processStartUser = starterRepresentation;
    }
}
