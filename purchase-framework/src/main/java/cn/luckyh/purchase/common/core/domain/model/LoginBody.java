package cn.luckyh.purchase.common.core.domain.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户登录对象
 */
public class LoginBody {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "登录名",example = "admin",position = 1)
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "密码",example = "admin123",position = 2)
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
