package cn.luckyh.purchase.system.controller;

import java.util.List;

import cn.luckyh.purchase.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.system.domain.SysWechatUserInfo;
import cn.luckyh.purchase.system.service.ISysWechatUserInfoService;
import cn.luckyh.purchase.common.utils.poi.ExcelUtil;
import cn.luckyh.purchase.common.core.page.TableDataInfo;

/**
 * 微信用户账号管理Controller
 *
 * @author purchase
 * @date 2021-04-25
 */
@RestController
@RequestMapping("/wechatUser/info")
public class SysWechatUserInfoController extends BaseController {
    @Autowired
    private ISysWechatUserInfoService sysWechatUserInfoService;

    /**
     * 查询微信用户账号管理列表
     */
    //@PreAuthorize("@ss.hasPermi('wechatUser:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysWechatUserInfo sysWechatUserInfo) {
        startPage();
        List<SysWechatUserInfo> list = sysWechatUserInfoService.selectSysWechatUserInfoList(sysWechatUserInfo);
        return getDataTable(list);
    }

    /**
     * 导出微信用户账号管理列表
     */
    //@PreAuthorize("@ss.hasPermi('wechatUser:info:export')")
    @Log(title = "微信用户账号管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public R export(SysWechatUserInfo sysWechatUserInfo) {
        List<SysWechatUserInfo> list = sysWechatUserInfoService.selectSysWechatUserInfoList(sysWechatUserInfo);
        ExcelUtil<SysWechatUserInfo> util = new ExcelUtil<SysWechatUserInfo>(SysWechatUserInfo.class);
        return util.exportExcel(list, "info");
    }

    /**
     * 获取微信用户账号管理详细信息
     */
    //@PreAuthorize("@ss.hasPermi('wechatUser:info:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        return R.success(sysWechatUserInfoService.selectSysWechatUserInfoById(id));
    }

    /**
     * 新增微信用户账号管理
     */
    //@PreAuthorize("@ss.hasPermi('wechatUser:info:add')")
    @Log(title = "微信用户账号管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody SysWechatUserInfo sysWechatUserInfo) {
        return toAjax(sysWechatUserInfoService.insertSysWechatUserInfo(sysWechatUserInfo));
    }

    /**
     * 修改微信用户账号管理
     */
    //@PreAuthorize("@ss.hasPermi('wechatUser:info:edit')")
    @Log(title = "微信用户账号管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody SysWechatUserInfo sysWechatUserInfo) {
        return toAjax(sysWechatUserInfoService.updateSysWechatUserInfo(sysWechatUserInfo));
    }

    /**
     * 删除微信用户账号管理
     */
    //@PreAuthorize("@ss.hasPermi('wechatUser:info:remove')")
    @Log(title = "微信用户账号管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(sysWechatUserInfoService.deleteSysWechatUserInfoByIds(ids));
    }
}
