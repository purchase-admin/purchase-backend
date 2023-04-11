package cn.luckyh.purchase.execute.domain;

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
 * 询价采购 对象 inquiry_purchase
 *
 * @author purchase
 * @date 2021-03-24
 */
@Data
public class InquiryPurchase extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 关联任务ID
     */
    private String taskId;

    /**
     * 供应商ID
     */
    @Excel(name = "供应商ID")
    private String supplier;

    /**
     * 供应商报价
     */
    @Excel(name = "供应商报价")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal quotation;

    /**
     * 中标供应商名称
     */
    @TableField(exist = false)
    private String checkedSupplierName;

    /**
     * 是否中标(0未中标，1已中标)
     */
    private String isCheck;

    /**
     * 是否参与报价（0否 1是）
     */
    private String isJoin;

    /**
     * 供应商名称
     */
    @TableField(exist = false)
    private String supplierName;

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

    @TableField(exist = false)
    private String isOpen;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 供应商联系电话
     */
    @TableField(exist = false)
    private String phone;

    /**
     * 评委评分表
     */
    private String file1;

    /**
     * 评分办法
     */
    private String file2;

    /**
     * 定标报告
     */
    private String file3;

    private String file4;

    private String file5;


    /**
     * 报价附件
     */
    private String bjfj;


    /**
     * 商务附件
     */
    private String swfj;


    /**
     * 技术附件
     */
    private String jsfj;


    @TableField(exist = false)
    private String bjfjName;
    @TableField(exist = false)
    private String swfjName;
    @TableField(exist = false)
    private String jsfjName;


    /**
     * 备注
     */
    private String remark;
}
