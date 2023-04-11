package cn.luckyh.purchase.workflow.vo.model;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/03/18 17:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelDto {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "模型Key")
    @NotEmpty(message = "模型key不能为空")
    private String key;

    @ApiModelProperty(value = "模型名称")
    @NotEmpty(message = "模型名称名称不可为空")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty("模型XML数据")
    private String xmlStr;

    @ApiModelProperty("是否作为新版本")
    private Boolean newVersion;

    @ApiModelProperty("备注")
    private String comment;

}
