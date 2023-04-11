package cn.luckyh.purchase.weave.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/09/17 0017 11:48
 */

@Data
@ApiModel(description = "响应实体")
@SuppressWarnings("SpellCheckingInspection")
public class RestResponse {

    @ApiModelProperty(value = "异构系统标识")
    private String syscode;

    @ApiModelProperty(value = "数据类型")
    private String dateType;

    @ApiModelProperty(value = "操作类型")
    private String operType;

    @ApiModelProperty(value = "操作结果")
    private String operResult;

    @ApiModelProperty(value = "错误信息")
    private String message;
}
