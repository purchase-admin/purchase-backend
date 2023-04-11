package cn.luckyh.purchase.budget.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 预算汇总-预算批数据关联表
 * @TableName budget_summary_batch
 */
@TableName(value ="budget_summary_batch")
@Data
public class BudgetSummaryBatch implements Serializable {
    /**
     * 预算汇总ID
     */
    private String summary_id;

    /**
     * 预算批数据ID
     */
    private String batch_id;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}