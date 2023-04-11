package cn.luckyh.purchase.purchase.mapper;

import cn.luckyh.purchase.purchase.domain.PurchaseApplyProjects;
import cn.luckyh.purchase.purchase.vo.PurchaseApplyProjectsVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 采购申请项目 Mapper接口
 *
 * @author purchase
 * @date 2021-03-22
 */
public interface PurchaseApplyProjectsMapper extends BaseMapper<PurchaseApplyProjects> {
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
    int updatePurchaseApplyProjects(PurchaseApplyProjects purchaseApplyProjects);

    /**
     * 删除采购申请项目
     *
     * @param id 采购申请项目 ID
     * @return 结果
     */
    int deletePurchaseApplyProjectsById(String id);

    /**
     * 批量删除采购申请项目
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deletePurchaseApplyProjectsByIds(String[] ids);

    int verifyMoney(PurchaseApplyProjects purchaseApplyProjects);

    int verifyNum(PurchaseApplyProjects purchaseApplyProjects);
}
