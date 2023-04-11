package cn.luckyh.purchase.budget.mapper;

import java.util.List;

import cn.luckyh.purchase.budget.domain.Budget;
import cn.luckyh.purchase.budget.vo.BudgetCollectVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 采购预算 Mapper接口
 *
 * @author purchase
 * @date 2021-03-19
 */
public interface BudgetMapper extends BaseMapper<Budget> {
    /**
     * 查询采购预算
     *
     * @param id 采购预算 ID
     * @return 采购预算
     */
    public Budget selectBudgetById(String id);

    /**
     * 查询采购预算 列表
     *
     * @param budget 采购预算
     * @return 采购预算 集合
     */
    public List<Budget> selectBudgetList(Budget budget);

    /**
     * 新增采购预算
     *
     * @param budget 采购预算
     * @return 结果
     */
    public int insertBudget(Budget budget);

    /**
     * 修改采购预算
     *
     * @param budget 采购预算
     * @return 结果
     */
    public int updateBudget(Budget budget);

    /**
     * 删除采购预算
     *
     * @param id 采购预算 ID
     * @return 结果
     */
    public int deleteBudgetById(String id);

    /**
     * 批量删除采购预算
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBudgetByIds(String[] ids);

    /**
     * 查询预算汇总信息
     * @param budget
     * @return
     */
    List<BudgetCollectVo> selectBudgetCollectList(Budget budget);

    /**
     * 修改采购预算状态
     * @param budget
     * @return
     */
    int updateBudgetByProcInstId(Budget budget);
}
