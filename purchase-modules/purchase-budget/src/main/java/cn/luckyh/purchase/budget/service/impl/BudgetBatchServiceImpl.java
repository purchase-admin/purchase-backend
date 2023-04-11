package cn.luckyh.purchase.budget.service.impl;

import cn.luckyh.purchase.budget.constant.BudgetConstants;
import cn.luckyh.purchase.budget.domain.Budget;
import cn.luckyh.purchase.budget.domain.BudgetBatch;
import cn.luckyh.purchase.budget.mapper.BudgetBatchMapper;
import cn.luckyh.purchase.budget.service.BudgetSummaryService;
import cn.luckyh.purchase.budget.service.IBudgetBatchService;
import cn.luckyh.purchase.budget.vo.BudgetBatchQuery;
import cn.luckyh.purchase.budget.vo.BudgetBatchVo;
import cn.luckyh.purchase.common.annotation.DataScope;
import cn.luckyh.purchase.common.constant.WorkflowConstants;
import cn.luckyh.purchase.common.exception.NotFoundException;
import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.workflow.service.runtime.ProcessInstancesService;
import cn.luckyh.purchase.workflow.service.runtime.TaskActionService;
import cn.luckyh.purchase.workflow.vo.runtime.CreateProcessInstanceRepresentation;
import cn.luckyh.purchase.workflow.vo.runtime.TaskCompleteRepresentation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 预算申请批Service业务层处理
 *
 * @author purchase
 * @date 2021-04-15
 */
@Service("BudgetBatchServiceImpl")
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
public class BudgetBatchServiceImpl extends ServiceImpl<BudgetBatchMapper, BudgetBatch> implements IBudgetBatchService {

    @Resource
    private BudgetBatchMapper budgetBatchMapper;

    @Autowired
    private BudgetServiceImpl budgetService;


    @Autowired
    BudgetSummaryService budgetSummaryService;
    @Resource
    private ProcessInstancesService processInstancesService;

    @Autowired
    private TaskActionService taskActionService;

    @Autowired
    TaskService taskService;

    /**
     * 查询预算申请批
     *
     * @param id 预算申请批ID
     * @return 预算申请批
     */
    @Override
    public BudgetBatch selectBudgetBatchById(String id) {
        return budgetBatchMapper.selectBudgetBatchById(id);
    }

    /**
     * 查询预算申请批列表
     *
     * @param budgetBatch 预算申请批
     * @return 预算申请批
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<BudgetBatchVo> selectBudgetBatchList(BudgetBatch budgetBatch) {
        //        budgetBatch.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId().toString());//设置当前登录人的部门参数
        List<BudgetBatchVo> budgetBatchVos = budgetBatchMapper.selectBudgetBatchList(budgetBatch);
        return budgetBatchVos;
    }

    /**
     * 新增预算申请批
     *
     * @param budgetBatch 预算申请批
     * @return 结果
     */
    @Override
    public int insertBudgetBatch(BudgetBatch budgetBatch) {
        if (StringUtil.isEmpty(budgetBatch.getCreateBy())) {
            budgetBatch.setCreateBy(SecurityUtils.getLoginUser().getUser().getNickName());
        }
        if (budgetBatch.getCreateTime() == null) {
            budgetBatch.setCreateTime(new Date());
        }
        return save(budgetBatch) ? 1 : 0;
    }

    /**
     * 修改预算申请批
     *
     * @param budgetBatch 预算申请批
     * @return 结果
     */
    @Override
    public int updateBudgetBatch(BudgetBatch budgetBatch) {
        return budgetBatchMapper.updateBudgetBatch(budgetBatch);
    }

    /**
     * 删除预算申请批信息
     *
     * @param id     预算申请批ID.
     * @param reason 删除原因.
     * @return 结果
     */
    @Override
    public int deleteBudgetBatchById(String id, String reason) {
        BudgetBatch data = getById(id);
        if (Objects.isNull(data)) {
            log.error("删除预算申请批信息是未找到数据,主键为:{}", id);
            return 0;
        }
        deleteSubData(data);
        deleteProcessData(data, reason);
        return budgetBatchMapper.deleteBudgetBatchById(id);
    }

    private void deleteProcessData(BudgetBatch data, String reason) {
        if (StringUtils.hasText(data.getProcId())) {
            processInstancesService.delete(data.getProcId(), reason);
        }
    }

