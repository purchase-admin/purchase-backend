package cn.luckyh.purchase.workflow.vo.runtime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/03/25 14:21
 */
@Data
@ApiModel(value = "流程定义")
public class ProcessDefinitionRepresentation {

    @ApiModelProperty(value = "流程定义id")
    protected String id;
    @ApiModelProperty(value = "流程定义名称")
    protected String name;
    @ApiModelProperty(value = "流程定义描述")
    protected String description;
    @ApiModelProperty(value = "路程定义KEY")
    protected String key;
    @ApiModelProperty(value = "流程分类")
    protected String category;
    @ApiModelProperty(value = "流程定义版本号")
    protected int version;
    @ApiModelProperty(value = "部署id")
    protected String deploymentId;
    private String resource;
    private String diagramResource;
    protected String tenantId;
    protected boolean hasStartForm;
    private boolean suspended;
    private boolean startFormDefined;
    private boolean graphicalNotationDefined;
}
