package cn.luckyh.purchase.execute.mapper;

import cn.luckyh.purchase.execute.domain.InquiryPurchase;
import cn.luckyh.purchase.execute.vo.InquiryPurchaseVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 询价采购 Mapper接口
 *
 * @author purchase
 * @date 2021-03-24
 */
public interface InquiryPurchaseMapper extends BaseMapper<InquiryPurchase> {
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
     * 删除询价采购
     *
     * @param id 询价采购 ID
     * @return 结果
     */
    int deleteInquiryPurchaseById(String id);

    /**
     * 批量删除询价采购 
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteInquiryPurchaseByIds(String[] ids);

    /**
     * 根据供应商ID查询采购公告
     * @param supplierId
     * @return
     */
    List<Map<String, Object>> selectNoticeList(String supplierId);

    /**
     * 查看公开的采购公告
     *
     * @return
     */
    List<Map<String, Object>> selectOpenNoticeList(@Param("supplierId") String supplierId);

    InquiryPurchase getOnlyOne(@Param("taskId") String taskId, @Param("supplierId") String supplierId);

    InquiryPurchase selectInquiryPurchaseTaskIdAndSupplierId(@Param("taskId") String taskId, @Param("supplierId") String supplierId);

    /**
     * 未登录的用户查看公开的采供公告
     * @return
     */
    List<Map<String, Object>> selectOpenNoticeListWithoutLogin();

    /**
     * 查询出已参与报价的采购任务id，用于过滤公开招标数据
     * @param taskIdList
     * @param supplierId
     * @return
     */
    List<String> selectIsJoinTaskId(@Param("taskIdList") List<String> taskIdList, @Param("supplierId") String supplierId);
}
