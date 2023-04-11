package cn.luckyh.purchase.system.service;

import cn.luckyh.purchase.common.core.domain.TreeSelect;
import cn.luckyh.purchase.system.domain.SysProjectType;

import java.util.List;

/**
 * 项目分类管理 服务层
 */
public interface ISysProjectTypeService {
    /**
     * 查询项目分类管理数据
     *
     * @param projectType 项目分类信息
     * @return 项目分类信息集合
     */
    public List<SysProjectType> selectProjectTypeList(SysProjectType projectType);

    /**
     * 构建前端所需要树结构
     *
     * @param projectTypes 项目分类列表
     * @return 树结构列表
     */
    public List<SysProjectType> buildProjectTypeTree(List<SysProjectType> projectTypes);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param projectTypes 项目分类列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildProjectTypeTreeSelect(List<SysProjectType> projectTypes);

    /**
     * 根据项目分类ID查询信息
     *
     * @param projectTypeId 项目分类ID
     * @return 项目分类信息
     */
    public SysProjectType selectProjectTypeById(Long projectTypeId);

    /**
     * 根据ID查询所有子项目分类（正常状态）
     *
     * @param projectTypeId 项目分类ID
     * @return 子项目分类数
     */
    public int selectNormalChildrenProjectTypeById(Long projectTypeId);

    /**
     * 是否存在项目分类子节点
     *
     * @param projectTypeId 项目分类ID
     * @return 结果
     */
    public boolean hasChildByProjectTypeId(Long projectTypeId);



    /**
     * 校验项目分类名称是否唯一
     *
     * @param projectType 项目分类信息
     * @return 结果
     */
    public String checkProjectTypeNameUnique(SysProjectType projectType);

    /**
     * 新增保存项目分类信息
     *
     * @param projectType 项目分类信息
     * @return 结果
     */
    public int insertProjectType(SysProjectType projectType);

    /**
     * 修改保存项目分类信息
     *
     * @param projectType 项目分类信息
     * @return 结果
     */
    public int updateProjectType(SysProjectType projectType);

    /**
     * 删除项目分类管理信息
     *
     * @param projectTypeId 项目分类ID
     * @return 结果
     */
    public int deleteProjectTypeById(Long projectTypeId);

    String selectByName(String projectName);
}
