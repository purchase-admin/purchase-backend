package cn.luckyh.purchase.supplier.service.impl;

import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import cn.luckyh.purchase.common.exception.NotFoundException;
import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.common.utils.poi.ExcelUtil;
import cn.luckyh.purchase.execute.domain.InquiryPurchase;
import cn.luckyh.purchase.execute.service.impl.InquiryPurchaseServiceImpl;
import cn.luckyh.purchase.purchase.domain.PurchaseApplyProjects;
import cn.luckyh.purchase.purchase.service.IPurchaseApplyProjectsService;
import cn.luckyh.purchase.supplier.domain.Supplier;
import cn.luckyh.purchase.supplier.domain.SupplierProjectMapping;
import cn.luckyh.purchase.supplier.domain.SupplierProjectType;
import cn.luckyh.purchase.supplier.mapper.SupplierMapper;
import cn.luckyh.purchase.supplier.service.ISupplierProjectTypeService;
import cn.luckyh.purchase.supplier.service.ISupplierService;
import cn.luckyh.purchase.supplier.service.SupplierProjectMappingService;
import cn.luckyh.purchase.system.domain.SysWechatUserInfo;
import cn.luckyh.purchase.system.service.ISysProjectTypeService;
import cn.luckyh.purchase.system.service.ISysWechatUserInfoService;
import cn.luckyh.purchase.system.service.impl.SysUserServiceImpl;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 供应商信息 供应商数据Service业务层处理
 *
 * @author purchase
 * @date 2021-03-23
 */
