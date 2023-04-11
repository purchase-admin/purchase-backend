package cn.luckyh.purchase.purchase.domain;

import java.math.BigDecimal;
import java.util.Date;

import cn.luckyh.purchase.common.core.domain.BaseEntity;
import cn.luckyh.purchase.common.utils.bean.Decimal2Serializer;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.luckyh.purchase.common.annotation.Excel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

/**
 * 采购申请 对象 purchase_apply
 *
 * @author zhouwenchao
 * @date 2021-03-20
 */
@Data
public class PurchaseApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 关联预算中对应项目id
     */
    @Excel(name = "关联预算中对应项目id")
    private String projectId;

    @TableField(exist = false)
    private String projectName;

    /**
     * 关联预算中对应的年份
     */
    @Excel(name = "关联预算中对应的年份")
    private String budgetYear;

    /**
     * 申购部门
     */
    @Excel(name = "申购部门")
    private String deptId;

    @TableField(exist = false)
    private String deptName;

    /**
     * 经办人（采购申请人）
     */
    @Excel(name = "经办人", readConverterExp = "采购申请人")
    private String userId;

    @TableField(exist = false)
    private String userName;

    /**
     * 经办人联系电话
     */
    @Excel(name = "经办人联系电话")
    private String userPhone;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private Long num;

    /**
     * 预算金额
     */
    @Excel(name = "预算金额")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal budgetMoney;

    /**
     * 交付日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交付日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryDay;

    /**
     * 申购原因
     */
    @Excel(name = "申购原因")
    private String reason;

    /**
     * 规格型号、技术参数及服务要求
     */
    @Excel(name = "规格型号、技术参数及服务要求")
    private String specification;

    /**
     * 推荐品牌或者供应商
     */
    @Excel(name = "推荐品牌或者供应商")
    private String supplier;

    /**
     * 推荐供应商联系人
     */
    @Excel(name = "推荐供应商联系人")
    private String supplierUsername;

    /**
     * 推荐供应商联系电话
     */
    @Excel(name = "推荐供应商联系电话")
    private String supplierPhone;

    /**
     * 项目预算(预算内;预算外)
     */
    @Excel(name = "项目预算(预算内;预算外)")
    private String budgetType;

    /**
     * 预算内（是否应急预算：0否，1是）
     */
    @Excel(name="是否应急预算：0否，1是")
    private String isUrgencyType;

    /**
     * 是否通过审批(0未审批，1已审批)
     */
    @Excel(name = "是否通过审批(0未审批，1已审批)")
    private String isOk;

    /**
     * 采购执行人
     */
    @Excel(name = "采购执行人")
    private String purchaseOperator;

    /**
     * 执行状态
     */
    @Excel(name = "执行状态")
    private String status;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 申请项目个数
     */
    @TableField(exist = false)
    private Integer projectNum;

    /**
     * 标题
     */
    private String title;

    /**
     * 流程实例ID
     */
    private String procId;


}
