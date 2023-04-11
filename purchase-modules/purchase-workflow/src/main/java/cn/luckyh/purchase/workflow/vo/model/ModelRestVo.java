package cn.luckyh.purchase.workflow.vo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/03/24 16:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelRestVo {

    private String id;
    private String name;
    private String key;
    private String description;
    private String comment;
    private Integer version;
    private Integer latestVersion;
    private String latestDefinitionId;
    private Date createTime;
    private Date lastUpdateTime;
    private String metaInfo;
    private String deploymentId;
    private String tenantId;
    private String modelResourceId;
}