    private void deleteSubData(BudgetBatch data) {
        LambdaQueryWrapper<Budget> queryWrapper = Wrappers.lambdaQuery(Budget.class);
        queryWrapper.eq(Budget::getBatchId, data.getId());
        budgetService.remove(queryWrapper);
    }

    @Override
    public void updateBudgetBatchStatusById(String id, String status, String procId) {
        BudgetBatch byId = getById(id);
        if (Objects.isNull(byId)) {
            log.error("部门预算业务数据:{}不存在,关联流程实例ID为:{}", id, procId);
            throw new NotFoundException("部门预算业务数据不存在");
        }
        LambdaUpdateWrapper<BudgetBatch> wrapper = Wrappers.lambdaUpdate(BudgetBatch.class);
        wrapper
                .eq(BudgetBatch::getId, id)
                .set(StringUtils.hasText(procId), BudgetBatch::getProcId, procId)
                .set(StringUtils.hasText(status), BudgetBatch::getStatus, status);
        update(wrapper);
        LambdaUpdateWrapper<Budget> budgetLambdaQueryWrapper = Wrappers.lambdaUpdate(Budget.class);
        budgetLambdaQueryWrapper.eq(Budget::getBatchId, id)
                .set(Budget::getApproveStatus, status);
        budgetService.update(budgetLambdaQueryWrapper);
    }

    @Override
    public IPage<BudgetBatch> pageSelectInIds(String ids, Page page) {
        return budgetBatchMapper.pageSelectByIds(page, ids);
    }

    @Override
    public String startProcess(CreateProcessInstanceRepresentation representation) {
        String s = processInstancesService.startNewProcessInstance(representation);
        BudgetBatch byId = getById(representation.getBusinessKey());
        if (Objects.isNull(byId)) {
            log.error("启动流程时未找到保存的部门预算数据,主键为{}", representation.getBusinessKey());
            throw new NotFoundException("启动流程时未找到保存的部门预算数据");
        }
        byId.setProcId(s).setStatus(WorkflowConstants.RUNNING);
        updateById(byId);
        return s;
    }


    @Override
    public void backToReSubmit(String summaryId, String batchId, String comment) {
        BudgetBatch data = getById(batchId);
        beforeBack(batchId, data);
        doBack(comment, data);
        afterBack(summaryId, batchId, data);
    }

    private void afterBack(String summaryId, String batchId, BudgetBatch data) {
        //从预算汇总中移除
        budgetSummaryService.deleteSummaryBatchData(summaryId, batchId);
        //更改数据状态
        data.setStatus(WorkflowConstants.RUNNING);
        updateById(data);
    }

    private void doBack(String comment, BudgetBatch data) {
        TaskCompleteRepresentation representation = new TaskCompleteRepresentation();
        representation.setAssignee(SecurityUtils.getUsername());
        //驳回预算申请
        representation.setOpinion(0);
        //驳回意见
        representation.setComment(comment);
        //驳回预算审批
        taskActionService.submitByProcessInstanceId(data.getProcId(), representation);
    }

    private void beforeBack(String batchId, BudgetBatch data) {
        if (Objects.isNull(data)) {
            log.error("部门预算数据不存在,数据主键为:{}", batchId);
            throw new NotFoundException("部门预算数据不存在");
        }
        //判断是否可以做退回操作
        if (!BudgetConstants.BUDGET_SUMMARY.equals(data.getStatus())) {
            throw new RuntimeException("当前环节不允许退回操作");
        }
    }

    @Override
    public IPage<BudgetBatch> getNotJoinSummaryList(BudgetBatchQuery query, Page page) {
        return budgetBatchMapper.selectNotJoinSummaryList(page, query);
    }


    @Override
    public void summaryCompleted(String id, String status) {
        BudgetBatch byId = getById(id);
        if (Objects.isNull(byId)) {
            log.error("部门预算业务数据:{}不存在}", id);
            throw new NotFoundException("部门预算业务数据不存在");
        }
        String procId = byId.getProcId();
        Task task = taskService.createTaskQuery().processInstanceId(procId).singleResult();
        if (task != null) {
            TaskCompleteRepresentation representation = new TaskCompleteRepresentation();
            representation.setAssignee(SecurityUtils.getUsername());
            //驳回预算申请
            representation.setOpinion(1);
            //驳回意见
            representation.setComment("");
            taskActionService.submitByProcessInstanceId(procId, representation);
        }
    }
}
