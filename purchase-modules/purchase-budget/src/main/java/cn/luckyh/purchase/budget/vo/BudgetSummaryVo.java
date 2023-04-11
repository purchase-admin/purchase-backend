package cn.luckyh.purchase.budget.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/09/09 0009 17:20
 */
@Data
public class BudgetSummaryVo {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "标题", position = 1)
    private String title;

    @ApiModelProperty(value = "年份", position = 2)
    private String year;

    @ApiModelProperty(value = "流程实例ID", position = 3)
    private String procId;

    @ApiModelProperty(value = "状态", position = 4)
    private String status;

    @ApiModelProperty(value = "部门预算ID", position = 5)
    private String budgetBatchIds;

    private String assignee;
    private String taskName;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
