package cn.luckyh.purchase.system.controller;

import java.util.List;

import cn.luckyh.purchase.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.common.utils.poi.ExcelUtil;
import cn.luckyh.purchase.system.domain.SysOperLog;
import cn.luckyh.purchase.system.service.ISysOperLogService;

/**
 * 操作日志记录
 */
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {
    @Autowired
    private ISysOperLogService operLogService;

    //@PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog) {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    //@PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
    @GetMapping("/export")
    public R export(SysOperLog operLog) {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        return util.exportExcel(list, "操作日志");
    }

    //@PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public R remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    //@PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public R clean() {
        operLogService.cleanOperLog();
        return R.success();
    }
}
