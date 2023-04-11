package cn.luckyh.purchase.contract.service.impl;

import java.util.List;

import cn.luckyh.purchase.common.utils.DateUtils;
import org.springframework.stereotype.Service;
import cn.luckyh.purchase.contract.mapper.ContractMapper;
import cn.luckyh.purchase.contract.domain.Contract;
import cn.luckyh.purchase.contract.service.IContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 合同管理 Service业务层处理
 *
 * @author purchase
 * @date 2021-04-19
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {
    @Resource
    private ContractMapper contractMapper;

    /**
     * 查询合同管理
     *
     * @param id 合同管理 ID
     * @return 合同管理
     */
    @Override
    public Contract selectContractById(String id) {
        return contractMapper.selectContractById(id);
    }

    /**
     * 查询合同管理 列表
     *
     * @param contract 合同管理
     * @return 合同管理
     */
    @Override
    public List<Contract> selectContractList(Contract contract) {
        return contractMapper.selectContractList(contract);
    }

    /**
     * 新增合同管理
     *
     * @param contract 合同管理
     * @return 结果
     */
    @Override
    public int insertContract(Contract contract) {
        contract.setCreateTime(DateUtils.getNowDate());
        return save(contract)?1:0;
    }

    /**
     * 修改合同管理
     *
     * @param contract 合同管理
     * @return 结果
     */
    @Override
    public int updateContract(Contract contract) {
        contract.setUpdateTime(DateUtils.getNowDate());
        return contractMapper.updateContract(contract);
    }

    /**
     * 批量删除合同管理
     *
     * @param ids 需要删除的合同管理 ID
     * @return 结果
     */
    @Override
    public int deleteContractByIds(String[] ids) {
        return contractMapper.deleteContractByIds(ids);
    }

    /**
     * 删除合同管理 信息
     *
     * @param id 合同管理 ID
     * @return 结果
     */
    @Override
    public int deleteContractById(String id) {
        return contractMapper.deleteContractById(id);
    }
}
