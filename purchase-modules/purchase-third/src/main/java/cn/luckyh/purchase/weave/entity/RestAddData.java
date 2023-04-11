package cn.luckyh.purchase.weave.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/08/17 0017 8:47
 */
@Data
@ApiModel(description = "推送待办-请求实体")
@SuppressWarnings("SpellCheckingInspection")
public class RestAddData {
    @ApiModelProperty(value = "系统标识")
    private String syscode;

    @ApiModelProperty(value = "流程实例Id")
    private String flowid;

    @ApiModelProperty(value = "标题")
    private String requestname;

    @ApiModelProperty(value = "流程类型名称")
    private String workflowname;

    @ApiModelProperty(value = "步骤名称(节点名称)")
    private String nodename;

    @ApiModelProperty(value = "PC地址")
    private String pcurl;

    @ApiModelProperty(value = "app地址")
    private String appurl;

    @ApiModelProperty(value = "流程处理状态", allowableValues = "0:待办,2:已办,4:办结,8:抄送(待阅)")
    private Integer isremark;

    @ApiModelProperty(value = "流程查看状态", allowableValues = "0:未读,1:已读")
    private Integer viewtype;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建日期时间")
    private Date createdatetime;

    @ApiModelProperty(value = "接收人")
    private String receiver;

    @ApiModelProperty(value = "接收日期时间")
    private Date receivedatetime;

    @ApiModelProperty(value = "时间戳")
    private final long receivets = System.currentTimeMillis();

}
