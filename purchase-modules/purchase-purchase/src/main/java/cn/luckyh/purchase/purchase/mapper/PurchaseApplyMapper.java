package cn.luckyh.purchase.purchase.mapper;

import cn.luckyh.purchase.purchase.domain.PurchaseApply;
import cn.luckyh.purchase.purchase.vo.PurchaseApplyListVo;
import cn.luckyh.purchase.purchase.vo.PurchaseApplyVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 采购申请 Mapper接口
 *
 * @author zhouwenchao
 * @date 2021-03-20
 */
public interface PurchaseApplyMapper extends BaseMapper<PurchaseApply> {
    /**
     * 查询采购申请
     *
     * @param id 采购申请 ID
     * @return 采购申请
     */
    public PurchaseApply selectPurchaseApplyById(String id);

    /**
     * 查询采购申请 列表
     *
     * @param purchaseApply 采购申请
     * @return 采购申请 集合
     */
    public List<PurchaseApplyListVo> selectPurchaseApplyList(PurchaseApply purchaseApply);

    /**
     * 新增采购申请
     *
     * @param purchaseApply 采购申请
     * @return 结果
     */
    public int insertPurchaseApply(PurchaseApply purchaseApply);

    /**
     * 修改采购申请
     *
     * @param purchaseApply 采购申请
     * @return 结果
     */
    public int updatePurchaseApply(PurchaseApply purchaseApply);

    /**
     * 删除采购申请
     *
     * @param id 采购申请 ID
     * @return 结果
     */
    public int deletePurchaseApplyById(String id);

    /**
     * 批量删除采购申请
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePurchaseApplyByIds(String[] ids);

    List<PurchaseApplyVo> selectContractProjectList(PurchaseApplyVo vo);
}
