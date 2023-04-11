package cn.luckyh.purchase.contract.mapper;

import java.util.List;

import cn.luckyh.purchase.contract.domain.Contract;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 合同管理 Mapper接口
 *
 * @author purchase
 * @date 2021-04-19
 */
public interface ContractMapper extends BaseMapper<Contract>{
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
     * 删除合同管理 
     *
     * @param id 合同管理 ID
     * @return 结果
     */
    public int deleteContractById(String id);

    /**
     * 批量删除合同管理 
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteContractByIds(String[] ids);
    }
