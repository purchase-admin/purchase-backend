package cn.luckyh.purchase.execute.service.impl;

import cn.luckyh.purchase.execute.domain.AgencyPurchase;
import cn.luckyh.purchase.execute.mapper.AgencyPurchaseMapper;
import cn.luckyh.purchase.execute.service.IAgencyPurchaseService;
import cn.luckyh.purchase.execute.vo.AgencyPurchaseVo;
import cn.luckyh.purchase.task.domain.PurchaseTask;
import cn.luckyh.purchase.task.service.impl.PurchaseTaskServiceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 代理采购 Service业务层处理
 *
 * @author purchase
 * @date 2021-03-24
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AgencyPurchaseServiceImpl extends ServiceImpl<AgencyPurchaseMapper, AgencyPurchase> implements IAgencyPurchaseService {
    @Resource
    private AgencyPurchaseMapper agencyPurchaseMapper;

    @Autowired
    private PurchaseTaskServiceImpl taskService;

    /**
     * 查询代理采购
     *
     * @param id 代理采购 ID
     * @return 代理采购
     */
    @Override
    public AgencyPurchaseVo selectAgencyPurchaseById(String id) {
        return agencyPurchaseMapper.selectAgencyPurchaseById(id);
    }

    /**
     * 查询代理采购 列表
     *
     * @param agencyPurchase 代理采购
     * @return 代理采购
     */
    @Override
    public List<AgencyPurchase> selectAgencyPurchaseList(AgencyPurchase agencyPurchase) {
        return agencyPurchaseMapper.selectAgencyPurchaseList(agencyPurchase);
    }

    /**
     * 新增代理采购
     *
     * @param agencyPurchase 代理采购
     * @return 结果
     */
    @Override
    public int insertAgencyPurchase(AgencyPurchase agencyPurchase) {
        String taskId = agencyPurchase.getTaskId();
        int result = save(agencyPurchase)?1:0;
        if(StringUtil.isNotEmpty(agencyPurchase.getId())){
            if(StringUtil.isNotEmpty(taskId)){
                if(taskId.indexOf(",")!=-1){
                    for(String id: taskId.split(",")){
                        PurchaseTask pt = new PurchaseTask();
                        pt.setId(id);
                        pt.setAgencyId(agencyPurchase.getId());
                        taskService.updatePurchaseTask(pt);
                    }
                }else{
                    PurchaseTask pt = new PurchaseTask();
                    pt.setId(taskId);
                    pt.setAgencyId(agencyPurchase.getId());
                    taskService.updatePurchaseTask(pt);
                }
            }else{
                return 0;
            }
        }else{
            return 0;
        }
        return result;
    }



    /**
     * 修改代理采购
     *
     * @param agencyPurchase 代理采购
     * @return 结果
     */
    @Override
    public int updateAgencyPurchase(AgencyPurchase agencyPurchase) {
        return agencyPurchaseMapper.updateAgencyPurchase(agencyPurchase);
    }

    /**
     * 批量删除代理采购
     *
     * @param ids 需要删除的代理采购 ID
     * @return 结果
     */
    @Override
    public int deleteAgencyPurchaseByIds(String[] ids) {
        return agencyPurchaseMapper.deleteAgencyPurchaseByIds(ids);
    }

    /**
     * 删除代理采购 信息
     *
     * @param id 代理采购 ID
     * @return 结果
     */
    @Override
    public int deleteAgencyPurchaseById(String id) {
        return agencyPurchaseMapper.deleteAgencyPurchaseById(id);
    }
}