@Service("SupplierServiceImpl")
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {
    @Resource
    private SupplierMapper supplierMapper;

    @Autowired
    private SysUserServiceImpl userService;

    @Autowired
    private InquiryPurchaseServiceImpl inquiryPurchaseService;

    @Autowired
    private ISupplierProjectTypeService supplierProjectTypeService;

    @Autowired
    private ISysWechatUserInfoService wechatUserInfoService;


    @Autowired
    private SupplierProjectMappingService supplierProjectMappingService;

    @Autowired
    private IPurchaseApplyProjectsService purchaseApplyProjectsService;


    @Autowired
    private ISysProjectTypeService sysProjectTypeService;

    /**
     * 查询供应商信息 供应商数据
     *
     * @param id 供应商信息 供应商数据ID
     * @return 供应商信息 供应商数据
     */
    @Override
    public Supplier selectSupplierById(String id) {
        Supplier supplier = supplierMapper.selectSupplierById(id);
        if (supplier == null) {
            throw new RuntimeException("供应商不存在");
        }
        //设置供应商关联的供货分类
        LambdaQueryWrapper<SupplierProjectType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SupplierProjectType::getSupplierId, id);
        List<SupplierProjectType> list = supplierProjectTypeService.list(wrapper);
        List<String> projectTypeIds = list.stream().map(SupplierProjectType::getProjectTypeId).collect(Collectors.toList());
        supplier.setProjectTypeIds(projectTypeIds);
        //设置关联账号的中文姓名
        if (StringUtil.isNotEmpty(supplier.getLoginName())) {
            String nickName = userService.selectUserByUserName(supplier.getLoginName()).getNickName();
            supplier.setNickName(nickName);
        }
        return supplier;
    }

    /**
     * 查询供应商信息 供应商数据列表
     *
     * @param supplier 供应商信息 供应商数据
     * @return 供应商信息 供应商数据
     */
    @Override
    public List<Supplier> selectSupplierList(Supplier supplier) {
        return supplierMapper.selectSupplierList(supplier);
    }

    /**
     * 新增供应商信息 供应商数据
     *
     * @param supplier 供应商信息 供应商数据
     * @return 结果
     */
    @Override
    public int insertSupplier(Supplier supplier) {
        if (StringUtil.isEmpty(supplier.getId())) {
            supplier.setId(IdUtil.simpleUUID());
        }
        if (!updateSupplierProjectType(supplier)) {//更新供应商关联的项目分类
            return 0;
        }
        if (StringUtil.isNotEmpty(supplier.getLoginName())) {//如果存在关联账号，则反向更新关联表中的关联账号
            if (!updateWeChatRelationTable(supplier)) {
                return 0;
            }
        }
        // 更新关联数据
        updateSupplierProjectMapping(supplier);
        return supplierMapper.insert(supplier);
    }

    private void updateSupplierProjectMapping(Supplier supplier) {
        String provideProduct = supplier.getProvideProduct();
        if (StrUtil.isEmpty(provideProduct)) {
            return;
        }
        List<SupplierProjectMapping> mappingList = new ArrayList<>();
        Arrays.stream(provideProduct.split("-")).map(s -> s.split(",")).forEach(split -> {
            String element = split[split.length - 1];
            SupplierProjectMapping mapping = new SupplierProjectMapping();
            mapping.setProjectId(element);
            mapping.setSupplierId(supplier.getId());
            mappingList.add(mapping);
        });

        supplierProjectMappingService.deleteBySupplierId(supplier.getId());
        supplierProjectMappingService.saveBatch(mappingList);
    }

    /**
     * 更新供应商关联的项目分类
     *
     * @param supplier
     * @return
     */
    private boolean updateSupplierProjectType(Supplier supplier) {
        if (supplier.getProjectTypeIds() != null && supplier.getProjectTypeIds().size() > 0) {
            String supplierId = supplier.getId();
            supplierProjectTypeService.removeBySupplier(supplierId);
            List<SupplierProjectType> list = new ArrayList<>();
            for (String projectTypeId : supplier.getProjectTypeIds()) {
                SupplierProjectType projectType = new SupplierProjectType();
                projectType.setSupplierId(supplierId);
                projectType.setProjectTypeId(projectTypeId);
                list.add(projectType);
            }
            return supplierProjectTypeService.saveBatch(list);
        }
        return true;
    }

    /**
     * 修改供应商信息 供应商数据
     *
     * @param supplier 供应商信息 供应商数据
     * @return 结果
     */
    @Override
    public int updateSupplier(Supplier supplier) {
        if (!updateSupplierProjectType(supplier)) {//更新供应商关联的项目分类
            return 0;
        }
        if (StringUtil.isNotEmpty(supplier.getLoginName())) {//如果存在关联账号，则反向更新关联表中的关联账号
            updateWeChatRelationTable(supplier);
        }
        updateSupplierProjectMapping(supplier);
        return supplierMapper.updateById(supplier);
    }

    /**
     * 更新供应商微信关联关系表 sys_wechat_user_info
     *
     * @param supplier
     */
    private boolean updateWeChatRelationTable(Supplier supplier) {
        //若供应商没有关联的openId，则为批量导入的供应商，所以不需要更新微信关联关系
        if(StrUtil.isEmpty(supplier.getOpenId())){
            return true;
        }
        SysWechatUserInfo wechatUserInfo = wechatUserInfoService.selectSysWechatUserInfoByOpenId(supplier.getOpenId());
        wechatUserInfo.setRelationLoginName(supplier.getLoginName());
        int result = wechatUserInfoService.updateSysWechatUserInfo(wechatUserInfo);
        return result == 1;
    }

    /**
     * 批量删除供应商信息 供应商数据
     *
     * @param ids 需要删除的供应商信息 供应商数据ID
     * @return 结果
     */
    @Override
    public int deleteSupplierByIds(String[] ids) {
        List<String> list = Arrays.asList(ids);
        if (list.isEmpty()) {
            return 0;
        }
        // 删除关联关系
        supplierProjectMappingService.deleteBySupplierIds(list);
        return supplierMapper.deleteBatchIds(list);
    }


    /**
     * 供应商查看自己的历史报价记录
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> selectSupplierJoinList(Supplier supplier) {
        return supplierMapper.selectSupplierJoinList(supplier);
    }

    @Override
    public Supplier getByOpenId(String openId) {

        List<Supplier> list = supplierMapper.listByOpenId(openId);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() != 1) {
            log.error("当前openId关联多个供应商信息:{}", list);
            throw new RuntimeException("供应商关联信息查询异常,请联系管理员处理");
        }
        return list.get(0);

//        QueryWrapper<Supplier> wrapper = new QueryWrapper();
//        wrapper.eq("open_id", openId);
//        Supplier supplier;
//        try {
//            supplier = getOne(wrapper);
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return supplier;
    }

    @Override
    public Supplier getByCurrentUser() {
        String loginName = SecurityUtils.getUsername();
        return this.getSupplierByUserName(loginName);
    }

    @Override
    public Supplier getSupplierByUserName(String username) {
        Supplier supplier = new Supplier();
        supplier.setLoginName(username);
        List<Supplier> suppliers = selectSupplierList(supplier);//通过当前登录人账号获取关联的供应商信息
        if (suppliers.size() == 1) {
            return suppliers.get(0);
        }
        return null;
    }

    /**
     * 根据项目ID获取采购任务详情
     *
     * @param projectId
     * @return
     */
    @Override
    public Map<String, Object> getNoticeInfo(String projectId) {
        Supplier supplier = getByCurrentUser();
        if (supplier != null) {
            return supplierMapper.getNoticeInfo(projectId, supplier.getId());
        }
        return null;
    }

    @Override
    public int joinProject(InquiryPurchase inquiryPurchase) {
        Supplier supplier = getByCurrentUser();
        inquiryPurchase.setSupplier(supplier.getId());
        inquiryPurchase.setSupplierName(supplier.getCompanyName());
        inquiryPurchase.setIsCheck("0");
        inquiryPurchase.setIsJoin("1");
        return inquiryPurchaseService.save(inquiryPurchase) ? 1 : 0;
    }

    @Override
    public List<Supplier> getRelationSupplierByTask(String type, String name, String projectTypeId, String taskId) {
        List<SupplierProjectMapping> list = new ArrayList<>();
        if (StrUtil.isNotEmpty(taskId)) {
            PurchaseApplyProjects byId = purchaseApplyProjectsService.getById(taskId);
            // PurchaseApplyProjectsVo purchaseApplyProjectsVo = purchaseApplyProjectsService.selectPurchaseApplyProjectsById(taskId);
            if (Objects.nonNull(byId)) {
                String projectName = byId.getProjectName();
                String projectId=sysProjectTypeService.selectByName(projectName);
                list = supplierProjectMappingService.lambdaQuery().eq(SupplierProjectMapping::getProjectId, projectId).list();
                list = list.stream().distinct().collect(Collectors.toList());
            }
        }

        List<String> relationSupplierIds = null;
        if (!list.isEmpty()) {
            relationSupplierIds = list.stream().map(SupplierProjectMapping::getSupplierId).collect(Collectors.toList());
        }
        if ("0".equals(type)) {
            type = null;
            // relationSupplierIds=null;
        }
        List<Supplier> suppliers ;
        List<String> projectTypeIds = new ArrayList<>();
        if(StrUtil.isNotEmpty(projectTypeId) ){
            if(projectTypeId.indexOf(",")!=-1){
                projectTypeIds = Arrays.asList(projectTypeId.split(","));
            }else if(projectTypeId.indexOf(",")==-1){
                projectTypeIds.add(projectTypeId);
            }
        }
        suppliers = supplierMapper.selectRelationSupplier(type, name, projectTypeIds, relationSupplierIds);
        return suppliers;
    }


    /**
     * 采购预算流程流程结束-执行监听器结束表达式
     * ${SupplierServiceImpl.endProcess(execution,"1")}
     *
     * @param execution 执行实例
     * @param status    状态
     */
    @SuppressWarnings("unused")
    public void endProcess(DelegateExecution execution, String status) {
        String businessKey = execution.getProcessInstanceBusinessKey();
        Supplier apply = getById(businessKey);
        if (Objects.isNull(apply)) {
            log.error("供应商注册申请业务数据不存在:数据主键为:{},关联流程实例ID为{}", businessKey, execution.getProcessInstanceId());
            throw new NotFoundException("供应商注册申请业务数据不存在");
        }
        apply.setIsApprove(status);
        updateById(apply);
        log.debug("供应商注册申请审批结束,修改流程状态");
    }

    @Transactional(rollbackFor = Exception.class)
    public R importExcel(MultipartFile file) {
        ExcelUtil<Supplier> util = new ExcelUtil<>(Supplier.class);
        List<Supplier> errList = new ArrayList<>();
        try {
            List<Supplier> list = util.importExcel(file.getInputStream());
            for (Supplier supplier : list) {
                //设置为已审批状态，这样就可以在界面填写关联的账号
                supplier.setIsApprove("1");
                if(checkSupplierExist(supplier)) {
                    if (!save(supplier)) {
                        errList.add(supplier);
                    }
                }
                if(StrUtil.isNotEmpty(supplier.getLoginName())){
                    SysUser user = userService.selectUserByLoginName(supplier.getLoginName());
                    user.setUserType("01");
                    userService.updateUser(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导入excle异常，请联系管理员");
        }
        if (errList.size() > 0) {
            return R.error("导入失败，请检查数据或联系管理员", errList);
        }
        return R.success("导入成功");
    }

    /**
     * 根据公司名称和法人代表查询供应商是否已存在(true:不存在;false:已存在)
     * @param supplier
     * @return
     */
    private boolean checkSupplierExist(Supplier supplier){
        LambdaQueryWrapper<Supplier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Supplier::getCompanyName,supplier.getCompanyName());
        queryWrapper.eq(Supplier::getCorporateRep,supplier.getCorporateRep());
        List<Supplier> list = list(queryWrapper);
        return list.size()==0;
    }
}
