package cn.luckyh.purchase.workflow.vo.runtime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

/**
 * processInstance start model.
 *
 * @author heng.wang
 * @since 2021/03/31 09:05
 */
@Data
@ApiModel(description = "流程启动 Rest 对象")
public class CreateProcessInstanceRepresentation {

    @ApiModelProperty(value = "流程Key", required = true)
    @NotNull(message = "请指定要发起的流程Key")
    private String processDefinitionKey;

    @ApiModelProperty(value = "业务数据主键", position = 1)
    private String businessKey;

    @ApiModelProperty(value = "标题", position = 2)
    private String title;

    @ApiModelProperty(value = "表单数据", position = 3)
    private HashMap<String, Object> variables;
}
