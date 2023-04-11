package cn.luckyh.purchase.common.core.domain.model;

import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * simple user model .
 *
 * @author heng.wang
 * @since 2021/04/19 09:35
 */
@Data
@ApiModel(description = "simple user model")
public class UserRepresentation {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "登录名", position = 1)
    private String userName;

    @ApiModelProperty(value = "用户昵称", position = 2)
    private String nickName;

    @ApiModelProperty(value = "性别", position = 3)
    private String sex;

    @ApiModelProperty(value = "父部门ID", position = 4)
    private Long parentId;

    @ApiModelProperty(value = "父部门名称", position = 5)
    private String parentName;

    @ApiModelProperty(value = "部门ID", position = 6)
    private Long deptId;

    @ApiModelProperty(value = "部门名称", position = 7)
    private String deptName;

    public UserRepresentation(SysUser user) {
        this.userId = user.getUserId();
        this.deptId = user.getDeptId();
        this.userName = user.getUserName();
        this.nickName = user.getNickName();
        this.deptName = user.getDept().getDeptName();
        this.parentId = user.getDept().getParentId();
        this.parentName = user.getDept().getParentName();
    }

    public UserRepresentation(String userName) {
        setUserName(userName);
        setNickName(userName);
    }
}
