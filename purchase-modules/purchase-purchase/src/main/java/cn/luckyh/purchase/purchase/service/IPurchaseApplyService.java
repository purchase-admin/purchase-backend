package cn.luckyh.purchase.purchase.service;

import cn.luckyh.purchase.purchase.domain.PurchaseApply;
import cn.luckyh.purchase.purchase.vo.PurchaseApplyListVo;
import cn.luckyh.purchase.purchase.vo.PurchaseApplyVo;
import cn.luckyh.purchase.workflow.vo.runtime.CreateProcessInstanceRepresentation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 采购申请 Service接口
 *
 * @author zhouwenchao
 * @date 2021-03-20
 */
public interface IPurchaseApplyService extends IService<PurchaseApply> {
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
     * 批量删除采购申请
     *
     * @param ids 需要删除的采购申请 ID
     * @return 结果
     */
    public int deletePurchaseApplyByIds(String[] ids);

    /**
     * 删除采购申请 信息
     *
     * @param id 采购申请 ID
     * @return 结果
     */
    public int deletePurchaseApplyById(String id);

    /**
     * 批量保存采购申请以及批次信息
     *
     * @param purchaseApplys
     * @return
     */
    boolean insertPurchaseApplys(List<PurchaseApply> purchaseApplys);

    List<PurchaseApplyVo> selectContractProjectList(PurchaseApplyVo vo);

    /**
     * 发起采购申请流程.
     *
     * @param representation 流程启动 Rest 对象.
     * @return 流程实例ID
     * @author heng.wang
     */
    String startProcess(CreateProcessInstanceRepresentation representation);

}
