package cn.luckyh.purchase.contract.domain;

import java.math.BigDecimal;
import java.util.Date;

import cn.luckyh.purchase.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.luckyh.purchase.common.annotation.Excel;
import lombok.Data;

/**
 * 合同管理 对象 contract
 *
 * @author purchase
 * @date 2021-04-19
 */
@Data
public class Contract extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 关联的采购项目申请id
     */
    @Excel(name = "关联的采购项目申请id")
    private String applyId;

    /**
     * 合同名称
     */
    @Excel(name = "合同名称")
    private String contractName;

    /**
     * 合同乙方名称
     */
    @Excel(name = "合同乙方名称")
    private String secondPartyName;

    /**
     * 合同金额
     */
    @Excel(name = "合同金额")
    private BigDecimal money;

    /**
     * 合同签订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "合同签订日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date contractDate;

    /**
     * 合同标的
     */
    @Excel(name = "合同标的")
    private String contractObject;

    /**
     * 附件
     */
    @Excel(name = "附件")
    private String file;
    /**
     * 备注
     */
    private String remark;



}
