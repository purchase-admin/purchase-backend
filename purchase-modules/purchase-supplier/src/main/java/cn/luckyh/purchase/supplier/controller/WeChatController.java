package cn.luckyh.purchase.supplier.controller;

import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.domain.Result;
import cn.luckyh.purchase.common.core.domain.ResultUtil;
import cn.luckyh.purchase.common.core.domain.model.LoginUser;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.common.exception.CustomException;
import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.common.utils.StringUtils;
import cn.luckyh.purchase.execute.service.IInquiryPurchaseService;
import cn.luckyh.purchase.supplier.domain.Supplier;
import cn.luckyh.purchase.supplier.service.ISupplierService;
import cn.luckyh.purchase.supplier.service.IWeChatService;
import cn.luckyh.purchase.system.domain.SysFileStorage;
import cn.luckyh.purchase.system.domain.SysWechatUserInfo;
import cn.luckyh.purchase.system.service.SysFileStorageService;
import cn.luckyh.purchase.system.service.impl.SysWechatUserInfoServiceImpl;
import cn.luckyh.purchase.workflow.service.runtime.ProcessInstancesService;
import cn.luckyh.purchase.workflow.service.runtime.TaskActionService;
import cn.luckyh.purchase.workflow.vo.history.HistoryRecordResult;
import cn.luckyh.purchase.workflow.vo.runtime.CreateProcessInstanceRepresentation;
import cn.luckyh.purchase.workflow.vo.runtime.TaskCompleteRepresentation;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 微信小程序接口控制器
 */
@Slf4j
@Api(tags = "微信小程序接口控制器")
@RestController
@RequestMapping("/weChat")
public class WeChatController extends BaseController {

    @Autowired
    private ISupplierService supplierService;

    @Autowired
    private IWeChatService weChatService;

    @Autowired
    private IInquiryPurchaseService inquiryPurchaseService;

    @Autowired
    private ProcessInstancesService processInstancesService;

    @Autowired
    private TaskActionService taskActionService;

    @Autowired
    private SysWechatUserInfoServiceImpl wechatUserInfoService;

    @Autowired
    private HistoryService historyService;


    @Autowired
    private SysFileStorageService sysFileStorageService;

    /**
     * 微信登录
     */
    @PostMapping("/weChatLogin")
    public R weChatLogin(@RequestBody LoginUser loginUser, HttpServletRequest request) {
        System.out.println("********微信公众平台授权方法******");
        String code = loginUser.getCode();
        System.out.println("code值" + code);
        return weChatService.weChatLogin(code, request);
    }

    /**
     * 供应商注册
     */
    @Log(title = "供应商注册", businessType = BusinessType.INSERT)
    @PostMapping(value = "/register")
    @Transactional(rollbackFor = RuntimeException.class)
    public R register(Supplier supplier,
                      MultipartFile yyzzFile, MultipartFile khxkFile, MultipartFile sqwtFile, MultipartFile txzsFile, MultipartFile cpdlFile
    ) {
        if (StringUtil.isEmpty(supplier.getOpenId())) {
            return R.error("openId不能为空");
        }
        int result;
        Supplier byOpenId = supplierService.getByOpenId(supplier.getOpenId());
        if (Objects.isNull(byOpenId)) {
            saveOrUpdateFile(supplier, yyzzFile, khxkFile, sqwtFile, txzsFile, cpdlFile);
            byOpenId = new Supplier();
            BeanUtils.copyProperties(supplier, byOpenId, "id");
            result = supplierService.insertSupplier(byOpenId);
        } else {
            logger.debug("数据库原始数据:{}", byOpenId);
            saveOrUpdateFile(supplier, yyzzFile, khxkFile, sqwtFile, txzsFile, cpdlFile);
            logger.debug("after save:{}", byOpenId);
            logger.debug("更新数据:{}", supplier);
            result = supplierService.updateById(supplier) ? 1 : 0;
        }
        return result == 1 ? R.success("注册成功", supplier) : R.error("注册失败，请联系管理员");
    }

    /**
     * 保存或更新文件
     *
     * @param dto  供应商
     * @param yyzz 营业执照
     * @param khxk 开户银行
     * @param sqwt 授权委托书
     * @param txzs 体系证书
     * @param cpdl 产品代理
     */
    private void saveOrUpdateFile(Supplier dto, MultipartFile yyzz, MultipartFile khxk, MultipartFile sqwt, MultipartFile txzs, MultipartFile cpdl) {
        if (Objects.isNull(dto.getYyzz()) && Objects.isNull(yyzz)) {
            throw new RuntimeException("营业执照附件不可为空");
        }
        //有附件,替换操作
        if (Objects.nonNull(yyzz)) {
            logger.debug("更新yyzz");
            logger.debug("更新前:{}", dto.getYyzz());
            SysFileStorage storage = sysFileStorageService.storeFile(yyzz);
            dto.setYyzz(storage.getId());
            logger.debug("更新后:{}", dto.getYyzz());
        }
        if (Objects.nonNull(khxk)) {
            logger.debug("更新khxk");
            logger.debug("更新前:{}", dto.getKhxk());
            SysFileStorage storage = sysFileStorageService.storeFile(khxk);
            dto.setKhxk(storage.getId());
            logger.debug("更新后:{}", dto.getKhxk());
        }
        if (Objects.nonNull(sqwt)) {
            logger.debug("更新sqwt");
            SysFileStorage storage = sysFileStorageService.storeFile(sqwt);
            logger.debug("更新前:{}", dto.getSqwt());
            dto.setSqwt(storage.getId());
            logger.debug("更新后:{}", dto.getSqwt());
        }
        if (Objects.nonNull(txzs)) {
            logger.debug("更新txzs");
            logger.debug("更新前:{}", dto.getTxzs());
            SysFileStorage storage = sysFileStorageService.storeFile(txzs);
            dto.setTxzs(storage.getId());
            logger.debug("更新后:{}", dto.getTxzs());
        }
        if (Objects.nonNull(cpdl)) {
            logger.debug("更新cpdl");
            logger.debug("更新前:{}", dto.getCpdl());
            SysFileStorage storage = sysFileStorageService.storeFile(cpdl);
            dto.setCpdl(storage.getId());
            logger.debug("更新后:{}", dto.getCpdl());
        }
    }


