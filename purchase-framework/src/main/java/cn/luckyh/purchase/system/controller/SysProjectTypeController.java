package cn.luckyh.purchase.system.controller;

import cn.luckyh.purchase.common.annotation.Log;
import cn.luckyh.purchase.common.constant.UserConstants;
import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.enums.BusinessType;
import cn.luckyh.purchase.common.utils.SecurityUtils;
import cn.luckyh.purchase.common.utils.StringUtils;
import cn.luckyh.purchase.system.domain.SysProjectType;
import cn.luckyh.purchase.system.service.ISysProjectTypeService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;


/**
 * 项目分类信息
 */
@RestController
@RequestMapping("/system/projectType")
public class SysProjectTypeController extends BaseController {
    @Autowired
    private ISysProjectTypeService projectTypeService;

    /**
     * 获取项目分类列表
     */
    //@PreAuthorize("@ss.hasPermi('system:projectType:list')")
    @GetMapping("/list")
    public R list(SysProjectType projectType) {
        List<SysProjectType> projectTypes = projectTypeService.selectProjectTypeList(projectType);
        return R.success(projectTypes);
    }

    /**
     * 查询项目分类列表（排除节点）
     */
    //@PreAuthorize("@ss.hasPermi('system:projectType:list')")
    @GetMapping("/list/exclude/{projectTypeId}")
    public R excludeChild(@PathVariable(value = "projectTypeId", required = false) Long projectTypeId) {
        List<SysProjectType> projectTypes = projectTypeService.selectProjectTypeList(new SysProjectType());
        Iterator<SysProjectType> it = projectTypes.iterator();
        while (it.hasNext()) {
            SysProjectType d = (SysProjectType) it.next();
            if (d.getId().intValue() == projectTypeId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), projectTypeId + "")) {
                it.remove();
            }
        }
        return R.success(projectTypes);
    }

    /**
     * 根据项目分类编号获取详细信息
     */
    //@PreAuthorize("@ss.hasPermi('system:projectType:query')")
    @GetMapping(value = "/{projectTypeId}")
    public R getInfo(@PathVariable Long projectTypeId) {
        return R.success(projectTypeService.selectProjectTypeById(projectTypeId));
    }

    /**
     * 获取项目分类下拉树列表
     */
    @GetMapping("/treeselect")
    public R treeselect(SysProjectType projectType) {
        List<SysProjectType> projectTypes = projectTypeService.selectProjectTypeList(projectType);
        return R.success(projectTypeService.buildProjectTypeTreeSelect(projectTypes));
    }


    /**
     * 新增项目分类
     */
    //@PreAuthorize("@ss.hasPermi('system:projectType:add')")
    @Log(title = "项目分类管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@Validated @RequestBody SysProjectType projectType) {
        if (UserConstants.NOT_UNIQUE.equals(projectTypeService.checkProjectTypeNameUnique(projectType))) {
            return R.error("新增项目分类'" + projectType.getProjectTypeName() + "'失败，项目分类名称已存在");
        }
        projectType.setCreateBy(SecurityUtils.getUsername());
        return toAjax(projectTypeService.insertProjectType(projectType));
    }

    /**
     * 修改项目分类
     */
    //@PreAuthorize("@ss.hasPermi('system:projectType:edit')")
    @Log(title = "项目分类管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@Validated @RequestBody SysProjectType projectType) {
        if (UserConstants.NOT_UNIQUE.equals(projectTypeService.checkProjectTypeNameUnique(projectType))) {
            return R.error("修改项目分类'" + projectType.getProjectTypeName() + "'失败，项目分类名称已存在");
        } else if (projectType.getParentId().equals(projectType.getId())) {
            return R.error("修改项目分类'" + projectType.getProjectTypeName() + "'失败，上级项目分类不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, projectType.getStatus())
                && projectTypeService.selectNormalChildrenProjectTypeById(projectType.getId()) > 0) {
            return R.error("该项目分类包含未停用的子项目分类！");
        }
        projectType.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(projectTypeService.updateProjectType(projectType));
    }

    /**
     * 删除项目分类
     */
    //@PreAuthorize("@ss.hasPermi('system:projectType:remove')")
    @Log(title = "项目分类管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{projectTypeId}")
    public R remove(@PathVariable Long projectTypeId) {
        if (projectTypeService.hasChildByProjectTypeId(projectTypeId)) {
            return R.error("存在下级项目分类,不允许删除");
        }
        return toAjax(projectTypeService.deleteProjectTypeById(projectTypeId));
    }
}
