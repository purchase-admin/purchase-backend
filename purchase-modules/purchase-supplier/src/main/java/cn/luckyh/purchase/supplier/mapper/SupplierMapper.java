package cn.luckyh.purchase.supplier.mapper;

import cn.luckyh.purchase.supplier.domain.Supplier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 供应商信息 供应商数据Mapper接口
 *
 * @author purchase
 * @date 2021-03-23
 */
public interface SupplierMapper extends BaseMapper<Supplier> {
    /**
     * 查询供应商信息 供应商数据
     *
     * @param id 供应商信息 供应商数据ID
     * @return 供应商信息 供应商数据
     */
    public Supplier selectSupplierById(String id);

    /**
     * 查询供应商信息 供应商数据列表
     *
     * @param supplier 供应商信息 供应商数据
     * @return 供应商信息 供应商数据集合
     */
    public List<Supplier> selectSupplierList(Supplier supplier);

    public List<Supplier> selectRelationSupplier(@Param("type") String type, @Param("name") String name,
                                                 @Param("projectTypeId") Collection projectTypeId, @Param("ids") List<String> ids);

    /**
     * 删除供应商信息 供应商数据
     *
     * @param id 供应商信息 供应商数据ID
     * @return 结果
     */
    public int deleteSupplierById(String id);

    /**
     * 批量删除供应商信息 供应商数据
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSupplierByIds(String[] ids);

    /**
     * 根据供应商ID查询供应商报价列表
     *
     * @param supplier
     * @return
     */
    List<Map<String, Object>> selectSupplierJoinList(Supplier supplier);


    Map<String, Object> getNoticeInfo(@Param("projectId") String projectId, @Param("supplierId") String supplierId);

    List<Supplier> listByOpenId(@Param("openId") String openId);
}
