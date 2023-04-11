package cn.luckyh.purchase.supplier.service.impl;

import cn.luckyh.purchase.supplier.domain.SupplierProjectType;
import cn.luckyh.purchase.supplier.mapper.SupplierProjectTypeMapper;
import cn.luckyh.purchase.supplier.service.ISupplierProjectTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SupplierProjectTypeServiceImpl extends ServiceImpl<SupplierProjectTypeMapper, SupplierProjectType> implements ISupplierProjectTypeService {

    @Override
    public boolean removeBySupplier(String supplierId) {
        QueryWrapper<SupplierProjectType> wrapper = new QueryWrapper();
        wrapper.eq("supplier_id",supplierId);
        return remove(wrapper);
    }
}
