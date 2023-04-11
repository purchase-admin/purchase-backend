package cn.luckyh.purchase.purchase.domain;

import cn.luckyh.purchase.common.annotation.Excel;
import cn.luckyh.purchase.common.core.domain.BaseEntity;
import cn.luckyh.purchase.common.utils.bean.Decimal2Serializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购申请项目 对象 purchase_apply_projects
 *
 * @author purchase
 * @date 2021-03-22
 */
@Data
public class PurchaseApplyProjects extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 预算项目ID
     */
    private String budgetId;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    private String projectName;

    /**
     * 申请ID
     */
    private String applyId;

    /**
     * 数量
     */
    @Excel(name = "数量")
    @Min(value = 1, message = "预算数量必须大于等于{value}")
    private String num;

    /**
     * 预算金额
     */
    @Excel(name = "预算金额")
    @JsonSerialize(using = Decimal2Serializer.class)
    @DecimalMin(value = "1", message = "预算金额必须大于或等于{value}")
    private BigDecimal budgetCost;

    /**
     * 交付日期
     */
    @Excel(name = "交付日期")
    private String deliveryDate;

    /**
     * 申购原因
     */
    @Excel(name = "申购原因")
    private String reason;

    /**
     * 规格型号参数及要求
     */
    @Excel(name = "规格型号参数及要求")
    private String specification;

    /**
     * 推荐品牌或供应商
     */
    @Excel(name = "推荐品牌或供应商")
    private String supplier;

    /**
     * 推荐供应商联系人
     */
    @Excel(name = "推荐供应商联系人")
    private String supplierContactName;

    /**
     * 推荐供应商联系电话
     */
    @Excel(name = "推荐供应商联系电话")
    private String supplierContactPhone;

    /**
     * 项目预算类型
     */
    @Excel(name = "项目预算类型")
    private String budgetType;

    /**
     * 是否应急预算
     */
    @Excel(name = "是否应急预算")
    @TableField(exist = false)
    private String isUrgencyType;

    /**
     * 预算项目
     */
    @Excel(name = "预算项目")
    private String projectId;

    /**
     * 乐观锁
     */
    private Long revision;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
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
     * 申购部门名称
     */
    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private String deptId;

    /**
     * 申请人名称
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 申请人联系电话
     */
    @TableField(exist = false)
    private String userPhone;

    /**
     * 采购方式
     */
    @TableField(exist = false)
    private String purchaseType;

    /**
     * 执行人
     */
    @TableField(exist = false)
    private String executorName;

    @TableField(exist = false)
    private String companyName;

    @TableField(exist = false)
    private String companyId;

    @TableField(exist = false)
    private String taskId;

    /**
     * 是否公开招标
     */
    @TableField(exist = false)
    private String isOpen;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 定标结果说明
     */
    @TableField(exist = false)
    private String sureContent;

    /**
     * 定标附件名称
     */
    @TableField(exist = false)
    private String sureFileName;

    /**
     * 定标附件路径
     */
    @TableField(exist = false)
    private String sureFilePath;

    /**
     * 定标附件id
     */
    @TableField(exist = false)
    private String sureFileId;

}
