package cn.luckyh.purchase.budget.service.impl;

import cn.luckyh.purchase.budget.domain.Budget;
import cn.luckyh.purchase.budget.domain.BudgetBatch;
import cn.luckyh.purchase.budget.mapper.BudgetMapper;
import cn.luckyh.purchase.budget.service.IBudgetBatchService;
import cn.luckyh.purchase.budget.service.IBudgetService;
import cn.luckyh.purchase.budget.vo.BudgetCollectVo;
import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.common.utils.poi.ExcelUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 采购预算 Service业务层处理
 *
 * @author purchase
 * @date 2021-03-19
 */
@Service
@Slf4j
public class BudgetServiceImpl extends ServiceImpl<BudgetMapper, Budget> implements IBudgetService {

    @Resource
    private BudgetMapper budgetMapper;

    @Resource
    private IBudgetBatchService budgetBatchService;

    /**
     * 查询采购预算
     *
     * @param id 采购预算 ID
     * @return 采购预算
     */
    @Override
    public Budget selectBudgetById(String id) {
        return budgetMapper.selectBudgetById(id);
    }

    /**
     * 查询采购预算 列表
     *
     * @param budget 采购预算
     * @return 采购预算
     */
    @Override
    public List<Budget> selectBudgetList(Budget budget) {
        return budgetMapper.selectBudgetList(budget);
    }

    /**
     * 新增采购预算
     *
     * @param budget 采购预算
     * @return 结果
     */
    @Override
    public int insertBudget(Budget budget) {
        String batchId = budget.getBatchId();
        BudgetBatch budgetBatch = budgetBatchService.getById(batchId);
        if (budgetBatch == null) {
            throw new RuntimeException("预算上级关联数据未找到");
        }
        if (StringUtil.isEmpty(budget.getCreateBy())) {
            budget.setCreateBy(SecurityUtils.getUsername());
        }
        if (budget.getCreateTime() == null) {
            budget.setCreateTime(new Date());
        }
        budget.setBudgetYear(budgetBatch.getYear());
        //return budgetMapper.insertBudget(budget)
        save(budget);
        return 1;
    }

    /**
     * 修改采购预算
     *
     * @param budget 采购预算
     * @return 结果
     */
    @Override
    public int updateBudget(Budget budget) {
        if (StringUtil.isEmpty(budget.getUpdateBy())) {
            budget.setUpdateBy(SecurityUtils.getUsername());
        }
        if (budget.getUpdateTime() == null) {
            budget.setUpdateTime(new Date());
        }
        if (StringUtil.isNotEmpty(budget.getId())) {//如果id传的是逗号拼接的字符串，则批量修改状态
            List<String> ids = Arrays.asList(budget.getId().split(","));
            int i = 0;
            if (ids.size() > 1) {
                for (String id : ids) {
                    Budget b = new Budget();
                    b.setId(id);
                    b.setApproveStatus(budget.getApproveStatus());
                    i += budgetMapper.updateBudget(b);
                }
                return i;
            }
        }
        return budgetMapper.updateBudget(budget);
    }

    /**
     * 批量删除采购预算
     *
     * @param ids 需要删除的采购预算 ID
     * @return 结果
     */
    @Override
    public int deleteBudgetByIds(String[] ids) {
        return budgetMapper.deleteBudgetByIds(ids);
    }

    /**
     * 删除采购预算 信息
     *
     * @param id 采购预算 ID
     * @return 结果
     */
    @Override
    public int deleteBudgetById(String id) {
        return budgetMapper.deleteBudgetById(id);
    }

    /**
     * 批量导入采购预算数据
     *
     * @param file
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public R importExcel(MultipartFile file) {
        ExcelUtil<Budget> util = new ExcelUtil<Budget>(Budget.class);
        List<Budget> errList = new ArrayList<>();
        try {
            List<Budget> list = util.importExcel(file.getInputStream());
            for (Budget budget : list) {
                /*int re = budgetMapper.insertBudget(budget);
                if (re == 0) {//如果插入失败，则记录失败数据
                    errList.add(budget);
                }*/
                if (!save(budget)) {
                    errList.add(budget);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导入excle异常，请联系管理员");
        }
        if (errList.size() > 0) {
            return R.error("导入失败，请检查数据或联系管理员", errList);
        }
        return R.success("导入成功");
    }

    @Override
    public List<BudgetCollectVo> selectBudgetCollectList(Budget budget) {
        return budgetMapper.selectBudgetCollectList(budget);
    }

    @Override
    public int updateBudgetByProcInstId(Budget budget) {
        return budgetMapper.updateBudgetByProcInstId(budget);
    }
}
