package cn.luckyh.purchase.workflow.service.history;

import cn.luckyh.purchase.common.core.domain.entity.SysRole;
import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import cn.luckyh.purchase.common.core.domain.model.UserRepresentation;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.system.service.ISysUserService;
import cn.luckyh.purchase.workflow.service.BaseQueryService;
import cn.luckyh.purchase.workflow.vo.history.HistoryRecord;
import cn.luckyh.purchase.workflow.vo.history.HistoryRecordResult;
import cn.luckyh.purchase.workflow.vo.query.TaskInstanceQuery;
import cn.luckyh.purchase.workflow.vo.runtime.CommentRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.task.Comment;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/04/06 11:07
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class HistoryQueryService extends BaseQueryService {

    private final HistoryService historyService;

    private final RepositoryService repositoryService;
    private final TaskService taskService;


    public HistoryQueryService(HistoryService historyService, RepositoryService repositoryService, TaskService taskService,
                               ISysUserService userService) {
        super(userService);
        this.historyService = historyService;
        this.repositoryService = repositoryService;
        this.taskService = taskService;
    }

    public List<HistoryRecord> historyRecordQuery(String processInstanceId) {
        Set<String> strings = new HashSet<>();
        //        strings.add(BpmnXMLConstants.ELEMENT_EVENT_START);
        strings.add(BpmnXMLConstants.ELEMENT_TASK_USER);
        //        strings.add(BpmnXMLConstants.ELEMENT_EVENT_END);
        List<HistoricActivityInstance> historicActivityInstances = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .activityTypes(strings)
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();

        List<HistoryRecord> records = new ArrayList<>();

        historicActivityInstances.forEach(historicActivityInstance -> {
            HistoryRecord historyRecord = new HistoryRecord(historicActivityInstance);
            if (StringUtils.hasText(historicActivityInstance.getAssignee())) {
                UserRepresentation assignee = getAssigneeRepresentation(historicActivityInstance.getAssignee());
                historyRecord.setAssignee(assignee);
            } else {
                historyRecord.setAssignee(new UserRepresentation(historicActivityInstance.getAssignee()));
            }

            List<CommentRepresentation> commentList = getCommentRepresentationList(historicActivityInstance);
            Integer opinionInteger = getTaskOpinion(historicActivityInstance);
            historyRecord.setOpinion(opinionInteger);
            historyRecord.setComments(commentList);
            records.add(historyRecord);
        });

        return records;
    }

    private Integer getTaskOpinion(HistoricActivityInstance historicActivityInstance) {
        Integer taskOpinion = null;
        if (BpmnXMLConstants.ELEMENT_TASK_USER.equals(historicActivityInstance.getActivityType())) {
            HistoricVariableInstance opinion = historyService.createHistoricVariableInstanceQuery().taskId(historicActivityInstance.getTaskId())
                    .variableName("opinion").singleResult();
            if (Objects.nonNull(opinion)) {
                taskOpinion = (Integer) opinion.getValue();
            }
        }
        return taskOpinion;
    }


    private List<CommentRepresentation> getCommentRepresentationList(HistoricActivityInstance historicActivityInstance) {
        List<Comment> taskComments = taskService.getTaskComments(historicActivityInstance.getTaskId());
        // Create representation for all comments
        return taskComments.stream().map(CommentRepresentation::new).collect(Collectors.toList());
    }

    public HistoryRecordResult historyProcessRecordQuery(String processInstanceId) {
        //get history process instance
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();
        if(historicProcessInstance ==null){
            throw new RuntimeException("历史流程未找到");
        }
        HistoryRecordResult result = new HistoryRecordResult(historicProcessInstance, getAssigneeRepresentation(historicProcessInstance.getStartUserId()));
        List<HistoryRecord> records = historyRecordQuery(processInstanceId);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        List<FlowElement> activities = getActivities(bpmnModel);
        result.setActivities(activities);
        getFirstActivity(result, records, bpmnModel);
        return result;
    }

    /**
     * get first activity after start event.
     *
     * @param result
     * @param records
     * @param bpmnModel
     */
    private void getFirstActivity(HistoryRecordResult result, List<HistoryRecord> records, BpmnModel bpmnModel) {
        List<StartEvent> startEventList = bpmnModel.getMainProcess().findFlowElementsOfType(StartEvent.class);
        if (!records.isEmpty()) {
            result.setRecords(records);
            if (Objects.nonNull(result.getEndTime())) {
                //流程已经结束
                result.setIsFirst(0);
            } else {
                List<String> firstElements = startEventList
                        .stream()
                        .flatMap(
                                startEvent -> startEvent.getOutgoingFlows().stream()).map(SequenceFlow::getTargetRef)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                if (firstElements.contains(records.get(records.size() - 1).getActivityId())) {
                    result.setIsFirst(1);
                } else {
                    result.setIsFirst(0);
                }
            }

        }
    }

    private List<FlowElement> getActivities(BpmnModel bpmnModel) {
        List<FlowElement> activities = new ArrayList<>();
        Process mainProcess = bpmnModel.getMainProcess();
        List<UserTask> userTaskList = mainProcess.findFlowElementsOfType(UserTask.class);
        List<StartEvent> startEventList = mainProcess.findFlowElementsOfType(StartEvent.class);
        List<EndEvent> endEventList = mainProcess.findFlowElementsOfType(EndEvent.class);
        activities.addAll(userTaskList);
        activities.addAll(startEventList);
        activities.addAll(endEventList);
        return activities;
    }

    public HistoryRecordResult historyTaskRecordQuery(String taskId) {
        String processInstanceId = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult().getProcessInstanceId();
        //get history process instance
        return this.historyProcessRecordQuery(processInstanceId);
    }

    public TableDataInfo listHistoryRecord(TaskInstanceQuery query) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String userId = user.getUserName();
        List<String> roleIds = user.getRoles()
                .stream().map(SysRole::getRoleId).collect(Collectors.toList())
                .stream().map(id -> id + "")
                .collect(Collectors.toList());
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService
                .createHistoricProcessInstanceQuery();
        if (StringUtils.hasText(query.getProcessInstanceName())) {
            historicProcessInstanceQuery.processInstanceNameLike("%" + query.getProcessInstanceName() + "%");
        }
        if (StringUtils.hasText(query.getProcessDefinitionKey())) {
            historicProcessInstanceQuery.processDefinitionKey(query.getProcessDefinitionKey());
        }
        List<HistoricProcessInstance> historicProcessInstanceList = historicProcessInstanceQuery
                .or()
                .involvedUser(userId)
                .involvedGroups(new HashSet<>(roleIds))
                .endOr()
                .orderByProcessInstanceStartTime().desc()
                .listPage((query.getPageNum() - 1) * query.getPageSize(), query.getPageSize());
        List<HistoryRecordResult> resultList = historicProcessInstanceList.stream().map(historicProcessInstance -> new HistoryRecordResult(historicProcessInstance, getAssigneeRepresentation(historicProcessInstance.getStartUserId()))).collect(Collectors.toList());
        return TableDataInfo.success(resultList, historicProcessInstanceQuery.count(), HttpStatus.OK);
    }
}
