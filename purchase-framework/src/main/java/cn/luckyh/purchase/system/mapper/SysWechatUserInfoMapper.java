package cn.luckyh.purchase.system.mapper;

import java.util.List;

import cn.luckyh.purchase.system.domain.SysWechatUserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 微信用户账号管理Mapper接口
 *
 * @author purchase
 * @date 2021-04-25
 */
public interface SysWechatUserInfoMapper extends BaseMapper<SysWechatUserInfo> {
    /**
     * 查询微信用户账号管理
     *
     * @param id 微信用户账号管理ID
     * @return 微信用户账号管理
     */
    public SysWechatUserInfo selectSysWechatUserInfoById(String id);

    /**
     * 查询微信用户账号管理列表
     *
     * @param sysWechatUserInfo 微信用户账号管理
     * @return 微信用户账号管理集合
     */
    public List<SysWechatUserInfo> selectSysWechatUserInfoList(SysWechatUserInfo sysWechatUserInfo);

    /**
     * 新增微信用户账号管理
     *
     * @param sysWechatUserInfo 微信用户账号管理
     * @return 结果
     */
    public int insertSysWechatUserInfo(SysWechatUserInfo sysWechatUserInfo);

    /**
     * 修改微信用户账号管理
     *
     * @param sysWechatUserInfo 微信用户账号管理
     * @return 结果
     */
    public int updateSysWechatUserInfo(SysWechatUserInfo sysWechatUserInfo);

    /**
     * 删除微信用户账号管理
     *
     * @param id 微信用户账号管理ID
     * @return 结果
     */
    public int deleteSysWechatUserInfoById(String id);

    /**
     * 批量删除微信用户账号管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysWechatUserInfoByIds(String[] ids);
}
