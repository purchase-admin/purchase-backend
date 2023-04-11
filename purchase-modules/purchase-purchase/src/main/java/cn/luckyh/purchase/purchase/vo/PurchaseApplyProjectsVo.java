package cn.luckyh.purchase.purchase.vo;

import cn.luckyh.purchase.common.annotation.Excel;
import cn.luckyh.purchase.common.utils.bean.Decimal2Serializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/10/12 0012 17:49
 */
@Data
public class PurchaseApplyProjectsVo implements Serializable {

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
    private String num;

    /**
     * 预算金额
     */
    @Excel(name = "预算金额")
    @JsonSerialize(using = Decimal2Serializer.class)
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
     * 申购部门名称
     */
    private String deptName;

    private String deptId;

    /**
     * 申请人名称
     */
    private String userName;

    /**
     * 申请人联系电话
     */
    private String userPhone;

    /**
     * 采购方式
     */
    private String purchaseType;

    /**
     * 执行人
     */
    private String executorName;

    private String companyName;

    private String companyId;

    private String taskId;

    /**
     * 是否公开招标
     */
    private String isOpen;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 定标结果说明
     */
    private String sureContent;

    @ApiModelProperty(value = "定标结果")
    private String file1;

    @ApiModelProperty(value = "定标结果_文件名称")
    private String file1Name;

    @ApiModelProperty(value = "定标报告")
    private String file2;

    @ApiModelProperty(value = "定标报告_文件名称")
    private String file2Name;

    @ApiModelProperty(value = "推荐表")
    private String file3;

    @ApiModelProperty(value = "推荐表_文件名称")
    private String file3Name;

    @ApiModelProperty(value = "公示函")
    private String file4;

    @ApiModelProperty(value = "公示函_文件名称")
    private String file4Name;


}
