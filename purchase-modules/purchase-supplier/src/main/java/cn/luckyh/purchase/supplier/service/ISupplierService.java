package cn.luckyh.purchase.supplier.service;

import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.execute.domain.InquiryPurchase;
import cn.luckyh.purchase.supplier.domain.Supplier;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 供应商信息 供应商数据Service接口
 *
 * @author purchase
 * @date 2021-03-23
 */
public interface ISupplierService extends IService<Supplier> {
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

    /**
     * 新增供应商信息 供应商数据
     *
     * @param supplier 供应商信息 供应商数据
     * @return 结果
     */
    public int insertSupplier(Supplier supplier);

    /**
     * 修改供应商信息 供应商数据
     *
     * @param supplier 供应商信息 供应商数据
     * @return 结果
     */
    public int updateSupplier(Supplier supplier);

    /**
     * 批量删除供应商信息 供应商数据
     *
     * @param ids 需要删除的供应商信息 供应商数据ID
     * @return 结果
     */
    public int deleteSupplierByIds(String[] ids);



    List<Map<String, Object>> selectSupplierJoinList(Supplier supplier);

    Supplier getByOpenId(String openId);

    /**
     * 根据当前登录人获取关联的供应商信息
     *
     * @return
     */
    Supplier getByCurrentUser();


    Supplier getSupplierByUserName(String username);

    Map<String, Object> getNoticeInfo(String projectId);

    int joinProject(InquiryPurchase inquiryPurchase);

    List<Supplier> getRelationSupplierByTask(String type, String name,String projectTypeId, String taskId);

    R importExcel(MultipartFile file);
}
