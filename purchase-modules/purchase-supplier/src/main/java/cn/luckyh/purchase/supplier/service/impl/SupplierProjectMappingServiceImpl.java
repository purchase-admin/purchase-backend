package cn.luckyh.purchase.supplier.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.luckyh.purchase.supplier.domain.SupplierProjectMapping;
import cn.luckyh.purchase.supplier.service.SupplierProjectMappingService;
import cn.luckyh.purchase.supplier.mapper.SupplierProjectMappingMapper;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class SupplierProjectMappingServiceImpl extends ServiceImpl<SupplierProjectMappingMapper, SupplierProjectMapping>
        implements SupplierProjectMappingService {

    @Override
    public void deleteBySupplierId(@NotEmpty String id) {
        List<String> idsList = new ArrayList<>();
        idsList.add(id);
        deleteBySupplierIds(idsList);
    }

    @Override
    public void deleteBySupplierIds(List<String> ids) {
        LambdaQueryWrapper<SupplierProjectMapping> wrapper = Wrappers.lambdaQuery(SupplierProjectMapping.class);
        wrapper.in(SupplierProjectMapping::getSupplierId, ids);
        remove(wrapper);
    }
}




