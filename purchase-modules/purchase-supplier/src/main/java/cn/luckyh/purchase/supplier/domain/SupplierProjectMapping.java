package cn.luckyh.purchase.supplier.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 供应商-项目-映射
 *
 * @TableName supplier_project_mapping
 */
@TableName(value = "supplier_project_mapping")
@Data
public class SupplierProjectMapping implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 项目ID
     */
    private String projectId;


}