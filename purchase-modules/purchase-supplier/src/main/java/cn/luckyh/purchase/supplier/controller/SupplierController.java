package cn.luckyh.purchase.supplier.controller;

import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.config.PurchaseConfig;
import cn.luckyh.purchase.common.constant.Constants;
import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.common.exception.CustomException;
import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.common.utils.StringUtils;
import cn.luckyh.purchase.common.utils.file.FileUtils;
import cn.luckyh.purchase.execute.domain.InquiryPurchase;
import cn.luckyh.purchase.execute.service.impl.InquiryPurchaseServiceImpl;
import cn.luckyh.purchase.supplier.domain.Supplier;
import cn.luckyh.purchase.supplier.service.ISupplierService;
import cn.luckyh.purchase.system.service.SysFileStorageService;
import cn.luckyh.purchase.system.service.impl.SysUserServiceImpl;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 供应商信息 供应商数据Controller
 *
 * @author purchase
 * @date 2021-03-23
 */
@RestController
@RequestMapping("/supplier/supplier")
@Slf4j
public class SupplierController extends BaseController {
    @Autowired
    private ISupplierService supplierService;

    @Autowired
    private SysUserServiceImpl userService;

    @Autowired
    private InquiryPurchaseServiceImpl inquiryPurchaseService;

    @Autowired
    private SysFileStorageService sysFileStorageService;

    /**
     * 查询供应商信息 供应商数据列表
     */
    //@PreAuthorize("@ss.hasPermi('supplier:supplier:list')")
    @GetMapping("/list")
    public TableDataInfo list(Supplier supplier) {
        startPage();
        List<Supplier> list = supplierService.selectSupplierList(supplier);
        return getDataTable(list);
    }

    /**
     * GET /supplier/supplier/relation-supplier -> 查询关联供应商
     */
    @GetMapping("/relation-supplier")
    @ApiOperation(value = "查询关联供应商")
    public TableDataInfo relationSupplierList(String type, String name, String projectTypeId,String taskId) {
        startPage();
        List<Supplier> list = supplierService.getRelationSupplierByTask(type,name,projectTypeId,taskId);
        return getDataTable(list);
    }


    /**
     * 获取供应商信息 供应商数据详细信息
     */
    //@PreAuthorize("@ss.hasPermi('supplier:supplier:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        return R.success(supplierService.selectSupplierById(id));
    }

    /**
     * 新增供应商信息 供应商数据
     */
    //@PreAuthorize("@ss.hasPermi('supplier:supplier:add')")
    @Log(title = "供应商信息 供应商数据", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody Supplier supplier) {
        return toAjax(supplierService.insertSupplier(supplier));
    }

    /**
     * 修改供应商信息 供应商数据
     */
    //@PreAuthorize("@ss.hasPermi('supplier:supplier:edit')")
    @Log(title = "供应商信息 供应商数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody Supplier supplier) {
        if (StringUtil.isNotEmpty(supplier.getLoginName())) {
            SysUser user = userService.selectUserByUserName(supplier.getLoginName());
            if (user == null) {
                return R.error("用户名为 " + supplier.getLoginName() + "的用户不存在");
            }
            //将用户设置为供应商用户
            user.setUserType("01");
            userService.updateUser(user);
        }
        return toAjax(supplierService.updateSupplier(supplier));
    }

    /**
     * 删除供应商信息 供应商数据
     */
    //@PreAuthorize("@ss.hasPermi('supplier:supplier:remove')")
    @Log(title = "供应商信息 供应商数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(supplierService.deleteSupplierByIds(ids));
    }

    /**
     * 供应商查看自己的历史报价
     */
    @GetMapping("/joinList")
    public TableDataInfo joinList(String supplierId) {
        Supplier supplier;
        if(StrUtil.isEmpty(supplierId)){
            supplier = supplierService.getByCurrentUser();
        }else{
            supplier = supplierService.getById(supplierId);
        }
        startPage();
        List<Map<String, Object>> list = supplierService.selectSupplierJoinList(supplier);
        return getDataTable(list);
    }

    /**
     * 根据项目ID查询采购公告详情
     *
     * @return
     */
    @GetMapping("/getNoticeInfo")
    public R getNoticeInfo(String projectId) {
        return R.success(supplierService.getNoticeInfo(projectId));
    }

    /**
     * 公开招标参与报价
     */
//    //@PreAuthorize("@ss.hasPermi('inquiryPurchase:purchase:add')")
    @Log(title = "询价采购 ", businessType = BusinessType.INSERT)
    @PostMapping("/joinProject")
    public R add(@RequestBody InquiryPurchase inquiryPurchase) {
        if (inquiryPurchase != null && inquiryPurchase.getTaskId() == null) {
            return R.error("taskId不能为空");
        }
        return toAjax(supplierService.joinProject(inquiryPurchase));
    }


    /**
     * 报价
     *
     * @param taskId
     * @param quotation
     * @param bjfj
     * @param swfj
     * @param jsfj
     * @return
     */
    @PostMapping("/quotation")
    public R weChatQuotation(String taskId,
                             BigDecimal quotation,
                             String remark,
                             MultipartFile bjfj,
                             MultipartFile swfj,
                             MultipartFile jsfj
    ) {
        if (StringUtil.isEmpty(taskId) || quotation == null) {
            return R.error("taskId、quotation不能为空");
        }
        String loginName = SecurityUtils.getUsername();
        Supplier supplier = supplierService.getSupplierByUserName(loginName);
        if (Objects.isNull(supplier)) {
            throw new RuntimeException("供应商不存在");
        }
        InquiryPurchase inquiryPurchase = inquiryPurchaseService.getOnlyOne(taskId, supplier.getId());
        //保存附件
        if (Objects.nonNull(bjfj)) {
            inquiryPurchase.setBjfj(sysFileStorageService.storeFile(bjfj).getId());
        }
        if (Objects.nonNull(swfj)) {
            inquiryPurchase.setSwfj(sysFileStorageService.storeFile(swfj).getId());
        }
        if (Objects.nonNull(jsfj)) {
            inquiryPurchase.setJsfj(sysFileStorageService.storeFile(jsfj).getId());
        }
        if (StrUtil.isNotBlank(remark)) {
            inquiryPurchase.setRemark(remark);
        }
        inquiryPurchase.setQuotation(quotation);
        inquiryPurchaseService.updateById(inquiryPurchase);
        return R.success();
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
     * 批量导入供应商数据
     * @param file
     * @return
     */
    @Log(title = "批量导入供应商数据 ", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    public R importExcel(@RequestParam(value = "file") MultipartFile file) {
        return supplierService.importExcel(file);
    }

    /**
     * 下载
     */
    @GetMapping("/download")
    public void resourceDownload(HttpServletRequest request, HttpServletResponse response){
        String resource = "/profile/template/供应商导入模板.xls";
        try {
            if (!FileUtils.checkAllowDownload(resource)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = PurchaseConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }
}
