package cn.luckyh.purchase.workflow.vo.runtime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * simple task completion model.
 *
 * @author heng.wang
 * @since 2021/04/19 10:47
 */
@ApiModel(description = "task complete model")
@Data
public class TaskCompleteRepresentation {

    @ApiModelProperty(value = "办理人")
    private String assignee;
    @ApiModelProperty(value = "审批结果")
    private Integer opinion;
    @ApiModelProperty(value = "意见")
    private String comment;

}
