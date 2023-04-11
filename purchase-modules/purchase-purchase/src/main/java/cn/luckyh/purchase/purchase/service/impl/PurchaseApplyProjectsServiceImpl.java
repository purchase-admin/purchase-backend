package cn.luckyh.purchase.purchase.service.impl;

import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.utils.StringUtils;
import cn.luckyh.purchase.purchase.domain.PurchaseApplyProjects;
import cn.luckyh.purchase.purchase.mapper.PurchaseApplyProjectsMapper;
import cn.luckyh.purchase.purchase.service.IPurchaseApplyProjectsService;
import cn.luckyh.purchase.purchase.vo.PurchaseApplyProjectsVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购申请项目 Service业务层处理
 *
 * @author purchase
 * @date 2021-03-22
 */
@Service
public class PurchaseApplyProjectsServiceImpl extends ServiceImpl<PurchaseApplyProjectsMapper, PurchaseApplyProjects> implements IPurchaseApplyProjectsService {
    @Resource
    private PurchaseApplyProjectsMapper purchaseApplyProjectsMapper;

    /**
     * 查询采购申请项目
     *
     * @param id 采购申请项目 ID
     * @return 采购申请项目
     */
    @Override
    public PurchaseApplyProjectsVo selectPurchaseApplyProjectsById(String id) {
        return purchaseApplyProjectsMapper.selectPurchaseApplyProjectsById(id);
    }

    /**
     * 查询采购申请项目 列表
     *
     * @param purchaseApplyProjects 采购申请项目
     * @return 采购申请项目
     */
    @Override
    public List<PurchaseApplyProjectsVo> selectPurchaseApplyProjectsList(PurchaseApplyProjects purchaseApplyProjects) {
        return purchaseApplyProjectsMapper.selectPurchaseApplyProjectsList(purchaseApplyProjects);
    }

    /**
     * 新增采购申请项目
     *
     * @param purchaseApplyProjects 采购申请项目
     * @return 结果
     */
    @Override
    public int insertPurchaseApplyProjects(PurchaseApplyProjects purchaseApplyProjects) {
        return save(purchaseApplyProjects) ? 1 : 0;
    }

    /**
     * 修改采购申请项目
     *
     * @param purchaseApplyProjects 采购申请项目
     * @return 结果
     */
    @Override
    public R updatePurchaseApplyProjects(PurchaseApplyProjects purchaseApplyProjects) {
        String num = purchaseApplyProjects.getNum();
        BigDecimal budgetCost = purchaseApplyProjects.getBudgetCost();
        int verifyNumResult = 0,verifyMoneyResult=0;
        PurchaseApplyProjects purchaseApplyProjects1 = getById(purchaseApplyProjects.getId());
        if("预算内".equals(purchaseApplyProjects.getBudgetType())) {//预算内的数据才进行校验
            if (StringUtils.isNotEmpty(purchaseApplyProjects.getNum())) {//数量不为空的校验数量
                if (!purchaseApplyProjects.getNum().equals(purchaseApplyProjects1.getNum())) {//数量被修改过的才校验
                    //计算修改后的增量，将增量设置在参数中进行校验
                    Integer addNum = Integer.parseInt(purchaseApplyProjects.getNum()) - Integer.parseInt(purchaseApplyProjects1.getNum() == null ? "0" : purchaseApplyProjects1.getNum());
                    purchaseApplyProjects.setNum(addNum.toString());
                    verifyNumResult = purchaseApplyProjectsMapper.verifyNum(purchaseApplyProjects);
                }else{
                    verifyNumResult=1;
                }
            }
            if (purchaseApplyProjects1.getBudgetCost() != purchaseApplyProjects.getBudgetCost()) {//金额被修改过的才校验
                //计算修改后的增量，将增量设置在参数中进行校验
                BigDecimal addMoney = purchaseApplyProjects.getBudgetCost().subtract(purchaseApplyProjects1.getBudgetCost());
                purchaseApplyProjects.setBudgetCost(addMoney);
                verifyMoneyResult = purchaseApplyProjectsMapper.verifyMoney(purchaseApplyProjects);
            }else{
                verifyMoneyResult=1;
            }
            if (verifyNumResult == 0) {
                return R.error("采购数量超出年度总预算，请选择预算外申请");
            }
            if (verifyMoneyResult == 0) {
                return R.error("采购金额超出年度总预算，请选择预算外申请");
            }
        }
        purchaseApplyProjects.setBudgetCost(budgetCost);
        purchaseApplyProjects.setNum(num);
        return purchaseApplyProjectsMapper.updatePurchaseApplyProjects(purchaseApplyProjects)>0?R.success():R.error();
    }

    /**
     * 批量删除采购申请项目
     *
     * @param ids 需要删除的采购申请项目 ID
     * @return 结果
     */
    @Override
    public int deletePurchaseApplyProjectsByIds(String[] ids) {
        return purchaseApplyProjectsMapper.deletePurchaseApplyProjectsByIds(ids);
    }

    /**
     * 删除采购申请项目 信息
     *
     * @param id 采购申请项目 ID
     * @return 结果
     */
    @Override
    public int deletePurchaseApplyProjectsById(String id) {
        return purchaseApplyProjectsMapper.deletePurchaseApplyProjectsById(id);
    }

    @Override
    public Map<String, Object> verifyMoneyAndNum(PurchaseApplyProjects purchaseApplyProjects) {
        Map<String,Object> result = new HashMap<>();
        if("预算外".equals(purchaseApplyProjects.getBudgetType())){
            result.put("isOk",true);
            return result;
        }
        int verifyMoneyResult = purchaseApplyProjectsMapper.verifyMoney(purchaseApplyProjects);
        int verifyNumResult = purchaseApplyProjectsMapper.verifyNum(purchaseApplyProjects);
        result.put("verifyMoneyResult",verifyMoneyResult);
        result.put("verifyNumResult",verifyNumResult);
        if(verifyMoneyResult==1 && verifyNumResult==1){
            result.put("isOk",true);
        }else{
            result.put("isOk",false);
        }
        if(verifyMoneyResult==0){
            result.put("errorMsg","采购金额超出年度总预算，请选择预算外申请");
        }
        if(verifyNumResult==0){
            result.put("errorMsg","采购数量超出年度总预算，请选择预算外申请");
        }
        return result;
    }
}
