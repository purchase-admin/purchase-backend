package cn.luckyh.purchase.system.service;

import cn.luckyh.purchase.system.domain.SysWechatUserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 微信用户账号管理Service接口
 *
 * @author purchase
 * @date 2021-04-25
 */
public interface ISysWechatUserInfoService extends IService<SysWechatUserInfo> {
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
     * 批量删除微信用户账号管理
     *
     * @param ids 需要删除的微信用户账号管理ID
     * @return 结果
     */
    public int deleteSysWechatUserInfoByIds(String[] ids);

    /**
     * 删除微信用户账号管理信息
     *
     * @param id 微信用户账号管理ID
     * @return 结果
     */
    public int deleteSysWechatUserInfoById(String id);

    SysWechatUserInfo selectSysWechatUserInfoByOpenId(String wechatLoginName);

    boolean updateProcInstId(String openId , String procInstId);

    SysWechatUserInfo selectSysWechatUserInfoByLoginName(String loginName);
}
