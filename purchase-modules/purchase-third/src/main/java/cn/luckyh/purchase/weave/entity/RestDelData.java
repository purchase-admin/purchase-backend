package cn.luckyh.purchase.weave.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/09/17 0017 11:25
 */
@Data
@ApiModel(description = "删除待办-请求实体")
@SuppressWarnings("SpellCheckingInspection")
public class RestDelData {

    @ApiModelProperty(value = "系统标识")
    private String syscode;

    @ApiModelProperty(value = "流程实例id")
    private String flowid;

    @ApiModelProperty(value = "接收人")
    private String userid;
}
