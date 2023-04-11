package cn.luckyh.purchase.system.service.impl;

import cn.luckyh.purchase.common.constant.UserConstants;
import cn.luckyh.purchase.common.core.domain.TreeSelect;
import cn.luckyh.purchase.common.exception.CustomException;
import cn.luckyh.purchase.common.utils.StringUtils;
import cn.luckyh.purchase.system.domain.SysProjectType;
import cn.luckyh.purchase.system.mapper.SysProjectTypeMapper;
import cn.luckyh.purchase.system.service.ISysProjectTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目分类管理 服务实现
 */
@Service
public class SysProjectTypeServiceImpl implements ISysProjectTypeService {
    @Resource
    private SysProjectTypeMapper projectTypeMapper;



    /**
     * 查询项目分类管理数据
     *
     * @param projectType 项目分类信息
     * @return 项目分类信息集合
     */
    @Override
//    @DataScope(projectTypeAlias = "d")
    public List<SysProjectType> selectProjectTypeList(SysProjectType projectType) {
        return projectTypeMapper.selectProjectTypeList(projectType);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param projectTypes 项目分类列表
     * @return 树结构列表
     */
    @Override
    public List<SysProjectType> buildProjectTypeTree(List<SysProjectType> projectTypes) {
        List<SysProjectType> returnList = new ArrayList<SysProjectType>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysProjectType projectType : projectTypes) {
            tempList.add(projectType.getId());
        }
        for (Iterator<SysProjectType> iterator = projectTypes.iterator(); iterator.hasNext(); ) {
            SysProjectType projectType = (SysProjectType) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(projectType.getParentId())) {
                recursionFn(projectTypes, projectType);
                returnList.add(projectType);
            }
        }
        if (returnList.isEmpty()) {
            returnList = projectTypes;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param projectTypes 项目分类列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildProjectTypeTreeSelect(List<SysProjectType> projectTypes) {
        List<SysProjectType> projectTypeTrees = buildProjectTypeTree(projectTypes);
        return projectTypeTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }


    /**
     * 根据项目分类ID查询信息
     *
     * @param projectTypeId 项目分类ID
     * @return 项目分类信息
     */
    @Override
    public SysProjectType selectProjectTypeById(Long projectTypeId) {
        return projectTypeMapper.selectProjectTypeById(projectTypeId);
    }

    /**
     * 根据ID查询所有子项目分类（正常状态）
     *
     * @param projectTypeId 项目分类ID
     * @return 子项目分类数
     */
    @Override
    public int selectNormalChildrenProjectTypeById(Long projectTypeId) {
        return projectTypeMapper.selectNormalChildrenProjectTypeById(projectTypeId);
    }

    /**
     * 是否存在子节点
     *
     * @param projectTypeId 项目分类ID
     * @return 结果
     */
    @Override
    public boolean hasChildByProjectTypeId(Long projectTypeId) {
        int result = projectTypeMapper.hasChildByProjectTypeId(projectTypeId);
        return result > 0 ? true : false;
    }

    /**
     * 校验项目分类名称是否唯一
     *
     * @param projectType 项目分类信息
     * @return 结果
     */
    @Override
    public String checkProjectTypeNameUnique(SysProjectType projectType) {
        Long projectTypeId = StringUtils.isNull(projectType.getId()) ? -1L : projectType.getId();
        SysProjectType info = projectTypeMapper.checkProjectTypeNameUnique(projectType.getProjectTypeName(), projectType.getParentId());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != projectTypeId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增保存项目分类信息
     *
     * @param projectType 项目分类信息
     * @return 结果
     */
    @Override
    public int insertProjectType(SysProjectType projectType) {
        SysProjectType info = projectTypeMapper.selectProjectTypeById(projectType.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new CustomException("项目分类停用，不允许新增");
        }
        projectType.setAncestors(info.getAncestors() + "," + projectType.getParentId());
        return projectTypeMapper.insertProjectType(projectType);
    }

    /**
     * 修改保存项目分类信息
     *
     * @param projectType 项目分类信息
     * @return 结果
     */
    @Override
    public int updateProjectType(SysProjectType projectType) {
        SysProjectType newParentProjectType = projectTypeMapper.selectProjectTypeById(projectType.getParentId());
        SysProjectType oldProjectType = projectTypeMapper.selectProjectTypeById(projectType.getId());
        if (StringUtils.isNotNull(newParentProjectType) && StringUtils.isNotNull(oldProjectType)) {
            String newAncestors = newParentProjectType.getAncestors() + "," + newParentProjectType.getId();
            String oldAncestors = oldProjectType.getAncestors();
            projectType.setAncestors(newAncestors);
            updateProjectTypeChildren(projectType.getId(), newAncestors, oldAncestors);
        }
        int result = projectTypeMapper.updateProjectType(projectType);
        if (UserConstants.DEPT_NORMAL.equals(projectType.getStatus())) {
            // 如果该项目分类是启用状态，则启用该项目分类的所有上级项目分类
            updateParentProjectTypeStatus(projectType);
        }
        return result;
    }

    /**
     * 修改该项目分类的父级项目分类状态
     *
     * @param projectType 当前项目分类
     */
    private void updateParentProjectTypeStatus(SysProjectType projectType) {
        String updateBy = projectType.getUpdateBy();
        projectType = projectTypeMapper.selectProjectTypeById(projectType.getId());
        projectType.setUpdateBy(updateBy);
        projectTypeMapper.updateProjectTypeStatus(projectType);
    }

    /**
     * 修改子元素关系
     *
     * @param projectTypeId       被修改的项目分类ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateProjectTypeChildren(Long projectTypeId, String newAncestors, String oldAncestors) {
        List<SysProjectType> children = projectTypeMapper.selectChildrenProjectTypeById(projectTypeId);
        for (SysProjectType child : children) {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            projectTypeMapper.updateProjectTypeChildren(children);
        }
    }

    /**
     * 删除项目分类管理信息
     *
     * @param projectTypeId 项目分类ID
     * @return 结果
     */
    @Override
    public int deleteProjectTypeById(Long projectTypeId) {
        return projectTypeMapper.deleteProjectTypeById(projectTypeId);
    }

    @Override
    public String selectByName(String projectName) {
        return projectTypeMapper.selectIdByNameEq(projectName);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysProjectType> list, SysProjectType t) {
        // 得到子节点列表
        List<SysProjectType> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysProjectType tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysProjectType> getChildList(List<SysProjectType> list, SysProjectType t) {
        List<SysProjectType> tlist = new ArrayList<SysProjectType>();
        Iterator<SysProjectType> it = list.iterator();
        while (it.hasNext()) {
            SysProjectType n = (SysProjectType) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysProjectType> list, SysProjectType t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
