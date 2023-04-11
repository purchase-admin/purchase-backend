package cn.luckyh.purchase.budget.vo;

import cn.luckyh.purchase.common.annotation.Excel;
import cn.luckyh.purchase.common.core.domain.BaseEntity;
import cn.luckyh.purchase.common.utils.bean.Decimal2Serializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购预算汇总 对象 BudgetCollectVo
 *
 * @author purchase
 * @date 2021-03-19
 */
@Data
public class BudgetCollectVo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 预算年份
     */
    @Excel(name = "预算年份")
    private String budgetYear;

    /**
     * 填报部门
     */
    @Excel(name = "填报部门")
    private Long deptId;

    /**
     * 填报部门名称
     */
    @Excel(name = "填报部门名称")
    private String deptName;

    /**
     * 预算金额
     */
    @Excel(name = "预算金额")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal budgetMoney;

}
