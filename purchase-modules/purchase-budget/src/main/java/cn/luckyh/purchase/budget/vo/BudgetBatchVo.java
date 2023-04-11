package cn.luckyh.purchase.budget.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/09/09 0009 15:32
 */
@Data
public class BudgetBatchVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String deptId;
    private String deptName;
    private String year;
    private String status;
    private String procId;
    private BigDecimal totalMoney;
    private Integer budgetNum;
    private String taskName;
    private String assignee;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
