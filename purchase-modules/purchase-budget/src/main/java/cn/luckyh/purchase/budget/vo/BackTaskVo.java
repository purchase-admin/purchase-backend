package cn.luckyh.purchase.budget.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 汇总审批退回 rest 对象.
 *
 * @author heng.wang
 * @since 2021/6/2 0002 17:01
 */
@Data
@ApiModel("汇总审批退回预算申请 rest model")
public class BackTaskVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "流程业务数据ID不能为空")
    private String id;

    @NotEmpty(message = "预算数据ID不能为空")
    private String ids;

    @NotEmpty(message = "退回意见不能为空")
    private String comment;
}
