package cn.luckyh.purchase.purchase.vo;

import cn.luckyh.purchase.common.core.domain.BaseEntity;
import cn.luckyh.purchase.common.utils.bean.Decimal2Serializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购相关信息 对象 PurchaseApplyVo
 *
 * @author purchase
 * @date 2021-03-19
 */
@Data
public class PurchaseApplyVo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 预算年份
     */
    private String budgetYear;

    /**
     * 填报部门
     */
    private Long deptId;

    /**
     * 填报部门名称
     */
    private String deptName;

    /**
     * 预算金额
     */
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal budgetMoney;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 数量
     */
    private String num;

    /**
     * 采购原因
     */
    private String reason;

    /**
     * 规格型号，技术要求
     */
    private String specification;

    /**
     * 供应商ID
     */
    private String supplier;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商报价
     */
    private String quotation;
}
