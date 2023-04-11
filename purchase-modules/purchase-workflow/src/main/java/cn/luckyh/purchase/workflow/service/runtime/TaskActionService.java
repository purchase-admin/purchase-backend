package cn.luckyh.purchase.workflow.service.runtime;

import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.workflow.exception.NotFoundException;
import cn.luckyh.purchase.workflow.vo.runtime.TaskCompleteRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * service for resolve tasks.
 *
 * @author heng.wang
 * @since 2021/04/19 15:41
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class TaskActionService {

    private final TaskService taskService;
    private final RepositoryService repositoryService;

    @Autowired
    protected RuntimeService runtimeService;

    public TaskActionService(TaskService taskService, RepositoryService repositoryService) {
        this.taskService = taskService;
        this.repositoryService = repositoryService;
    }

    @SneakyThrows
    public void submit(String taskId, TaskCompleteRepresentation completeRepresentation) {
        //Todo: extract to method?    --Add By heng.wang 2021/04/19 14:22
        checkTaskStatus(taskId);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            //            throw new NotFoundException("Task with id: " + taskId + " does not exist");
            throw new NotFoundException("当前任务已存在或已办理过 ");
        }
        if (completeRepresentation == null) {
            throw new RuntimeException("No request found");
        }
        //Todo: check if next node is exclusive gateway    --Add By heng.wang 2021/04/19 14:05

        //        if (checkExclusive(task)) {
        //            Integer opinion = completeRepresentation.getOpinion();
        //        }
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap map = objectMapper.convertValue(completeRepresentation, HashMap.class);
        taskService.setVariableLocal(taskId, "opinion", completeRepresentation.getOpinion());
        String username = SecurityUtils.getUsername();
        Authentication.setAuthenticatedUserId(username);
        taskService.addComment(taskId, task.getProcessInstanceId(), completeRepresentation.getComment());
        taskService.complete(taskId, map);
    }

    @SuppressWarnings("unused")
    private boolean checkExclusive(Task task) {
        String taskDefinitionId = task.getTaskDefinitionKey();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement flowElement = bpmnModel.getFlowElement(taskDefinitionId);
        if (flowElement instanceof FlowNode) {
            List<SequenceFlow> outgoingFlows = ((FlowNode) flowElement).getOutgoingFlows();
            for (SequenceFlow sequenceFlow : outgoingFlows) {
                String targetRef = sequenceFlow.getTargetRef();
                FlowElement flowElement1 = bpmnModel.getFlowElement(targetRef);
                if (flowElement1 != null) {
                    if (flowElement1 instanceof ExclusiveGateway) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void checkTaskStatus(String taskId) {

    }

    public void submitByProcessInstanceId(String procId, TaskCompleteRepresentation representation) {
        List<Task> list = taskService.createTaskQuery().processInstanceId(procId).list();
        if (list.isEmpty()) {
            throw new NotFoundException("任务未找到");
        }
        Task task = list.get(0);
        taskService.setAssignee(task.getId(), representation.getAssignee());
        submit(task.getId(), representation);
    }

    public void delTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new NotFoundException("当前任务不存在或已结束 ");
        }
        runtimeService.deleteProcessInstance(task.getProcessInstanceId(),"");
    }
}
