package cn.luckyh.purchase.purchase.service.impl;

import cn.luckyh.purchase.common.constant.WorkflowConstants;
import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import cn.luckyh.purchase.common.exception.NotFoundException;
import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.purchase.domain.PurchaseApply;
import cn.luckyh.purchase.purchase.domain.PurchaseApplyProjects;
import cn.luckyh.purchase.purchase.mapper.PurchaseApplyMapper;
import cn.luckyh.purchase.purchase.service.IPurchaseApplyService;
import cn.luckyh.purchase.purchase.vo.PurchaseApplyListVo;
import cn.luckyh.purchase.purchase.vo.PurchaseApplyVo;
import cn.luckyh.purchase.workflow.service.runtime.ProcessInstancesService;
import cn.luckyh.purchase.workflow.vo.runtime.CreateProcessInstanceRepresentation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 采购申请 Service业务层处理
 *
 * @author zhouwenchao
 * @date 2021-03-20
 */

@Slf4j
@Service("PurchaseApplyService")
@Transactional(rollbackFor = RuntimeException.class)
public class PurchaseApplyServiceImpl extends ServiceImpl<PurchaseApplyMapper, PurchaseApply> implements IPurchaseApplyService {
    @Resource
    private PurchaseApplyMapper purchaseApplyMapper;

    @Autowired
    private PurchaseApplyProjectsServiceImpl purchaseApplyProjectsService;

    @Autowired
    private ProcessInstancesService processInstancesService;

    /**
     * 查询采购申请
     *
     * @param id 采购申请 ID
     * @return 采购申请
     */
    @Override
    public PurchaseApply selectPurchaseApplyById(String id) {
        return purchaseApplyMapper.selectPurchaseApplyById(id);
    }

    /**
     * 查询采购申请 列表
     *
     * @param purchaseApply 采购申请
     * @return 采购申请
     */
    @Override
    public List<PurchaseApplyListVo> selectPurchaseApplyList(PurchaseApply purchaseApply) {
        return purchaseApplyMapper.selectPurchaseApplyList(purchaseApply);
    }

