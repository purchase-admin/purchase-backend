package cn.luckyh.purchase.workflow.vo.query;

import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/04/13 14:48
 */
@Data
public class TaskInstanceQuery {

    @ApiModelProperty(value = "流程Key", required = true)
    @NotNull(message = "请指定要发起的流程Key")
    private String processDefinitionKey;

    @ApiModelProperty(value = "流程名称")
    private String processInstanceName;

    @ApiModelProperty(value = "用户", hidden = true)
    private SysUser user;

    @ApiModelProperty(value = "状态", allowableValues = "completed")
    private String state;

    @ApiModelProperty(value = "流程实例ID")
    private String processInstanceId;

    @ApiModelProperty(value = "任务名称模糊查询")
    private String text;

    @ApiModelProperty(value = "办理人查询")
    private String assignment;

    @ApiModelProperty(value = "流程定义ID查询")
    private String processDefinitionId;

    @ApiModelProperty(value = "流程定义ID查询")
    private String includeProcessInstance;

    @NotNull
    @ApiModelProperty("分页")
    private Integer pageNum = 1;

    @NotNull
    @ApiModelProperty(value = "条数")
    private Integer pageSize = Integer.MAX_VALUE;

    @ApiModelProperty("排序")
    private String sort;

    private String order;
}
