package cn.luckyh.purchase.supplier.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信 rest model.
 *
 * @author heng.wang
 * @since 2021/6/7 0007 15:17
 */
@Data
@Accessors(chain = true)
@ApiModel("微信 rest model")
public class WeChatRepresentation {

    public static final String REGISTER = "register";
    public static final String APPROVE = "approve";
    public static final String WAITING = "waiting";
    public static final String APPROVE_UPDATE = "approveUpdate";

    public static final String SUCCESS = "success";

    @ApiModelProperty(value = "操作状态码", allowableValues = "register,waiting,success")
    private String status;

    @ApiModelProperty("用户openId")
    private String openId;

    @ApiModelProperty("当前用户Token")
    private String token;
}
