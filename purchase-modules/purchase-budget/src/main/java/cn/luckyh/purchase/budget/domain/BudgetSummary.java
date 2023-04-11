package cn.luckyh.purchase.budget.domain;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableId;

import cn.luckyh.purchase.common.core.domain.BaseEntity;
import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 预算汇总.
 *
 * @author heng.wang
 * @since 2021/05/08 09:25
 */
@Data
@ApiModel("预算汇总")
public class BudgetSummary extends BaseEntity {

    @TableId
    @ApiModelProperty(value = "主键")
    private String id = IdUtil.getSnowflake(1L, 1L).nextIdStr();

    @ApiModelProperty(value = "标题", position = 1)
    private String title;

    @ApiModelProperty(value = "年份", position = 2)
    @NotNull(message = "请填写预算年份")
    private String year;

    @ApiModelProperty(value = "流程实例ID", position = 3)
    private String procId;

    @ApiModelProperty(value = "状态", position = 4)
    private String status;

    @ApiModelProperty(value = "部门预算ID", position = 5)
    private String budgetBatchIds;

}
