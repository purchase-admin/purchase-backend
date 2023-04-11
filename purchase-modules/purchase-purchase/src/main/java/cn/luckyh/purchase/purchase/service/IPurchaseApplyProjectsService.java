package cn.luckyh.purchase.purchase.service;

import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.purchase.domain.PurchaseApplyProjects;
import cn.luckyh.purchase.purchase.vo.PurchaseApplyProjectsVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 采购申请项目 Service接口
 *
 * @author purchase
 * @date 2021-03-22
 */
public interface IPurchaseApplyProjectsService extends IService<PurchaseApplyProjects> {
    /**
     * 查询采购申请项目
     *
     * @param id 采购申请项目 ID
     * @return 采购申请项目
     */
    PurchaseApplyProjectsVo selectPurchaseApplyProjectsById(String id);

    /**
     * 查询采购申请项目 列表
     *
     * @param purchaseApplyProjects 采购申请项目
     * @return 采购申请项目 集合
     */
    List<PurchaseApplyProjectsVo> selectPurchaseApplyProjectsList(PurchaseApplyProjects purchaseApplyProjects);

    /**
     * 新增采购申请项目
     *
     * @param purchaseApplyProjects 采购申请项目
     * @return 结果
     */
    int insertPurchaseApplyProjects(PurchaseApplyProjects purchaseApplyProjects);

    /**
     * 修改采购申请项目
     *
     * @param purchaseApplyProjects 采购申请项目
     * @return 结果
     */
    R updatePurchaseApplyProjects(PurchaseApplyProjects purchaseApplyProjects);

    /**
     * 批量删除采购申请项目
     *
     * @param ids 需要删除的采购申请项目 ID
     * @return 结果
     */
    int deletePurchaseApplyProjectsByIds(String[] ids);

    /**
     * 删除采购申请项目 信息
     *
     * @param id 采购申请项目 ID
     * @return 结果
     */
    int deletePurchaseApplyProjectsById(String id);

    /**
     * 校验采购申请的金额和数量
     * @param purchaseApplyProjects
     */
    Map<String, Object> verifyMoneyAndNum(PurchaseApplyProjects purchaseApplyProjects);
}
