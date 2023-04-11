package cn.luckyh.purchase.budget.domain;

import cn.luckyh.purchase.common.annotation.Excel;
import cn.luckyh.purchase.common.core.domain.BaseEntity;
import cn.luckyh.purchase.common.utils.bean.Decimal2Serializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购预算 对象 budget
 *
 * @author purchase
 * @date 2021-03-19
 */
@Data
public class Budget extends  BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 项目
     */

    private Long projectId;

    @Excel(name = "项目")
    @TableField(exist = false)
    private String projectName;

    /**
     * 批ID
     */
    private String batchId;

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
    @TableField(exist = false)
    private String deptName;

    /**
     * 填报人
     */
    @Excel(name = "填报人")
    private String userId;

    /**
     * 填报人账号
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 填报人姓名
     */
    @Excel(name = "填报人姓名")
    @TableField(exist = false)
    private String nickName;

    /**
     * 规格型号、技术要求
     */
    @Excel(name = "规格型号、技术要求")
    private String specification;

    /**
     * 资金来源
     */
    @Excel(name = "资金来源")
    private String moneyFrom;

    /**
     * 预算金额
     */
    @Excel(name = "预算金额")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal budgetMoney;

    /**
     * 预算数量
     */
    private Integer num;

    /**
     * 剩余预算数量
     */
    private Integer restNum;

    /**
     * 剩余预算金额
     */
    @TableField(exist = false)
    @JsonSerialize(using = Decimal2Serializer.class)
    private String restMoney;

    /**
     * 审批状态：0未审批 1预算审批中 2预算审批结束 3汇总审批中 4汇总审批结束
     */
    private String approveStatus;

    @Excel(name = "备注")
    private String remark;

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

    @TableField(exist = false)
    private String batchYear;

}
