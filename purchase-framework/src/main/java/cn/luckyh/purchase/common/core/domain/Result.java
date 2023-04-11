package cn.luckyh.purchase.common.core.domain;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @describe:
 * @author:Fanjj
 * @Date:2019/8/28
 * @Time:9:55
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "响应状态回执码")
    private Integer code = 200;

    @ApiModelProperty(value = "成功标志")
    private boolean success;

    @ApiModelProperty(value = "响应消息")
    private String msg = "success";

    @ApiModelProperty(value = "响应数据")
    private T data;

    @ApiModelProperty(value = "响应时间戳")
    private final long timestamp = System.currentTimeMillis();

}
