package cn.luckyh.purchase.supplier.service;

import cn.luckyh.purchase.supplier.domain.SupplierProjectType;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISupplierProjectTypeService extends IService<SupplierProjectType> {

    boolean removeBySupplier(String supplierId);
}
