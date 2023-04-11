package cn.luckyh.purchase.workflow.service.runtime;

import cn.luckyh.purchase.common.core.domain.entity.SysRole;
import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import cn.luckyh.purchase.common.core.domain.model.UserRepresentation;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.system.service.ISysUserService;
import cn.luckyh.purchase.workflow.mapper.TasksMapper;
import cn.luckyh.purchase.workflow.service.BaseQueryService;
import cn.luckyh.purchase.workflow.service.history.HistoryQueryService;
import cn.luckyh.purchase.workflow.vo.query.TaskInstanceQuery;
import cn.luckyh.purchase.workflow.vo.runtime.TaskQueryResult;
import cn.luckyh.purchase.workflow.vo.runtime.TaskRepresentation;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskInfoQueryWrapper;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
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
 * @since 2021/04/20 17:07
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class TasksService extends BaseQueryService {

    private final TaskService taskService;
    private final RepositoryService repositoryService;

    private final HistoryService historyService;

    protected final RuntimeService runtimeService;

    protected final HistoryQueryService historyQueryService;
    protected final TasksMapper tasksMapper;

    public TasksService(TaskService taskService, RepositoryService repositoryService, ISysUserService userService, HistoryService historyService, RuntimeService runtimeService, HistoryQueryService historyQueryService, TasksMapper tasksMapper) {
        super(userService);
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.historyService = historyService;
        this.runtimeService = runtimeService;
        this.historyQueryService = historyQueryService;
        this.tasksMapper = tasksMapper;
    }


    public TableDataInfo queryTasks(TaskInstanceQuery query) {
        TaskInfoQueryWrapper taskInfoQueryWrapper;

        //区分待办还是已办
        if ("completed".equals(query.getState())) {
            HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
            historicTaskInstanceQuery.finished();
            //开启节点过滤
            historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime().desc();
            taskInfoQueryWrapper = new TaskInfoQueryWrapper(historicTaskInstanceQuery);
        } else {
            TaskQuery taskQuery = taskService.createTaskQuery();
            taskInfoQueryWrapper = new TaskInfoQueryWrapper(taskQuery);

        }

        //标题过滤
        if (StringUtils.hasText(query.getProcessInstanceName())) {
            List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().processInstanceNameLike("%" + query.getProcessInstanceName() + "%").list();
            List<String> historicProcessInstanceIdList = historicProcessInstanceList.stream().map(HistoricProcessInstance::getId).collect(Collectors.toList());
            if (historicProcessInstanceList.isEmpty()) {
                return TableDataInfo.success(historicProcessInstanceList, 0, HttpStatus.OK);
            }
            taskInfoQueryWrapper.getTaskInfoQuery().processInstanceIdIn(historicProcessInstanceIdList);
        }

//        流程KEY过滤
        if (StringUtils.hasText(query.getProcessDefinitionKey())) {
            taskInfoQueryWrapper.getTaskInfoQuery().processDefinitionKey(query.getProcessDefinitionKey());
        }


        //人员过滤包含用户ID 角色IDS
        if (Objects.isNull(query.getUser())) {
            query.setUser(SecurityUtils.getLoginUser().getUser());
        }
        SysUser user = query.getUser();
        String userId = user.getUserName();
        List<String> roleIds = user.getRoles()
                .stream().map(SysRole::getRoleId).collect(Collectors.toList())
                .stream().map(id -> id + "")
                .collect(Collectors.toList());
        taskInfoQueryWrapper.getTaskInfoQuery().or()
                .taskAssignee(userId)
                .taskCandidateUser(userId)
                .taskCandidateGroupIn(roleIds).endOr();
        taskInfoQueryWrapper.getTaskInfoQuery().orderByTaskCreateTime().desc();

        //最终TaskList
        List<TaskInfo> taskList = new ArrayList<>();

        //查询的TaskList
        List<? extends TaskInfo> queryTaskList = taskInfoQueryWrapper.getTaskInfoQuery()
                .listPage((query.getPageNum() - 1) * query.getPageSize(), query.getPageSize());

        //为空直接返回
        if (queryTaskList.isEmpty()) {
            return TableDataInfo.success(queryTaskList, 0, HttpStatus.OK);
        }

        //开启已办节点过滤,否则查出所有的节点
        if ("completed".equals(query.getState())) {
            Map<String, List<TaskInfo>> collect = queryTaskList.stream().collect(Collectors.groupingBy(TaskInfo::getProcessInstanceId));
            for (Map.Entry<String, List<TaskInfo>> entry : collect.entrySet()) {
                List<TaskInfo> taskInfos = entry.getValue();
                taskInfos.sort(Comparator.nullsFirst(Comparator.comparing(TaskInfo::getCreateTime)).reversed());
                TaskInfo taskInfo = taskInfos.get(0);
                taskList.add(taskInfo);
            }
        } else {
            taskList = new ArrayList<>(queryTaskList);
        }

        //处理流程业务Key和流程名称
        //查询
        Map<String, String> processInstancesNames = new HashMap<>();
        Map<String, String> processInstancesBusinessKeys = new HashMap<>();
        handleIncludeProcessInstance(taskInfoQueryWrapper, taskList, processInstancesNames, processInstancesBusinessKeys);

        //转化
        List<TaskRepresentation> taskRepresentationList = convertTaskInfoList(taskList, processInstancesNames, processInstancesBusinessKeys);

        //返回值
        return TableDataInfo.success(taskRepresentationList, taskInfoQueryWrapper.getTaskInfoQuery().count(), HttpStatus.OK);
    }

    public TableDataInfo listTasks(TaskInstanceQuery query) {
        //区分待办还是已办
        if ("completed".equals(query.getState())) {
            return historyQueryService.listHistoryRecord(query);
        } else {
            PageHelper.startPage(query.getPageNum(), query.getPageSize());
            List<TaskQueryResult> taskQueryResults = tasksMapper.listTasks(SecurityUtils.getUsername(), query.getProcessDefinitionKey(), query.getProcessInstanceName());
            long total = new PageInfo<>(taskQueryResults).getTotal();
            List<TaskRepresentation> representationList = convertTaskInfoList(taskQueryResults);
            return TableDataInfo.success(representationList, total, HttpStatus.OK);
        }

    }

    private List<TaskRepresentation> convertTaskInfoList(List<TaskQueryResult> taskQueryResults) {
        List<TaskRepresentation> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(taskQueryResults)) {
            for (TaskQueryResult task : taskQueryResults) {
                TaskRepresentation taskRepresentation;
                ProcessDefinitionEntity processDefinition = null;

                if (task.getProcessDefinitionId() != null) {
                    processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
                }
                taskRepresentation = new TaskRepresentation(task, processDefinition);
                if (StringUtils.hasText(task.getAssignee())) {
                    UserRepresentation assignee = getAssigneeRepresentation(task.getAssignee());
                    taskRepresentation.setAssignee(assignee);
                }
                if (StringUtils.hasText(task.getProcessInstanceStartUserId())) {
                    UserRepresentation starter = getAssigneeRepresentation(task.getProcessInstanceStartUserId());
                    taskRepresentation.setProcessInstanceStartUser(starter);
                }
                result.add(taskRepresentation);
            }
        }
        return result;
    }

    protected void handleIncludeProcessInstance(TaskInfoQueryWrapper taskInfoQueryWrapper, List<? extends TaskInfo> tasks,
                                                Map<String, String> processInstanceNames, Map<String, String> processInstancesBusinessKeys) {
        if (CollectionUtils.isNotEmpty(tasks)) {
            //过滤 做个伪缓存
            Set<String> processInstanceIds = new HashSet<>();
            for (TaskInfo task : tasks) {
                if (task.getProcessInstanceId() != null) {
                    processInstanceIds.add(task.getProcessInstanceId());
                }
            }
            if (CollectionUtils.isNotEmpty(processInstanceIds)) {

                if (taskInfoQueryWrapper.getTaskInfoQuery() instanceof HistoricTaskInstanceQuery) {
                    List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery().processInstanceIds(processInstanceIds)
                            .list();
                    processInstances.forEach(processInstance -> {
                        processInstanceNames.put(processInstance.getId(), processInstance.getName());
                        processInstancesBusinessKeys.put(processInstance.getId(), processInstance.getBusinessKey());
                    });
                } else {
                    List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIds).list();
                    processInstances.forEach(processInstance -> {
                        processInstanceNames.put(processInstance.getId(), processInstance.getName());
                        processInstancesBusinessKeys.put(processInstance.getId(), processInstance.getBusinessKey());
                    });
                }
            }
        }
    }

    protected List<TaskRepresentation> convertTaskInfoList(List<? extends TaskInfo> tasks,
                                                           Map<String, String> processInstanceNames, Map<String, String> processInstancesBusinessKeys) {
        List<TaskRepresentation> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tasks)) {
            for (TaskInfo task : tasks) {
                TaskRepresentation taskRepresentation;
                ProcessDefinitionEntity processDefinition = null;

                if (task.getProcessDefinitionId() != null) {
                    processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
                }
                taskRepresentation = new TaskRepresentation(task, processDefinition, processInstanceNames.get(task.getProcessInstanceId()),
                        processInstancesBusinessKeys.get(task.getProcessInstanceId()));

                if (StringUtils.hasText(task.getAssignee())) {
                    UserRepresentation assignee = getAssigneeRepresentation(task.getAssignee());
                    taskRepresentation.setAssignee(assignee);
                }
                result.add(taskRepresentation);
            }
        }
        return result;
    }

}
