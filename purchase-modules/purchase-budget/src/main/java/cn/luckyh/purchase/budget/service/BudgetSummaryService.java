package cn.luckyh.purchase.budget.service;

import cn.luckyh.purchase.budget.domain.BudgetSummary;
import cn.luckyh.purchase.budget.domain.BudgetSummaryBatch;
import cn.luckyh.purchase.budget.mapper.BudgetSummaryMapper;
import cn.luckyh.purchase.budget.vo.BudgetSummaryQuery;
import cn.luckyh.purchase.budget.vo.BudgetSummaryVo;
import cn.luckyh.purchase.common.constant.WorkflowConstants;
import cn.luckyh.purchase.common.exception.NotFoundException;
import cn.luckyh.purchase.workflow.service.runtime.ProcessInstancesService;
import cn.luckyh.purchase.workflow.vo.runtime.CreateProcessInstanceRepresentation;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 预算汇总 Service.
 *
 * @author heng.wang
 * @since 2021/05/08 10:06
 */
@Slf4j
@Service("BudgetSummaryService")
@Transactional(rollbackFor = RuntimeException.class)
public class BudgetSummaryService extends ServiceImpl<BudgetSummaryMapper, BudgetSummary> {

    private final BudgetSummaryBatchService summaryBatchService;

    private final ProcessInstancesService processInstancesService;

    private final BudgetSummaryMapper budgetSummaryMapper;

    private final IBudgetBatchService budgetBatchService;

    private final TaskService taskService;

    public BudgetSummaryService(BudgetSummaryBatchService summaryBatchService,
                                ProcessInstancesService processInstancesService, BudgetSummaryMapper budgetSummaryMapper,
                                IBudgetBatchService budgetBatchService, TaskService taskService) {
        this.summaryBatchService = summaryBatchService;
        this.processInstancesService = processInstancesService;
        this.budgetSummaryMapper = budgetSummaryMapper;
        this.budgetBatchService = budgetBatchService;
        this.taskService = taskService;
    }

    public void saveSummary(BudgetSummary summary) {
        validateData(summary);
        summary.setStatus(WorkflowConstants.DRAFT);
        summary.setTitle(summary.getYear() + "年年度预算汇总");
        saveBatchData(summary);
        save(summary);
    }

    /**
     * 保存关联数据.
     * <p>1.先删除所有数据</>
     * <p>2.保存所有数据</>
     * Todo: extract to method    --Add By heng.wang 2021/05/08 14:15
     *
     * @param summary 预算汇总 model.
     */
    private void saveBatchData(BudgetSummary summary) {
        List<BudgetSummaryBatch> list = new ArrayList<>();
        List<String> split = StrUtil.split(summary.getBudgetBatchIds(), StrUtil.C_COMMA);
        split.forEach(s -> {
            BudgetSummaryBatch summaryBatch = new BudgetSummaryBatch();
            summaryBatch.setSummary_id(summary.getId());
            summaryBatch.setBatch_id(s);
            list.add(summaryBatch);
        });
        summaryBatchService.deleteBySummaryId(summary.getId());
        //save all
        summaryBatchService.saveBatch(list);
        String collect = list.stream().map(BudgetSummaryBatch::getBatch_id).collect(Collectors.joining(","));
        summary.setBudgetBatchIds(collect);
    }

    //Todo: pending    --Add By heng.wang 2021/05/08 14:19
    private void validateData(BudgetSummary summary) {
        if (log.isDebugEnabled()) {
            log.debug("检查年度预算数据有效性");
            log.debug(summary.toString());
        }
        String year = summary.getYear();
        LambdaQueryWrapper<BudgetSummary> wrapper = Wrappers.lambdaQuery(BudgetSummary.class);
        wrapper.ne(BudgetSummary::getId, summary.getId());
        wrapper.eq(BudgetSummary::getYear, year);
        List<BudgetSummary> list = list(wrapper);
        if (!list.isEmpty()) {
            throw new RuntimeException("当前年份汇总审批数据已存在");
        }

    }

