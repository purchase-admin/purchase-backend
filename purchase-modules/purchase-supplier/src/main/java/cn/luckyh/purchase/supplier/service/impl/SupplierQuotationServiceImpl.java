package cn.luckyh.purchase.supplier.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.execute.service.IInquiryPurchaseService;
import cn.luckyh.purchase.supplier.domain.Supplier;
import cn.luckyh.purchase.supplier.service.ISupplierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.luckyh.purchase.supplier.mapper.SupplierQuotationMapper;
import cn.luckyh.purchase.supplier.domain.SupplierQuotation;
import cn.luckyh.purchase.supplier.service.ISupplierQuotationService;

import javax.annotation.Resource;

/**
 * 供应商报价记录 询价采购记录Service业务层处理
 *
 * @author purchase
 * @date 2021-03-23
 */
@Service
public class SupplierQuotationServiceImpl extends ServiceImpl<SupplierQuotationMapper,SupplierQuotation> implements ISupplierQuotationService {
    @Resource
    private SupplierQuotationMapper supplierQuotationMapper;

    @Autowired
    private ISupplierService supplierService;

    @Autowired
    private IInquiryPurchaseService inquiryPurchaseService;

    /**
     * 查询供应商报价记录 询价采购记录
     *
     * @param id 供应商报价记录 询价采购记录ID
     * @return 供应商报价记录 询价采购记录
     */
    @Override
    public SupplierQuotation selectSupplierQuotationById(String id) {
        return supplierQuotationMapper.selectSupplierQuotationById(id);
    }

    /**
     * 查询供应商报价记录 询价采购记录列表
     *
     * @param supplierQuotation 供应商报价记录 询价采购记录
     * @return 供应商报价记录 询价采购记录
     */
    @Override
    public List<SupplierQuotation> selectSupplierQuotationList(SupplierQuotation supplierQuotation) {
        return supplierQuotationMapper.selectSupplierQuotationList(supplierQuotation);
    }

    /**
     * 新增供应商报价记录 询价采购记录
     *
     * @param supplierQuotation 供应商报价记录 询价采购记录
     * @return 结果
     */
    @Override
    public int insertSupplierQuotation(SupplierQuotation supplierQuotation) {
        //  return supplierQuotationMapper.insertSupplierQuotation(supplierQuotation);
        if(supplierQuotation.getSupplierId().indexOf(",")!=-1){
            List<String> supplierIds = Arrays.asList(supplierQuotation.getSupplierId().split(","));
            List<SupplierQuotation> list = new ArrayList<>();
            for(String id :supplierIds){
                Supplier supplier = supplierService.getById(id);
                supplierQuotation.setSupplierId(id);
                supplierQuotation.setSupplierContactName(supplier.getBusinessContact());
                supplierQuotation.setSupplierContactPhone(supplier.getPhone());
                list.add(supplierQuotation);
            }
            return saveBatch(list)?0:1;
        }
        return save(supplierQuotation)?1:0;
    }

    /**
     * 修改供应商报价记录 询价采购记录
     *
     * @param supplierQuotation 供应商报价记录 询价采购记录
     * @return 结果
     */
    @Override
    public int updateSupplierQuotation(SupplierQuotation supplierQuotation) {
        return supplierQuotationMapper.updateSupplierQuotation(supplierQuotation);
    }

    /**
     * 批量删除供应商报价记录 询价采购记录
     *
     * @param ids 需要删除的供应商报价记录 询价采购记录ID
     * @return 结果
     */
    @Override
    public int deleteSupplierQuotationByIds(String[] ids) {
        return supplierQuotationMapper.deleteSupplierQuotationByIds(ids);
    }

    /**
     * 删除供应商报价记录 询价采购记录信息
     *
     * @param id 供应商报价记录 询价采购记录ID
     * @return 结果
     */
    @Override
    public int deleteSupplierQuotationById(String id) {
        return supplierQuotationMapper.deleteSupplierQuotationById(id);
    }

    @Override
    public List<Map<String, Object>> selectNoticeList() {
        String loginName = SecurityUtils.getUsername();
        Supplier supplier = new Supplier();
        supplier.setLoginName(loginName);
        List<Map<String,Object>> mapList1 = inquiryPurchaseService.selectOpenNoticeList("");//查询公开招标的信息
        List<Supplier> suppliers = supplierService.selectSupplierList(supplier);//通过当前登录人账号获取关联的供应商信息
        if (suppliers.size()==1){
            List<Map<String, Object>> mapList = inquiryPurchaseService.selectNoticeList(suppliers.get(0).getId());
            mapList.addAll(mapList1);
            return mapList;
        }
        return mapList1;
    }
}
