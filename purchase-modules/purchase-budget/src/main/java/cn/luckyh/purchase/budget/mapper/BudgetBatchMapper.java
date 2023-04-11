package cn.luckyh.purchase.budget.mapper;

import cn.luckyh.purchase.budget.domain.BudgetBatch;
import cn.luckyh.purchase.budget.vo.BudgetBatchQuery;
import cn.luckyh.purchase.budget.vo.BudgetBatchVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算申请批Mapper接口
 *
 * @author purchase
 * @date 2021-04-15
 */
public interface BudgetBatchMapper extends BaseMapper<BudgetBatch> {

    /**
     * 查询预算申请批
     *
     * @param id 预算申请批ID
     * @return 预算申请批
     */
    public BudgetBatch selectBudgetBatchById(String id);

    /**
     * 查询预算申请批列表
     *
     * @param budgetBatch 预算申请批
     * @return 预算申请批集合
     */
    public List<BudgetBatchVo> selectBudgetBatchList(BudgetBatch budgetBatch);

    /**
     * 新增预算申请批
     *
     * @param budgetBatch 预算申请批
     * @return 结果
     */
    public int insertBudgetBatch(BudgetBatch budgetBatch);

    /**
     * 修改预算申请批
     *
     * @param budgetBatch 预算申请批
     * @return 结果
     */
    public int updateBudgetBatch(BudgetBatch budgetBatch);

    /**
     * 删除预算申请批
     *
     * @param id 预算申请批ID
     * @return 结果
     */
    public int deleteBudgetBatchById(String id);

    /**
     * 批量删除预算申请批
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBudgetBatchByIds(String[] ids);

    IPage<BudgetBatch> pageSelectByIds(@Param("page") Page page, @Param("ids") String ids);

    IPage<BudgetBatch> selectNotJoinSummaryList(@Param("page") Page page, @Param("query") BudgetBatchQuery query);
}
