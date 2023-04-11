package cn.luckyh.purchase.common.core.page;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author heng.wang
 */
@Data
@Accessors(chain = true)
public class PageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页号", position = 100)
    @ApiParam(defaultValue = "1")
    private int pageNumber = 1;

    @ApiModelProperty(value = "页面大小", position = 101)
    @ApiParam(defaultValue = "10")
    private int pageSize = 10;

    @ApiModelProperty(value = "排序字段", position = 102)
    private String sort;

    @ApiModelProperty(value = "排序方式 asc/desc", allowableValues = "asc,desc", position = 103)
    @ApiParam(defaultValue = "asc")
    private String order;

}