    //Todo: check    --Add By heng.wang 2021/05/08 14:19
    public void updateSummary(String id, BudgetSummary summary) {
        BudgetSummary original = getById(id);
        original.setYear(summary.getYear());
        original.setTitle(summary.getYear() + "年年度预算汇总");
        //检验数据
        validateData(summary);
        //保存数据
        saveBatchData(summary);
        original.setBudgetBatchIds(summary.getBudgetBatchIds());
        updateById(original);
    }

    //Todo: complete    --Add By heng.wang 2021/05/08 14:19
    public void deleteSummary(String ids) {
        List<String> list = Arrays.asList(ids.split(StrUtil.COMMA));
        list.forEach(s -> {
            BudgetSummary byId = getById(s);
            //删除关联数据
            summaryBatchService.deleteBySummaryId(byId.getId());
            //删除流程数据
            deleteProcessData(byId.getProcId());
            //删除自己
            removeById(byId);
        });
    }

    private void deleteProcessData(String procId) {
        if (log.isDebugEnabled()) {
            log.debug("删除预算汇总流程数据");
            log.debug(procId);
        }
        if (StringUtils.hasText(procId)) {
            processInstancesService.delete(procId, null);
        }
    }

    public void deleteSummaryBatchData(String id, String ids) {
        BudgetSummary byId = getById(id);
        if (Objects.isNull(byId)) {
            log.warn("数据不存在{}", id);
            return;
        }
        List<String> list = StrUtil.splitTrim(ids, StrUtil.C_COMMA);
        if (ids.isEmpty()) {
            return;
        }
        summaryBatchService.deleteBySummaryIdAndBatchIds(id, list);
        List<BudgetSummaryBatch> budgetSummaryBatches = summaryBatchService.getBySummaryId(id);
        String collect = budgetSummaryBatches.stream().map(BudgetSummaryBatch::getBatch_id).collect(Collectors.joining(","));
        byId.setBudgetBatchIds(collect);
        updateById(byId);
    }

    //Todo: check    --Add By heng.wang 2021/05/08 14:42
    public void updateSummaryStatus(String id, String status) {
        this.updateSummaryStatus(id, status, null);
    }

    public void updateSummaryStatus(String id, String status, String procId) {
        BudgetSummary byId = getById(id);
        if (Objects.isNull(byId)) {
            log.error("年度预算汇总业务数据{}不存在,关联流程实例ID为{}", id, procId);
            throw new NotFoundException("年度预算汇总业务数据不存在");
        }
        LambdaUpdateWrapper<BudgetSummary> wrapper = Wrappers.lambdaUpdate(BudgetSummary.class);
        wrapper.eq(BudgetSummary::getId, id);
        wrapper.set(StringUtils.hasText(procId), BudgetSummary::getProcId, procId);
        wrapper.set(StringUtils.hasText(status), BudgetSummary::getStatus, status);
        update(wrapper);
    }

    //Todo: check    --Add By heng.wang 2021/05/08 14:49
    public String startProcessInstance(CreateProcessInstanceRepresentation representation) {
        String s = processInstancesService.startNewProcessInstance(representation);
        updateSummaryStatus(representation.getBusinessKey(), WorkflowConstants.RUNNING, s);
        return s;
    }

    /**
     * 流程结束状态更新.
     * 执行表达式:${BudgetSummaryService.endProcess(execution,"9")}.
     *
     * @param execution 执行实例
     * @param status 状态码
     */
    @SuppressWarnings("unused")
    public void endProcess(DelegateExecution execution, String status) {
        String businessKey = execution.getProcessInstanceBusinessKey();
        updateSummaryStatus(businessKey, status);
        String budgetBatchIds = getById(businessKey).getBudgetBatchIds();
//        budgetBatchService.updateBudgetBatchStatusById(budgetBatchIds, WorkflowConstants.END, null);
        budgetBatchService.summaryCompleted(budgetBatchIds, WorkflowConstants.END);

    }

    public IPage<BudgetSummaryVo> getSummary(BudgetSummaryQuery query, Page page) {
        return budgetSummaryMapper.selectSummary(page, query);
    }
}
