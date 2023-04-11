package cn.luckyh.purchase.budget.mapper;

import cn.luckyh.purchase.budget.domain.BudgetSummary;
import cn.luckyh.purchase.budget.vo.BudgetSummaryQuery;
import cn.luckyh.purchase.budget.vo.BudgetSummaryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/05/08 10:09
 */
@Mapper
public interface BudgetSummaryMapper extends BaseMapper<BudgetSummary> {

    List<BudgetSummaryVo> selectSummary(BudgetSummaryQuery query);

    IPage<BudgetSummaryVo> selectSummary(@Param("page") Page page, @Param("query") BudgetSummaryQuery query);
}
