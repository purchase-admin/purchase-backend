package cn.luckyh.purchase.supplier.service;

import java.util.List;
import java.util.Map;

import cn.luckyh.purchase.supplier.domain.SupplierQuotation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 供应商报价记录 询价采购记录Service接口
 *
 * @author purchase
 * @date 2021-03-23
 */
public interface ISupplierQuotationService extends IService<SupplierQuotation> {
    /**
     * 查询供应商报价记录 询价采购记录
     *
     * @param id 供应商报价记录 询价采购记录ID
     * @return 供应商报价记录 询价采购记录
     */
    public SupplierQuotation selectSupplierQuotationById(String id);

    /**
     * 查询供应商报价记录 询价采购记录列表
     *
     * @param supplierQuotation 供应商报价记录 询价采购记录
     * @return 供应商报价记录 询价采购记录集合
     */
    public List<SupplierQuotation> selectSupplierQuotationList(SupplierQuotation supplierQuotation);

    /**
     * 新增供应商报价记录 询价采购记录
     *
     * @param supplierQuotation 供应商报价记录 询价采购记录
     * @return 结果
     */
    public int insertSupplierQuotation(SupplierQuotation supplierQuotation);

    /**
     * 修改供应商报价记录 询价采购记录
     *
     * @param supplierQuotation 供应商报价记录 询价采购记录
     * @return 结果
     */
    public int updateSupplierQuotation(SupplierQuotation supplierQuotation);

    /**
     * 批量删除供应商报价记录 询价采购记录
     *
     * @param ids 需要删除的供应商报价记录 询价采购记录ID
     * @return 结果
     */
    public int deleteSupplierQuotationByIds(String[] ids);

    /**
     * 删除供应商报价记录 询价采购记录信息
     *
     * @param id 供应商报价记录 询价采购记录ID
     * @return 结果
     */
    public int deleteSupplierQuotationById(String id);

    /**
     * 查看采购公告
     * @return
     */
    List<Map<String, Object>> selectNoticeList();
}
