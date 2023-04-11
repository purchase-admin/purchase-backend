package cn.luckyh.purchase.budget.service;

import cn.luckyh.purchase.budget.domain.BudgetBatch;
import cn.luckyh.purchase.budget.vo.BudgetBatchQuery;
import cn.luckyh.purchase.budget.vo.BudgetBatchVo;
import cn.luckyh.purchase.workflow.vo.runtime.CreateProcessInstanceRepresentation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 预算申请批Service接口.
 *
 * @author purchase
 * @since 2021-04-15
 */
public interface IBudgetBatchService extends IService<BudgetBatch> {

    /**
     * 查询预算申请批
     *
     * @param id 预算申请批ID
     * @return 预算申请批
     */
    BudgetBatch selectBudgetBatchById(String id);

    /**
     * 查询预算申请批列表
     *
     * @param budgetBatch 预算申请批
     * @return 预算申请批集合
     */
    List<BudgetBatchVo> selectBudgetBatchList(BudgetBatch budgetBatch);

    /**
     * 新增预算申请批
     *
     * @param budgetBatch 预算申请批
     * @return 结果
     */
    int insertBudgetBatch(BudgetBatch budgetBatch);

    /**
     * 修改预算申请批
     *
     * @param budgetBatch 预算申请批
     * @return 结果
     */
    int updateBudgetBatch(BudgetBatch budgetBatch);

    /**
     * 删除预算申请批信息
     *
     * @param id     预算申请批ID
     * @param reason 删除原因
     * @return 结果
     */
    int deleteBudgetBatchById(String id, String reason);

    void updateBudgetBatchStatusById(String id, String Status, String procId);

    public IPage<BudgetBatch> pageSelectInIds(String ids, Page page);

    String startProcess(CreateProcessInstanceRepresentation representation);

    /**
     * 退回重新发起
     *
     * @param summaryId 年度预算ID
     * @param batchId   部门预算ID
     * @param comment   退回意见
     */
    void backToReSubmit(String summaryId, String batchId, String comment);

    IPage<BudgetBatch> getNotJoinSummaryList(BudgetBatchQuery query, Page page);

    void summaryCompleted(String id, String status);
}
