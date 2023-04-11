package cn.luckyh.purchase.execute.vo;

import cn.luckyh.purchase.common.annotation.Excel;
import cn.luckyh.purchase.common.utils.bean.Decimal2Serializer;
import cn.luckyh.purchase.task.domain.PurchaseTask;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 询价采购信息查询对象 InquiryPurchaseVo
 *
 * @author purchase
 * @date 2021-03-24
 */
@Data
public class InquiryPurchaseVo {
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
    private String supplierName;

    private PurchaseTask purchaseTask;
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

    private String isOpen;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 供应商联系电话
     */
    private String phone;


    /**
     * 评分办法
     */
    private String file1;
    @ApiModelProperty(value = "多个文件")
    private List<SimpleFileVo> fileList;

    /**
     * 评分办法
     */
    private String file2;
    private String file2Name;


    /**
     * 定标报告
     */
    private String file3;
    private String file3Name;
    /**
     * 评分办法
     */
    private String file4;
    private String file4Name;

    /**
     * 定标报告
     */
    private String file5;
    private String file5Name;


    /**
     * 报价附件
     */
    private String bjfj;
    @ApiModelProperty(value = "报价附件")
    private String bjfjName;


    /**
     * 商务附件
     */
    private String swfj;
    @ApiModelProperty(value = "商务附件")
    private String swfjName;


    /**
     * 技术附件
     */
    private String jsfj;
    @ApiModelProperty(value = "技术附件")
    private String jsfjName;

    /**
     * 备注
     */
    private String remark;

}
