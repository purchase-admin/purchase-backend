package cn.luckyh.purchase.workflow.vo.history;

import java.util.Date;
import java.util.List;

import org.flowable.engine.history.HistoricActivityInstance;

import cn.luckyh.purchase.common.core.domain.model.UserRepresentation;
import cn.luckyh.purchase.workflow.vo.runtime.CommentRepresentation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/04/06 11:09
 */
@Data
@ApiModel("节点记录")
public class HistoryRecord {

    @ApiModelProperty(value = "节点数据ID")
    private String id;

    @ApiModelProperty(value = "流程实例ID")
    private String processInstanceId;

    @ApiModelProperty(value = "流程定义ID")
    private String processDefinitionId;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "用时")
    private Long durationInMillis;

    @ApiModelProperty(value = "删除原因")
    private String deleteReason;

    @ApiModelProperty(value = "节点ID")
    private String activityId;

    @ApiModelProperty(value = "节点名称")
    private String activityName;

    @ApiModelProperty(value = "节点类型")
    private String activityType;

    @ApiModelProperty(value = "执行实例ID")
    private String executionId;

    @ApiModelProperty(value = "办理人")
    private UserRepresentation assignee;

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "审批结果")
    private Integer opinion;


    @ApiModelProperty(value = "意见")
    List<CommentRepresentation> comments;

    public HistoryRecord(HistoricActivityInstance historicActivityInstance) {
        this.id = historicActivityInstance.getId();
        this.processInstanceId = historicActivityInstance.getProcessInstanceId();
        this.processDefinitionId = historicActivityInstance.getProcessDefinitionId();
        this.startTime = historicActivityInstance.getStartTime();
        this.endTime = historicActivityInstance.getEndTime();
        this.durationInMillis = historicActivityInstance.getDurationInMillis();
        this.deleteReason = historicActivityInstance.getDeleteReason();
        this.activityId = historicActivityInstance.getActivityId();
        this.activityName = historicActivityInstance.getActivityName();
        this.activityType = historicActivityInstance.getActivityType();
        this.executionId = historicActivityInstance.getExecutionId();
        this.taskId = historicActivityInstance.getTaskId();
    }
}
