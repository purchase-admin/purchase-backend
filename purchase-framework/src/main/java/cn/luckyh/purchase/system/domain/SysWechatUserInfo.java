package cn.luckyh.purchase.system.domain;

import cn.luckyh.purchase.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信用户账号管理对象 sys_wechat_user_info
 *
 * @author purchase
 * @date 2021-04-25
 */
@Data
public class SysWechatUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private String id;

    /**
     * 微信账号
     */
    @Excel(name = "微信账号openId")
    private String wechatLoginName;

    /**
     * 关联的系统账号
     */
    @Excel(name = "关联的系统账号")
    private String relationLoginName;

    /**
     * 审核流程id
     */
    @Excel(name = "审核流程id")
    private String procInstId;

    private String createBy;

    private String updateBy;

    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE)
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}
