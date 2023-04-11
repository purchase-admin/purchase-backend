package cn.luckyh.purchase.execute.service;

import cn.luckyh.purchase.execute.domain.InquiryPurchase;
import cn.luckyh.purchase.execute.vo.ChooseSupplierDto;
import cn.luckyh.purchase.execute.vo.InquiryPurchaseVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 询价采购 Service接口
 *
 * @author purchase
 * @date 2021-03-24
 */
public interface IInquiryPurchaseService extends IService<InquiryPurchase> {
    /**
     * 查询询价采购
     *
     * @param id 询价采购 ID
     * @return 询价采购
     */
    InquiryPurchaseVo selectInquiryPurchaseById(String id);

    /**
     * 查询询价采购 列表
     *
     * @param inquiryPurchase 询价采购
     * @return 询价采购 集合
     */
    List<InquiryPurchaseVo> selectInquiryPurchaseList(InquiryPurchase inquiryPurchase);

    /**
     * 新增询价采购
     *
     * @param inquiryPurchase 询价采购
     * @return 结果
     */
    int insertInquiryPurchase(InquiryPurchase inquiryPurchase);

    /**
     * 修改询价采购 
     *
     * @param inquiryPurchase 询价采购 
     * @return 结果
     */
    int updateInquiryPurchase(InquiryPurchase inquiryPurchase);

    /**
     * 批量删除询价采购 
     *
     * @param ids 需要删除的询价采购 ID
     * @return 结果
     */
    int deleteInquiryPurchaseByIds(String[] ids);

    /**
     * 删除询价采购 信息
     *
     * @param id 询价采购 ID
     * @return 结果
     */
    int deleteInquiryPurchaseById(String id);

    int chooseSupplier(InquiryPurchase inquiryPurchase);

    int chooseSupplier(ChooseSupplierDto chooseSupplierDto);

    /**
     * 根据供应商ID查询采购公告信息
     *
     * @param supplierId
     * @return
     */
    List<Map<String, Object>> selectNoticeList(String supplierId);

    /**
     * 查看公开的采购公告
     * @return
     */
    List<Map<String, Object>> selectOpenNoticeList(String supplierId);

    /**
     * 根据任务ID和供应商ID查询唯一对应
     * @param taskId
     * @param supplierId
     * @return
     */
    InquiryPurchase getOnlyOne(String taskId, String supplierId);

    /**
     * 校验是否到了询价截止日期
     *
     * @param inquiryPurchase
     * @return
     */
    boolean checkEndTime(InquiryPurchase inquiryPurchase);

    boolean checkEndTime(String taskId);

    /**
     * 询价采购作废
     *
     * @param taskId
     * @return
     */
    int deleteInquiryPurchaseByTaskId(String taskId);

    void cancelSupplier(String taskId, String supplier);
}
