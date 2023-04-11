package cn.luckyh.purchase.execute.vo;

import cn.luckyh.purchase.common.annotation.Excel;
import cn.luckyh.purchase.common.utils.bean.Decimal2Serializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/10/11 0011 15:23
 */
@Data
public class AgencyPurchaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 中标供应商
     */
    @Excel(name = "中标供应商")
    @ApiModelProperty(value = "中标供应商")
    private String supplierId;


    @ApiModelProperty(value = "供应商名称")
    @TableField(exist = false)
    private String supplierName;

    /**
     *
     */
    @ApiModelProperty(value = "关联任务ID")
    @Excel(name = "关联任务ID")
    private String taskId;

    /**
     *
     */
    @ApiModelProperty(value = "中标金额")
    @Excel(name = "中标金额")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal chooseMoney;

    /**
     *
     */
    @ApiModelProperty(value = "招标公告(附件)")
    @Excel(name = "招标公告(附件)")
    private String tenderAnnouncement;

    @ApiModelProperty(value = "招标公告(附件)")
    @Excel(name = "招标公告(附件)")
    private String tenderAnnouncementName;

    /**
     *
     */
    @ApiModelProperty(value = "招标文件(附件)")
    @Excel(name = "招标文件(附件)")
    private String tenderFile;

    @ApiModelProperty(value = "招标文件(附件)")
    @Excel(name = "招标文件(附件)")
    private String tenderFileName;

    /**
     * 供应商签到表(附件)
     */
    @ApiModelProperty(value = "供应商签到表(附件)")
    @Excel(name = "供应商签到表(附件)")
    private String supplierSignIn;

    @ApiModelProperty(value = "供应商签到表(附件)")
    @Excel(name = "供应商签到表(附件)")
    private String supplierSignInName;

    /**
     *
     */
    @ApiModelProperty(value = "供应商投标文件(多附件)")
    @Excel(name = "供应商投标文件(多附件)")
    private String supplierBidFile;

    @ApiModelProperty(value = "供应商投标文件(多附件)")
    @Excel(name = "供应商投标文件(多附件)")
    private String supplierBidFileName;

    /**
     *
     */
    @ApiModelProperty(value = "评委签到表(附件)")
    @Excel(name = "评委签到表(附件)")
    private String judgingSignIn;

    @ApiModelProperty(value = "评委签到表(附件)")
    @Excel(name = "评委签到表(附件)")
    private String judgingSignInName;

    /**
     *
     */
    @ApiModelProperty(value = "评委评分(附件)")
    @Excel(name = "评委评分(附件)")
    private String judgingGrade;

    @ApiModelProperty(value = "评委评分(附件)")
    @Excel(name = "评委评分(附件)")
    private String judgingGradeName;
}
