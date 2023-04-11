package cn.luckyh.purchase.workflow.vo.runtime;

import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;

import java.io.Serializable;
import java.util.Date;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/04/08 16:05
 */
@Data
public class ProcessInstanceRepresentation implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "流程实例ID")
    protected String id;

    @ApiModelProperty(value = "流程名称")
    protected String name;

    @ApiModelProperty(value = "业务关联")
    protected String businessKey;

    @ApiModelProperty(value = "流程定义ID")
    protected String processDefinitionId;

    @ApiModelProperty(value = "租户ID")
    protected String tenantId;

    @ApiModelProperty(value = "发起日期")
    protected Date started;

    @ApiModelProperty(value = "结束日期")
    protected Date ended;

    @ApiModelProperty(value = "发起人")
    protected SysUser startedBy;

    @ApiModelProperty(value = "流程定义名称")
    protected String processDefinitionName;

    @ApiModelProperty(value = "流程定义KEY")
    protected String processDefinitionKey;

    protected String processDefinitionDescription;
    protected String processDefinitionCategory;
    protected int processDefinitionVersion;
    protected String processDefinitionDeploymentId;

    public ProcessInstanceRepresentation(HistoricProcessInstance processInstance, ProcessDefinition processDefinition, SysUser startedBy) {
        this(processInstance, startedBy);
        mapProcessDefinition(processDefinition);
    }

    public ProcessInstanceRepresentation(HistoricProcessInstance processInstance, SysUser startedBy) {
        this.id = processInstance.getId();
        this.name = processInstance.getName();
        this.businessKey = processInstance.getBusinessKey();
        this.processDefinitionId = processInstance.getProcessDefinitionId();
        this.tenantId = processInstance.getTenantId();
        this.started = processInstance.getStartTime();
        this.ended = processInstance.getEndTime();
        if (startedBy != null) {
            this.startedBy = startedBy;
        }
//        else if (processInstance.getStartUserId() != null) {
//            this.startedBy = new SysUser(Long.parseLong(processInstance.getStartUserId()));
//        }
    }

    protected void mapProcessDefinition(ProcessDefinition processDefinition) {
        if (processDefinition != null) {
            this.processDefinitionName = processDefinition.getName();
            this.processDefinitionDescription = processDefinition.getDescription();
            this.processDefinitionKey = processDefinition.getKey();
            this.processDefinitionCategory = processDefinition.getCategory();
            this.processDefinitionVersion = processDefinition.getVersion();
            this.processDefinitionDeploymentId = processDefinition.getDeploymentId();
        }
    }
}
