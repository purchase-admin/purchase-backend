package cn.luckyh.purchase.workflow.service.runtime;

import cn.luckyh.purchase.common.constant.HttpStatus;
import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.system.service.ISysUserService;
import cn.luckyh.purchase.workflow.vo.query.ProcessInstanceQuery;
import cn.luckyh.purchase.workflow.vo.runtime.ProcessInstanceRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/03/29 11:40
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ProcessInstanceQueryService {

    protected final HistoryService historyService;
    protected final ProcessEngineConfiguration processEngineConfiguration;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RepositoryService repositoryService;
    private final ISysUserService userService;

    public ProcessInstanceQueryService(ProcessEngineConfiguration processEngineConfiguration, HistoryService historyService, ISysUserService userService) {
        this.processEngineConfiguration = processEngineConfiguration;
        this.taskService = processEngineConfiguration.getTaskService();
        this.runtimeService = processEngineConfiguration.getRuntimeService();
        this.repositoryService = processEngineConfiguration.getRepositoryService();
        this.historyService = historyService;
        this.userService = userService;
    }

    public TableDataInfo getProcessInstances(ProcessInstanceQuery query) {
        HistoricProcessInstanceQuery instanceQuery = historyService.createHistoricProcessInstanceQuery();
        if (org.springframework.util.StringUtils.hasText(query.getStarterBy())) {
            instanceQuery.startedBy(query.getStarterBy());
        }
        if (StringUtils.hasText(query.getProcessInstanceName())) {
            instanceQuery.processInstanceNameLike("%" + query.getProcessInstanceName() + "%");
        }

        if (StringUtils.hasText(query.getProcessDefinitionKey())) {
            instanceQuery.processDefinitionKey(query.getProcessDefinitionKey());
        }
        if (query.getProcessDefinitionVersion() != null) {
            instanceQuery.processDefinitionVersion(query.getProcessDefinitionVersion());
        }
        if (query.getFinished() != null) {
            if (query.getFinished()) {
                instanceQuery.finished();
                instanceQuery.orderByProcessInstanceEndTime().desc();
            } else {
                instanceQuery.unfinished();
                instanceQuery.orderByProcessInstanceStartTime().desc();
            }
        }

        List<HistoricProcessInstance> instances = instanceQuery.listPage((query.getPageNum() - 1) * query.getPageSize(), query.getPageSize());
        List<ProcessInstanceRepresentation> processInstanceRepresentations = convertInstanceList(instances);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(processInstanceRepresentations);
        rspData.setTotal(instanceQuery.count());
        return rspData;

    }

    protected List<ProcessInstanceRepresentation> convertInstanceList(List<HistoricProcessInstance> instances) {
        List<ProcessInstanceRepresentation> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(instances)) {
            //缓存
            HashMap<String, SysUser> userCache = new HashMap<>();
            for (HistoricProcessInstance processInstance : instances) {
                SysUser userRep = null;
                if (processInstance.getStartUserId() != null) {
                    SysUser obj = userCache.get(processInstance.getStartUserId());
                    if (Objects.nonNull(obj)) {
                        userRep = obj;
                    } else {
                        SysUser user = userService.selectUserByUserName(processInstance.getStartUserId());
                        if (Objects.nonNull(user)) {
                            userRep = user;
                            userCache.put(processInstance.getStartUserId(), user);
                        }
                    }
                }
                ProcessDefinitionEntity procDef = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
                ProcessInstanceRepresentation instanceRepresentation = new ProcessInstanceRepresentation(processInstance, procDef, userRep);
                result.add(instanceRepresentation);
            }

        }
        return result;
    }
}
