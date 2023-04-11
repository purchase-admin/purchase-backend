package cn.luckyh.purchase.task.domain;

import cn.luckyh.purchase.common.annotation.Excel;
import cn.luckyh.purchase.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 采购任务 对象 purchase_task
 *
 * @author purchase
 * @date 2021-03-22
 */
@Data
public class PurchaseTask extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 项目
     */

    private String projectId;

    /**
     * 项目名称
     */
    @Excel(name = "项目")
    @TableField(exist = false)
    private String projectName;
    /**
     * 申请ID
     */
    private String applyId;

    /**
     * 是否公开招标
     */
    private String isOpen;

    /**
     * 执行人
     */
    @Excel(name = "执行人")
    private String executor;

    @TableField(exist = false)
    private String executorName;
    /**
     * 采购类型 1询价采购 2评选采购 3代理采购
     */
    @Excel(name = "采购类型")
    private String purchaseType;

    /**
     * 关联的代理采购信息ID
     */
    private String agencyId;

    /**
     * 部门ID
     */
    private String deptId;

    @TableField(exist = false)
    private String deptName;

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
     * 预算类型
     */
    @TableField(exist = false)
    private String budgetType;

    /**
     * 预算内是否紧急预算
     */
    @TableField(exist = false)
    private String isUrgencyType;

    /**
     * 预算金额
     */
    @TableField(exist = false)
    private String budgetCost;

    /**
     * 交付日期
     */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    /**
     * 推荐供应商
     */
    @TableField(exist = false)
    private String supplier;

    /**
     * 申购部门联系人
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 申购部门联系人电话
     */
    @TableField(exist = false)
    private String userPhone;

    /**
     * 采购任务ID（做关联用）
     */
    @TableField(exist = false)
    private String taskId;

    /**
     * 是否已录入代理采购数据
     */
    @TableField(exist = false)
    private String isSaveAgencyData;

    /**
     * 截止日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 是否已截止 0未截止 1已截止
     */
    @TableField(exist = false)
    private String isEnd;

    /**
     * 是否中标
     */
    @TableField(exist = false)
    private String isCheck;

    /**
     * 已参与的供应商名称
     *
     * @return
     */
    @TableField(exist = false)
    private String joinSupplierNames;

    /**
     * 定标结果说明
     */
    private String sureContent;


    @ApiModelProperty(value = "定标结果")
    private String file1;

    @ApiModelProperty(value = "定标报告")
    private String file2;

    @ApiModelProperty(value = "推荐表")
    private String file3;

    @ApiModelProperty(value = "公示函")
    private String file4;


}
