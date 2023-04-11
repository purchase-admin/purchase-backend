package cn.luckyh.purchase.workflow.vo.runtime;

import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.flowable.engine.history.HistoricProcessInstance;

import java.util.Date;
import java.util.Objects;

/**
 * simple representation model.
 *
 * @author heng.wang
 * @since 2021/09/14 0014 18:01
 */
@ApiModel("simple processinstance rest model")
@Data
public class SimpleProcessInstanceRepresentation {

    @ApiModelProperty(value = "流程实例ID")
    private String processInstanceId;

    @ApiModelProperty(value = "当前节点名称")
    private String taskName;

    @ApiModelProperty(value = "当前任务ID")
    private String taskId;

    @ApiModelProperty(value = "当前办理人")
    private SysUser currentAssignee;

    private Boolean isEnd;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;


    public SimpleProcessInstanceRepresentation(HistoricProcessInstance processInstance) {
        this.processInstanceId = processInstance.getId();
        this.startTime = processInstance.getStartTime();
        this.endTime = processInstance.getEndTime();
        this.isEnd = Objects.isNull(processInstance.getEndTime());
    }
}
