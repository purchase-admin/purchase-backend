package cn.luckyh.purchase.contract.controller;

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
import cn.luckyh.purchase.contract.domain.Contract;
import cn.luckyh.purchase.contract.service.IContractService;
import cn.luckyh.purchase.common.utils.poi.ExcelUtil;
import cn.luckyh.purchase.common.core.page.TableDataInfo;

/**
 * 合同管理 Controller
 *
 * @author purchase
 * @date 2021-04-19
 */
@RestController
@RequestMapping("/contract/contract")
public class ContractController extends BaseController {
    @Autowired
    private IContractService contractService;

    /**
     * 查询合同管理 列表
     */
    //@PreAuthorize("@ss.hasPermi('contract:contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(Contract contract) {
        startPage();
        List<Contract> list = contractService.selectContractList(contract);
        return getDataTable(list);
    }

    /**
     * 导出合同管理 列表
     */
    //@PreAuthorize("@ss.hasPermi('contract:contract:export')")
    @Log(title = "合同管理 ", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public R export(Contract contract) {
        List<Contract> list = contractService.selectContractList(contract);
        ExcelUtil<Contract> util = new ExcelUtil<Contract>(Contract.class);
        return util.exportExcel(list, "contract");
    }

    /**
     * 获取合同管理 详细信息
     */
    //@PreAuthorize("@ss.hasPermi('contract:contract:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        return R.success(contractService.selectContractById(id));
    }

    /**
     * 新增合同管理
     */
    //@PreAuthorize("@ss.hasPermi('contract:contract:add')")
    @Log(title = "合同管理 ", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody Contract contract) {
        return toAjax(contractService.insertContract(contract));
    }

    /**
     * 修改合同管理
     */
    //@PreAuthorize("@ss.hasPermi('contract:contract:edit')")
    @Log(title = "合同管理 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody Contract contract) {
        return toAjax(contractService.updateContract(contract));
    }

    /**
     * 删除合同管理
     */
    //@PreAuthorize("@ss.hasPermi('contract:contract:remove')")
    @Log(title = "合同管理 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(contractService.deleteContractByIds(ids));
    }
}
