package cn.luckyh.purchase.system.mapper;

import cn.luckyh.purchase.system.domain.SysProjectType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目分类管理 数据层
 */
public interface SysProjectTypeMapper {
    /**
     * 查询项目分类管理数据
     *
     * @param projectType 项目分类信息
     * @return 项目分类信息集合
     */
    public List<SysProjectType> selectProjectTypeList(SysProjectType projectType);

    /**
     * 根据角色ID查询项目分类树信息
     *
     * @param roleId            角色ID
     * @param projectTypeCheckStrictly 项目分类树选择项是否关联显示
     * @return 选中项目分类列表
     */
    public List<Integer> selectProjectTypeListByRoleId(@Param("roleId") Long roleId, @Param("projectTypeCheckStrictly") boolean projectTypeCheckStrictly);

    /**
     * 根据项目分类ID查询信息
     *
     * @param projectTypeId 项目分类ID
     * @return 项目分类信息
     */
    public SysProjectType selectProjectTypeById(Long projectTypeId);

    /**
     * 根据ID查询所有子项目分类
     *
     * @param projectTypeId 项目分类ID
     * @return 项目分类列表
     */
    public List<SysProjectType> selectChildrenProjectTypeById(Long projectTypeId);

    /**
     * 根据ID查询所有子项目分类（正常状态）
     *
     * @param projectTypeId 项目分类ID
     * @return 子项目分类数
     */
    public int selectNormalChildrenProjectTypeById(Long projectTypeId);

    /**
     * 是否存在子节点
     *
     * @param projectTypeId 项目分类ID
     * @return 结果
     */
    public int hasChildByProjectTypeId(Long projectTypeId);


    /**
     * 校验项目分类名称是否唯一
     *
     * @param projectTypeName 项目分类名称
     * @param parentId 父项目分类ID
     * @return 结果
     */
    public SysProjectType checkProjectTypeNameUnique(@Param("projectTypeName") String projectTypeName, @Param("parentId") Long parentId);

    /**
     * 新增项目分类信息
     *
     * @param projectType 项目分类信息
     * @return 结果
     */
    public int insertProjectType(SysProjectType projectType);

    /**
     * 修改项目分类信息
     *
     * @param projectType 项目分类信息
     * @return 结果
     */
    public int updateProjectType(SysProjectType projectType);

    /**
     * 修改所在项目分类的父级项目分类状态
     *
     * @param projectType 项目分类
     */
    public void updateProjectTypeStatus(SysProjectType projectType);

    /**
     * 修改子元素关系
     *
     * @param projectTypes 子元素
     * @return 结果
     */
    public int updateProjectTypeChildren(@Param("projectTypes") List<SysProjectType> projectTypes);

    /**
     * 删除项目分类管理信息
     *
     * @param projectTypeId 项目分类ID
     * @return 结果
     */
    public int deleteProjectTypeById(Long projectTypeId);

    String selectIdByNameEq(String projectName);

}
