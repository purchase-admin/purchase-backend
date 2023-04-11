package cn.luckyh.purchase.budget.domain;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.luckyh.purchase.common.annotation.Excel;
import cn.luckyh.purchase.common.core.domain.BaseEntity;
import cn.luckyh.purchase.common.utils.bean.Decimal2Serializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 预算申请批对象 budget_batch
 *
 * @author purchase
 * @date 2021-04-15
 */
@Data
@Accessors(chain = true)
public class BudgetBatch extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 批次标题
     */
    @Excel(name = "批次标题")
    private String title;

    /**
     * 采购预算申请部门
     */
    @Excel(name = "采购预算申请部门")
    @NotEmpty(message = "部门不能为空")
    private String deptId;

    @TableField(exist = false)
    private String deptName;

    /**
     * 采购预算申请年份
     */
    @Excel(name = "采购预算申请年份")
    @NotEmpty(message = "年份不能为空")
    private String year;

    /**
     * 状态
     *
     * @see cn.luckyh.purchase.budget.constant.BudgetConstants;
     */
    @Excel(name = "状态", readConverterExp = "0=草稿,1=审核中,2=汇总审核中,3=审批完成")
    private String status;

    /**
     * 流程实例ID
     *
     * @see cn.luckyh.purchase.budget.constant.BudgetConstants;
     */
//    @Excel(name = "状态", readConverterExp = "0=草稿,1=审核中,2=汇总审核中,3=审批完成")
    private String procId;

    /**
     * 合计预算金额
     */
    @TableField(exist = false)
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal totalMoney;

    /**
     * 预算条数
     */
    @TableField(exist = false)
    private Integer budgetNum;
}
