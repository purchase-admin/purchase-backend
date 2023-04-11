package cn.luckyh.purchase.budget.service;

import cn.luckyh.purchase.budget.domain.Budget;
import cn.luckyh.purchase.budget.vo.BudgetCollectVo;
import cn.luckyh.purchase.common.core.domain.R;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 采购预算 Service接口
 *
 * @author purchase
 * @date 2021-03-19
 */
public interface IBudgetService extends IService<Budget> {

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
     * 批量删除采购预算
     *
     * @param ids 需要删除的采购预算 ID
     * @return 结果
     */
    public int deleteBudgetByIds(String[] ids);

    /**
     * 删除采购预算 信息
     *
     * @param id 采购预算 ID
     * @return 结果
     */
    public int deleteBudgetById(String id);

    /**
     * 批量导入预算信息
     *
     * @param file
     * @return
     */
    public R importExcel(MultipartFile file);

    /**
     * 采购预算汇总 信息
     *
     * @param budget
     * @return
     */
    List<BudgetCollectVo> selectBudgetCollectList(Budget budget);

    /**
     * 修改采购预算状态
     *
     * @param budget
     * @return
     */
    int updateBudgetByProcInstId(Budget budget);



}
