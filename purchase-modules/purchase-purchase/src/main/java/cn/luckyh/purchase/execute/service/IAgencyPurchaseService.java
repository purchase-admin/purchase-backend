package cn.luckyh.purchase.execute.service;

import cn.luckyh.purchase.execute.domain.AgencyPurchase;
import cn.luckyh.purchase.execute.vo.AgencyPurchaseVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 代理采购 Service接口
 *
 * @author purchase
 * @date 2021-03-24
 */
public interface IAgencyPurchaseService extends IService<AgencyPurchase> {
    /**
     * 查询代理采购
     *
     * @param id 代理采购 ID
     * @return 代理采购
     */
    AgencyPurchaseVo selectAgencyPurchaseById(String id);

    /**
     * 查询代理采购 列表
     *
     * @param agencyPurchase 代理采购
     * @return 代理采购 集合
     */
    List<AgencyPurchase> selectAgencyPurchaseList(AgencyPurchase agencyPurchase);

    /**
     * 新增代理采购
     *
     * @param agencyPurchase 代理采购
     * @return 结果
     */
    int insertAgencyPurchase(AgencyPurchase agencyPurchase);

    /**
     * 修改代理采购
     *
     * @param agencyPurchase 代理采购
     * @return 结果
     */
    int updateAgencyPurchase(AgencyPurchase agencyPurchase);

    /**
     * 批量删除代理采购
     *
     * @param ids 需要删除的代理采购 ID
     * @return 结果
     */
    int deleteAgencyPurchaseByIds(String[] ids);

    /**
     * 删除代理采购 信息
     *
     * @param id 代理采购 ID
     * @return 结果
     */
    int deleteAgencyPurchaseById(String id);
}
