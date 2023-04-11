package cn.luckyh.purchase.execute.domain;

import cn.luckyh.purchase.common.annotation.Excel;
import cn.luckyh.purchase.common.core.domain.BaseEntity;
import cn.luckyh.purchase.common.utils.bean.Decimal2Serializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 代理采购 对象 agency_purchase
 *
 * @author purchase
 * @date 2021-03-24
 */
@Data
public class AgencyPurchase extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 中标供应商
     */
    @Excel(name = "中标供应商")
    private String supplierId;

    /**
     * 供应商名称
     */
    @TableField(exist = false)
    private String supplierName;

    /**
     * 关联任务ID
     */
    @Excel(name = "关联任务ID")
    private String taskId;

    /**
     * 中标金额
     */
    @Excel(name = "中标金额")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal chooseMoney;

    /**
     * 招标公告(附件)
     */
    @Excel(name = "招标公告(附件)")
    private String tenderAnnouncement;

    /**
     * 招标文件(附件)
     */
    @Excel(name = "招标文件(附件)")
    private String tenderFile;

    /**
     * 供应商签到表(附件)
     */
    @Excel(name = "供应商签到表(附件)")
    private String supplierSignIn;

    /**
     * 供应商投标文件(多附件)
     */
    @Excel(name = "供应商投标文件(多附件)")
    private String supplierBidFile;

    /**
     * 评委签到表(附件)
     */
    @Excel(name = "评委签到表(附件)")
    private String judgingSignIn;

    /**
     * 评委评分(附件)
     */
    @Excel(name = "评委评分(附件)")
    private String judgingGrade;


}