    /**
     * 新增采购申请
     *
     * @return 结果
     */
    public int insertPurchaseApply(PurchaseApply purchaseApply) {
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        String title = String.format("%s(%s)发起的采购申请", currentUser.getNickName(), currentUser.getDept().getDeptName());
//        purchaseApply.setTitle(currentUser.getNickName() + currentUser.getDept().getDeptName()+"()于"
//                + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())
//                + "创建的采购申请");
        purchaseApply.setTitle(title);
        purchaseApply.setDeptId(currentUser.getDeptId().toString());
        purchaseApply.setUserId(currentUser.getUserName());
        purchaseApply.setUserPhone(currentUser.getPhonenumber());
        purchaseApply.setCreateBy(currentUser.getUserName());
        purchaseApply.setCreateTime(new Date());
        purchaseApply.setBudgetYear(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        //新建状态为草稿
        purchaseApply.setStatus(WorkflowConstants.DRAFT);
        return save(purchaseApply) ? 1 : 0;
    }

    /**
     * 批量保存采购申请以及批次信息
     *
     * @param purchaseApplys
     * @return
     */
    public boolean insertPurchaseApplys(List<PurchaseApply> purchaseApplys) {
        List<PurchaseApplyProjects> purchaseApplyProjectList = new ArrayList<>();
        if (purchaseApplys.size() > 0) {
            PurchaseApply purchaseApply = purchaseApplys.get(0);
            int result = purchaseApplyMapper.insert(purchaseApply);
            String relatedId;
            if (result == 1) {
                relatedId = purchaseApply.getId();
            } else {
                return false;
            }
            for (PurchaseApply p : purchaseApplys) {
                PurchaseApplyProjects purchaseApplyProjects = new PurchaseApplyProjects();
                purchaseApplyProjects.setApplyId(relatedId);
                purchaseApplyProjects.setNum(p.getNum().toString());
                purchaseApplyProjects.setBudgetCost(p.getBudgetMoney());
                purchaseApplyProjects.setDeliveryDate(p.getDeliveryDay().toString());
                purchaseApplyProjects.setReason(p.getReason());
                purchaseApplyProjects.setSpecification(p.getSpecification());
                purchaseApplyProjects.setSupplier(p.getSupplier());
                purchaseApplyProjects.setSupplierContactName(p.getSupplierUsername());
                purchaseApplyProjects.setSupplierContactPhone(p.getSupplierPhone());
                purchaseApplyProjects.setBudgetType(p.getBudgetType());
                purchaseApplyProjects.setProjectId(p.getProjectId());
                purchaseApplyProjects.setCreateTime(new Date());
                purchaseApplyProjects.setCreateBy(SecurityUtils.getUsername());
                purchaseApplyProjectList.add(purchaseApplyProjects);
            }
        }
        return purchaseApplyProjectsService.saveOrUpdateBatch(purchaseApplyProjectList);
    }

    /**
     * 修改采购申请
     *
     * @param purchaseApply 采购申请
     * @return 结果
     */
    @Override
    public int updatePurchaseApply(PurchaseApply purchaseApply) {
        return purchaseApplyMapper.updatePurchaseApply(purchaseApply);
    }

    /**
     * 批量删除采购申请
     *
     * @param ids 需要删除的采购申请 ID
     * @return 结果
     */
    @Override
    public int deletePurchaseApplyByIds(String[] ids) {
        //删除采购申请前，先删除采购项目
        LambdaQueryWrapper<PurchaseApplyProjects> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(PurchaseApplyProjects::getApplyId,ids);
        purchaseApplyProjectsService.remove(queryWrapper);
        return purchaseApplyMapper.deletePurchaseApplyByIds(ids);
    }

    /**
     * 删除采购申请 信息
     *
     * @param id 采购申请 ID
     * @return 结果
     */
    @Override
    public int deletePurchaseApplyById(String id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("apply_id", id);
        //Fixme: 删除流程信息    --Add By heng.wang 2021/05/26 18:36
        purchaseApplyProjectsService.remove(wrapper);//先删除该批次下的采购项目
        return purchaseApplyMapper.deletePurchaseApplyById(id);//再删除批次
    }

    @Override
    public List<PurchaseApplyVo> selectContractProjectList(PurchaseApplyVo vo) {
        return purchaseApplyMapper.selectContractProjectList(vo);
    }

    @Override
    public String startProcess(CreateProcessInstanceRepresentation representation) {

        //Fixme: projectType 状态值应在采购项目中设置    --Add By heng.wang 2021/05/26 18:38
        //Fixme: 提交的时候做内部数据校验?    --Add By heng.wang 2021/05/27 10:08

        PurchaseApply byId = getById(representation.getBusinessKey());
        if (Objects.isNull(byId)) {
            log.error("采购申请业务数据不存在:数据主键为:{}", representation.getBusinessKey());
            throw new NotFoundException("采购申请业务数据不存在");
        }
        HashMap<String, Object> hashMap = new HashMap();
        //是否需要总经理审批
        hashMap.put("projectType", "1");
        if (Objects.nonNull(representation.getVariables())) {
            hashMap.putAll(representation.getVariables());
        }
        representation.setVariables(hashMap);
        String s = processInstancesService.startNewProcessInstance(representation);
        byId.setProcId(s);
        byId.setStatus(WorkflowConstants.RUNNING);
        updateById(byId);
        return s;
    }


    /**
     * 采购申请流程 流程结束-执行监听器结束表达式
     * 生成采购任务
     * ${PurchaseApplyService.endProcess(execution,"9")}
     *
     * @param execution 执行实例
     * @param status    状态
     */
    @SuppressWarnings("unused")
    public void endProcess(DelegateExecution execution, String status) {
        String businessKey = execution.getProcessInstanceBusinessKey();
        PurchaseApply apply = getById(businessKey);
        if (Objects.isNull(apply)) {
            log.error("采购申请业务数据不存在:数据主键为:{},关联流程实例ID为{}", businessKey, execution.getProcessInstanceId());
            throw new NotFoundException("采购申请业务数据不存在");
        }
        apply.setStatus(status);
        apply.setIsOk("1");
        updateById(apply);
    }
}
