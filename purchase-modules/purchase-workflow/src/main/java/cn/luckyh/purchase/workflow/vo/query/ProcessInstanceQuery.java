package cn.luckyh.purchase.workflow.vo.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 流程查询对象.
 *
 * @author heng.wang
 * @since 2021/04/10 14:36
 */
@Data
public class ProcessInstanceQuery {

    @NotNull
    @ApiModelProperty("分页")
    private Integer pageNum;

    @NotNull
    @ApiModelProperty(value = "条数")
    private Integer pageSize;

    @ApiModelProperty("排序")
    private String sort;

    private String order;

    @ApiModelProperty(value = "发起人")
    private String starterBy;
    @ApiModelProperty(value = "流程名称")
    private String processInstanceName;

    @ApiModelProperty(value = "流程定义Key")
    private String ProcessDefinitionKey;
    @ApiModelProperty(value = "流程定义版本")
    private Integer processDefinitionVersion;

    @ApiModelProperty(value = "是否结束")
    private Boolean finished;
}