    /**
     * 供应商修改
     */
    @Log(title = "供应商注册", businessType = BusinessType.INSERT)
    @PostMapping(value = "/update")
    @Transactional(rollbackFor = RuntimeException.class)
    public R update(@ModelAttribute Supplier supplier,
                    MultipartFile yyzz,
                    MultipartFile khxk,
                    MultipartFile sqwt,
                    MultipartFile txzs,
                    MultipartFile cpdl) {
        if (StringUtil.isEmpty(supplier.getId())) {
            return R.error("Id不能为空");
        }
        saveOrUpdateFile(supplier, yyzz, khxk, sqwt, txzs, cpdl);
        boolean result = supplierService.updateById(supplier);
        return result ? R.success("修改成功", supplier) : R.error("修改成功，请联系管理员");
    }

    /**
     * 查看注册审批记录
     */
    @GetMapping("/approvalRecords")
    public Result<HistoryRecordResult> approvalRecords(String openId) {
        return ResultUtil.data(weChatService.historyProcessRecordQuery(openId));
    }

    /**
     * 查看公开性的采购公告
     */
    @GetMapping("/notice")
    public TableDataInfo notice() {
        Supplier supplier = null;
        try {
            supplier = supplierService.getByCurrentUser();
        } catch (CustomException e) {
            System.out.println("未登录");
        }
        List<Map<String, Object>> result;
        if (supplier != null) {
            result = inquiryPurchaseService.selectOpenNoticeList(supplier.getId());
        } else {
            result = inquiryPurchaseService.selectOpenNoticeList("");
        }
        return getDataTable(result);
    }


    /**
     * 根据openId查询供应商数据
     */
    @GetMapping("/getById")
    public R getByOpenId(String openId) {
        if (StringUtils.isEmpty(openId)) {
            return R.error("openId不能为空");
        }
        Supplier byOpenId = supplierService.getByOpenId(openId);
        return R.success(byOpenId);
    }

    /**
     * 根据userName查询供应商数据
     * @param userName
     * @return
     */
    @GetMapping("/getByUserName")
    public R getByUserName(String userName){
        if (StringUtils.isEmpty(userName)) {
            return R.error("userName不能为空");
        }
        Supplier supplierByUserName = supplierService.getSupplierByUserName(userName);
        return R.success(supplierByUserName);
    }

    /**
     * 解除当前用户的微信账号绑定
     * @return
     */
    @GetMapping("/unbind")
    @Transactional(rollbackFor = Exception.class)
    public R unbind(){
        String loginName = SecurityUtils.getUsername();
        SysWechatUserInfo wechatUserInfo =wechatUserInfoService.selectSysWechatUserInfoByLoginName(loginName);
        if(wechatUserInfo==null){
            return R.error("此账号尚未绑定微信");
        }
       // wechatUserInfo.setRelationLoginName("");
        int i = wechatUserInfoService.deleteSysWechatUserInfoById(wechatUserInfo.getId());
        Supplier supplierByUserName = supplierService.getSupplierByUserName(loginName);
        supplierByUserName.setOpenId("");
        boolean c = supplierService.updateById(supplierByUserName);
        return i==1&&c?R.success("解绑成功"):R.error("解绑失败，请联系管理员");
    }

    @ApiOperation(value = "启动流程")
    @PostMapping("/run/start")
    public R start(@RequestBody CreateProcessInstanceRepresentation dto) {
        if (StringUtils.isEmpty(dto.getBusinessKey())) {
            return R.error("openId不能为空");
        }
        String code = dto.getBusinessKey();
        Supplier supplier = supplierService.getByOpenId(code);
        if (supplier == null) {
            log.error("根据openid查找供应商信息失败,openid为{}", code);
            throw new RuntimeException("未找到填写的注册信息");
        }


        validRunningProcess(dto.getProcessDefinitionKey(), supplier.getId());
        String id = supplier.getId();
        dto.setBusinessKey(id);
        String s = processInstancesService.startNewProcessInstance(dto, supplier.getCompanyName());
        //将流程实例ID记录到供应商信息中
        boolean result = wechatUserInfoService.updateProcInstId(code, s);
        if (!result) {
            R.error("流程发起失败", s);
        }
        return R.success("流程发起成功", s);
    }

    /**
     * // 验证当前供应商是否已有在途的注册流程
     *
     * @param processDefinitionKey
     * @param id
     */
    private void validRunningProcess(String processDefinitionKey, String id) {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().unfinished().processDefinitionKey(processDefinitionKey).processInstanceBusinessKey(id).list();
        if (!list.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            String collect = list.stream().map(HistoricProcessInstance::getId).collect(Collectors.joining(StrUtil.COMMA));
            log.error("当前供应商已有在途注册流程,流程实例ID为{}", collect);
            throw new RuntimeException("当前已有在途申请");
        }
    }

    @ApiOperation(value = "流程提交")
    @PostMapping("/run/tasks/{taskId}/submit")
    public R submit(@PathVariable @ApiParam(value = "任务id", name = "taskId") String taskId,
                    @RequestBody TaskCompleteRepresentation completeRepresentation) {
        taskActionService.submit(taskId, completeRepresentation);
        return R.success();
    }
}

