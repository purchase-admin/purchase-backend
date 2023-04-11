package cn.luckyh.purchase.budget.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.luckyh.purchase.budget.domain.BudgetSummaryBatch;
import cn.luckyh.purchase.budget.mapper.BudgetSummaryBatchMapper;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Service("BudgetSummaryBatchService")
@Slf4j
public class BudgetSummaryBatchService extends ServiceImpl<BudgetSummaryBatchMapper, BudgetSummaryBatch> {

    private final BudgetSummaryBatchMapper budgetSummaryBatchMapper;

    public BudgetSummaryBatchService(BudgetSummaryBatchMapper budgetSummaryBatchMapper) {
        this.budgetSummaryBatchMapper = budgetSummaryBatchMapper;
    }

    public void deleteBySummaryId(String summaryId) {
        this.deleteBySummaryIdAndBatchIds(summaryId, null);
    }

    public void deleteBySummaryIdAndBatchIds(String id, List<String> list) {
        LambdaQueryWrapper<BudgetSummaryBatch> wrapper = Wrappers.lambdaQuery(BudgetSummaryBatch.class);
        wrapper.eq(BudgetSummaryBatch::getSummary_id, id);
        wrapper.in(Objects.nonNull(list) && !list.isEmpty(), BudgetSummaryBatch::getBatch_id, list);
        remove(wrapper);
    }

    public List<BudgetSummaryBatch> getBySummaryId(String id) {
        LambdaQueryWrapper<BudgetSummaryBatch> wrapper = Wrappers.lambdaQuery(BudgetSummaryBatch.class);
        wrapper.eq(BudgetSummaryBatch::getSummary_id, id);
        return list(wrapper);
    }
}




