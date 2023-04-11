package cn.luckyh.purchase.supplier.domain;

import java.util.Date;

import cn.luckyh.purchase.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.luckyh.purchase.common.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 供应商报价记录 询价采购记录对象 supplier_quotation
 *
 * @author purchase
 * @date 2021-03-23
 */
@Data
public class SupplierQuotation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 供应商ID
     */
    @Excel(name = "供应商ID")
    private String supplierId;

    /**
     * 采购申请ID
     */
    @Excel(name = "采购申请ID")
    private String taskId;

    /**
     * 采购部门
     */
    @Excel(name = "采购部门")
    private String purchaseOffice;

    /**
     * 联系人
     */
    @Excel(name = "联系人")
    private String purchaseContact;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String purchaseTelephone;

    /**
     * 交付日期
     */
    @Excel(name = "交付日期")
    private String deliveryDate;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    private String projectName;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private String numbers;

    /**
     * 项目预算
     */
    @Excel(name = "项目预算")
    private String projectBudget;

    /**
     * 规格参数及服务要求
     */
    @Excel(name = "规格参数及服务要求")
    private String requireContent;

    /**
     * 采购方式
     */
    @Excel(name = "采购方式")
    private String purchaseType;

    /**
     * 截止时间
     */
    @Excel(name = "截止时间")
    private String endDate;

    /**
     * 产品技术规格
     */
    @Excel(name = "产品技术规格")
    private String productStandard;

    /**
     * 产品其他说明
     */
    @Excel(name = "产品其他说明")
    private String productExplain;

    /**
     * 交货日期
     */
    @Excel(name = "交货日期")
    private String supplierDeliveryDate;

    /**
     * 项目报价
     */
    @Excel(name = "项目报价")
    private String projectPrice;

    /**
     * 附件
     */
    @Excel(name = "附件")
    private String files;

    /**
     * 供应商联系人
     */
    @Excel(name = "供应商联系人")
    private String supplierContactName;

    /**
     * 供应商联系电话
     */
    @Excel(name = "供应商联系电话")
    private String supplierContactPhone;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 更新人
     */
    @Excel(name = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("supplierId", getSupplierId())
                .append("taskId", getTaskId())
                .append("purchaseOffice", getPurchaseOffice())
                .append("purchaseContact", getPurchaseContact())
                .append("purchaseTelephone", getPurchaseTelephone())
                .append("deliveryDate", getDeliveryDate())
                .append("projectName", getProjectName())
                .append("numbers", getNumbers())
                .append("projectBudget", getProjectBudget())
                .append("requireContent", getRequireContent())
                .append("purchaseType", getPurchaseType())
                .append("endDate", getEndDate())
                .append("productStandard", getProductStandard())
                .append("productExplain", getProductExplain())
                .append("supplierDeliveryDate", getSupplierDeliveryDate())
                .append("projectPrice", getProjectPrice())
                .append("files", getFiles())
                .append("supplierContactName", getSupplierContactName())
                .append("supplierContactPhone", getSupplierContactPhone())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
