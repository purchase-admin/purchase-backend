package cn.luckyh.purchase.supplier.service;

import cn.luckyh.purchase.supplier.domain.SupplierProjectMapping;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface SupplierProjectMappingService extends IService<SupplierProjectMapping> {

    void deleteBySupplierId(String id);

    void deleteBySupplierIds(List<String> list);
}
