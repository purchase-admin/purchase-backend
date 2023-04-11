package cn.luckyh.purchase.task.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 询价采购-确定中标传输DTO.
 *
 * @author heng.wang
 * @since 2021/10/12 0012 16:57
 */
@Data
public class PurchaseTaskDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "供应商ID")
    @NotEmpty(message = "供应商不能为空")
    private String supplierId;

    @ApiModelProperty(value = "定标结果说明")
    private String sureContent;

    @ApiModelProperty(value = "采购任务ID")
    @NotEmpty(message = "采购任务不能为空")
    private String taskId;

    @ApiModelProperty(value = "定标结果_文件名称")
    private String file1;

    @ApiModelProperty(value = "定标报告_文件名称")
    private String file2;

    @ApiModelProperty(value = "推荐表_文件名称")
    private String file3;

    @ApiModelProperty(value = "公示函_文件名称")
    private String file4;

}
