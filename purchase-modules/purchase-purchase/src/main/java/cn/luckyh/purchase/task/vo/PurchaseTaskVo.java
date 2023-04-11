package cn.luckyh.purchase.task.vo;

import cn.luckyh.purchase.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 采购任务.
 *
 * @author heng.wang
 * @since 2021/10/12 0012 17:35
 */
@Data
public class PurchaseTaskVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "项目")
    private String projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "申请ID")
    private String applyId;

    @ApiModelProperty(value = "是否公开招标")
    private String isOpen;

    @ApiModelProperty(value = "执行人")
    @Excel(name = "执行人")
    private String executor;

    @ApiModelProperty(value = "执行人名称")
    private String executorName;

    @ApiModelProperty(value = "采购类型 1询价采购 2评选采购 3代理采购")
    @Excel(name = "采购类型")
    private String purchaseType;

    @ApiModelProperty(value = "关联的代理采购信息ID")
    private String agencyId;

    @ApiModelProperty(value = "部门ID")
    private String deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "预算类型")
    private String budgetType;

    @ApiModelProperty(value = "预算内是否紧急预算")
    private String isUrgencyType;

    @ApiModelProperty(value = "预算金额")
    private String budgetCost;

    @ApiModelProperty(value = "交付日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    @ApiModelProperty(value = "推荐供应商")
    private String supplier;

    @ApiModelProperty(value = "申购部门联系人")
    private String userName;

    @ApiModelProperty(value = "申购部门联系人电话")
    private String userPhone;

    @ApiModelProperty(value = "采购任务ID（做关联用）")
    private String taskId;

    @ApiModelProperty(value = "是否已录入代理采购数据")
    private String isSaveAgencyData;

    @ApiModelProperty(value = "截止日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "是否已截止 0未截止 1已截止")
    private String isEnd;

    @ApiModelProperty(value = "是否中标")
    private String isCheck;

    @ApiModelProperty(value = "已参与的供应商名称")
    private String joinSupplierNames;

    @ApiModelProperty(value = "定标结果说明")
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
