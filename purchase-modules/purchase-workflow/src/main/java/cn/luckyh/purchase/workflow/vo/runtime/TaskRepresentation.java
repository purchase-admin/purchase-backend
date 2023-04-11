/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.luckyh.purchase.workflow.vo.runtime;

import cn.luckyh.purchase.common.core.domain.model.LoginUser;
import cn.luckyh.purchase.common.core.domain.model.UserRepresentation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.Date;
import java.util.List;

/**
 * REST representation of a task.
 *
 * @author Tijs Rademakers
 */
@Data
public class TaskRepresentation {

    @ApiModelProperty(value = "任务id")
    protected String id;

    @ApiModelProperty(value = "任务名称")
    protected String name;
    @ApiModelProperty(value = "描述")
    protected String description;
    protected String category;
    @ApiModelProperty(value = "办理人")
    protected UserRepresentation assignee;
    @ApiModelProperty(value = "创建日期")
    protected Date created;
    protected Date dueDate;
    protected Date endDate;
    protected Long duration;
    protected Integer priority;
    protected String processInstanceId;
    protected String processInstanceName;
    protected Date endTime;
    protected Date startTime;

    protected String businessKey;

    protected String processDefinitionId;
    protected String processDefinitionName;
    protected String processDefinitionDescription;
    protected String processDefinitionKey;
    protected String processDefinitionCategory;
    protected int processDefinitionVersion;
    protected String processDefinitionDeploymentId;

    protected String scopeId;
    protected String scopeType;
    protected String caseInstanceName;

//    protected String scopeDefinitionId;
//    protected String caseDefinitionName;
//    protected String caseDefinitionDescription;
//    protected String caseDefinitionKey;
//    protected String caseDefinitionCategory;
//    protected int caseDefinitionVersion;
//    protected String caseDefinitionDeploymentId;

    protected String parentTaskId;
    protected String parentTaskName;

    protected String formKey;
    protected String processInstanceStartUserId;
    @ApiModelProperty(value = "流程发起人")
    protected UserRepresentation processInstanceStartUser;
    protected boolean initiatorCanCompleteTask;
    protected boolean isMemberOfCandidateGroup;
    protected boolean isMemberOfCandidateUsers;

    @JsonDeserialize(contentAs = LoginUser.class)
    @JsonInclude(Include.NON_NULL)
    protected List<LoginUser> involvedPeople;

    // Needed for serialization!
    public TaskRepresentation() {
    }

    public TaskRepresentation(Task task) {
        initializeTaskDetails(task);
    }

    public TaskRepresentation(HistoricTaskInstance task) {
        initializeTaskDetails(task);
    }

    public TaskRepresentation(TaskInfo taskInfo, ProcessDefinition processDefinition) {
        initializeTaskDetails(taskInfo);
        if (processDefinition != null) {
            this.processDefinitionName = processDefinition.getName();
            this.processDefinitionDescription = processDefinition.getDescription();
            this.processDefinitionKey = processDefinition.getKey();
            this.processDefinitionCategory = processDefinition.getCategory();
            this.processDefinitionVersion = processDefinition.getVersion();
            this.processDefinitionDeploymentId = processDefinition.getDeploymentId();
        }
    }

    public TaskRepresentation(TaskQueryResult taskInfo, ProcessDefinition processDefinition) {
        initializeTaskDetails(taskInfo);
        if (processDefinition != null) {
            this.processDefinitionName = processDefinition.getName();
            this.processDefinitionDescription = processDefinition.getDescription();
            this.processDefinitionKey = processDefinition.getKey();
            this.processDefinitionCategory = processDefinition.getCategory();
            this.processDefinitionVersion = processDefinition.getVersion();
            this.processDefinitionDeploymentId = processDefinition.getDeploymentId();
        }
    }

//    public TaskRepresentation(TaskInfo taskInfo, CaseDefinition caseDefinition) {
//        initializeTaskDetails(taskInfo);
//
//        if (caseDefinition != null) {
//            this.caseDefinitionName = caseDefinition.getName();
//            this.caseDefinitionDescription = caseDefinition.getDescription();
//            this.caseDefinitionKey = caseDefinition.getKey();
//            this.caseDefinitionCategory = caseDefinition.getCategory();
//            this.caseDefinitionVersion = caseDefinition.getVersion();
//            this.caseDefinitionDeploymentId = caseDefinition.getDeploymentId();
//        }
//    }

    public TaskRepresentation(TaskInfo taskInfo, TaskInfo parentTaskInfo) {
        initializeTaskDetails(taskInfo);

        if (parentTaskInfo != null) {
            this.parentTaskId = parentTaskInfo.getId();
            this.parentTaskName = parentTaskInfo.getName();
        }
    }

    public TaskRepresentation(TaskInfo taskInfo, ProcessDefinition processDefinition, String processInstanceName,String businessKey) {
        this(taskInfo, processDefinition);
        this.processInstanceName = processInstanceName;
        this.businessKey = businessKey;
    }
//
//    public TaskRepresentation(TaskInfo taskInfo, CaseDefinition caseDefinition, String caseInstanceName) {
//        this(taskInfo, caseDefinition);
//        this.caseInstanceName = caseInstanceName;
//    }

    public void initializeTaskDetails(TaskInfo taskInfo) {
        this.id = taskInfo.getId();
        this.name = taskInfo.getName();
        this.description = taskInfo.getDescription();
        this.category = taskInfo.getCategory();
        this.created = taskInfo.getCreateTime();
        this.dueDate = taskInfo.getDueDate();
        this.priority = taskInfo.getPriority();
        this.processInstanceId = taskInfo.getProcessInstanceId();
        this.processDefinitionId = taskInfo.getProcessDefinitionId();
        this.scopeId = taskInfo.getScopeId();
        this.scopeType = taskInfo.getScopeType();
//        this.scopeDefinitionId = taskInfo.getScopeDefinitionId();

        if (taskInfo instanceof HistoricTaskInstance) {
            this.endDate = ((HistoricTaskInstance) taskInfo).getEndTime();
            this.formKey = taskInfo.getFormKey();
            this.duration = ((HistoricTaskInstance) taskInfo).getDurationInMillis();
        } else {
            // Rendering of forms for historic tasks not supported currently
            this.formKey = taskInfo.getFormKey();
        }
    }

    public void initializeTaskDetails(TaskQueryResult taskInfo) {
        this.initializeTaskDetails((TaskInfo) taskInfo);
        this.businessKey = taskInfo.getBusinessKey();
        this.processInstanceStartUserId = taskInfo.getProcessInstanceStartUserId();
        this.processInstanceName = taskInfo.getProcessInstanceName();
        this.endTime = taskInfo.getEndTime();
        this.startTime = taskInfo.getStartTime();
    }

    public void fillTask(Task task) {
        task.setName(name);
        task.setDescription(description);
        if (assignee != null) {
            task.setAssignee(assignee.getUserName());
        }
        task.setDueDate(dueDate);
        if (priority != null) {
            task.setPriority(priority);
        }
        task.setCategory(category);
    }

}
