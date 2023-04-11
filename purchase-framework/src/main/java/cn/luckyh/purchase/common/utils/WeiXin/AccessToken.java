package cn.luckyh.purchase.common.utils.WeiXin;


import lombok.Data;

/**
 * @author HP
 */
@Data
public class AccessToken {

    /**
     * 获取到的凭证
     */
    private String token;

    /**
     * 凭证有效时间，单位：秒
     */
    private int expiresIn;

    private String openId;

    private String sessionKey;

    private String errorCode;
}
