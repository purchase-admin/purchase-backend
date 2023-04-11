package cn.luckyh.purchase.contract.service;

import java.util.List;

import cn.luckyh.purchase.contract.domain.Contract;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 合同管理 Service接口
 *
 * @author purchase
 * @date 2021-04-19
 */
public interface IContractService extends IService<Contract> {
    /**
     * 查询合同管理
     *
     * @param id 合同管理 ID
     * @return 合同管理
     */
    public Contract selectContractById(String id);

    /**
     * 查询合同管理 列表
     *
     * @param contract 合同管理
     * @return 合同管理 集合
     */
    public List<Contract> selectContractList(Contract contract);

    /**
     * 新增合同管理
     *
     * @param contract 合同管理
     * @return 结果
     */
    public int insertContract(Contract contract);

    /**
     * 修改合同管理
     *
     * @param contract 合同管理
     * @return 结果
     */
    public int updateContract(Contract contract);

    /**
     * 批量删除合同管理
     *
     * @param ids 需要删除的合同管理 ID
     * @return 结果
     */
    public int deleteContractByIds(String[] ids);

    /**
     * 删除合同管理 信息
     *
     * @param id 合同管理 ID
     * @return 结果
     */
    public int deleteContractById(String id);
}
