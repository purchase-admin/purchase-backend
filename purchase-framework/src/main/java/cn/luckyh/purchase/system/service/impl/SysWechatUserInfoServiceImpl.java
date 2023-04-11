package cn.luckyh.purchase.system.service.impl;

import cn.luckyh.purchase.common.utils.DateUtils;
import cn.luckyh.purchase.system.domain.SysWechatUserInfo;
import cn.luckyh.purchase.system.mapper.SysWechatUserInfoMapper;
import cn.luckyh.purchase.system.service.ISysWechatUserInfoService;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 微信用户账号管理Service业务层处理
 *
 * @author purchase
 * @date 2021-04-25
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class SysWechatUserInfoServiceImpl extends ServiceImpl<SysWechatUserInfoMapper, SysWechatUserInfo> implements ISysWechatUserInfoService {
    @Resource
    private SysWechatUserInfoMapper sysWechatUserInfoMapper;

    /**
     * 查询微信用户账号管理
     *
     * @param id 微信用户账号管理ID
     * @return 微信用户账号管理
     */
    @Override
    public SysWechatUserInfo selectSysWechatUserInfoById(String id) {
        return sysWechatUserInfoMapper.selectSysWechatUserInfoById(id);
    }

    /**
     * 查询微信用户账号管理列表
     *
     * @param sysWechatUserInfo 微信用户账号管理
     * @return 微信用户账号管理
     */
    @Override
    public List<SysWechatUserInfo> selectSysWechatUserInfoList(SysWechatUserInfo sysWechatUserInfo) {
        return sysWechatUserInfoMapper.selectSysWechatUserInfoList(sysWechatUserInfo);
    }

    /**
     * 新增微信用户账号管理
     *
     * @param sysWechatUserInfo 微信用户账号管理
     * @return 结果
     */
    @Override
    public int insertSysWechatUserInfo(SysWechatUserInfo sysWechatUserInfo) {
        if (StringUtil.isEmpty(sysWechatUserInfo.getId())) {
            sysWechatUserInfo.setId(IdUtil.simpleUUID());
        }
        sysWechatUserInfo.setCreateTime(DateUtils.getNowDate());
        return sysWechatUserInfoMapper.insertSysWechatUserInfo(sysWechatUserInfo);
    }

    /**
     * 修改微信用户账号管理
     *
     * @param sysWechatUserInfo 微信用户账号管理
     * @return 结果
     */
    @Override
    public int updateSysWechatUserInfo(SysWechatUserInfo sysWechatUserInfo) {
        sysWechatUserInfo.setUpdateTime(DateUtils.getNowDate());
        return sysWechatUserInfoMapper.updateSysWechatUserInfo(sysWechatUserInfo);
    }

    /**
     * 批量删除微信用户账号管理
     *
     * @param ids 需要删除的微信用户账号管理ID
     * @return 结果
     */
    @Override
    public int deleteSysWechatUserInfoByIds(String[] ids) {
        return sysWechatUserInfoMapper.deleteSysWechatUserInfoByIds(ids);
    }

    /**
     * 删除微信用户账号管理信息
     *
     * @param id 微信用户账号管理ID
     * @return 结果
     */
    @Override
    public int deleteSysWechatUserInfoById(String id) {
        return sysWechatUserInfoMapper.deleteSysWechatUserInfoById(id);
    }

    @Override
    public SysWechatUserInfo selectSysWechatUserInfoByOpenId(String openId) {
        LambdaQueryWrapper<SysWechatUserInfo> wrapper = Wrappers.lambdaQuery(SysWechatUserInfo.class);
        wrapper.eq(SysWechatUserInfo::getWechatLoginName, openId);
        List<SysWechatUserInfo> list = list(wrapper);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() != 1) {
            log.warn("当前openId可能关联多条数据");
//            throw new RuntimeException("openid关联用户数据异常");
        }
        return list.get(0);
    }

    @Override
    public boolean updateProcInstId(String openId, String procInstId) {
        int result = 0;
        SysWechatUserInfo userInfo = new SysWechatUserInfo();
        userInfo.setWechatLoginName(openId);
        List<SysWechatUserInfo> sysWechatUserInfos = selectSysWechatUserInfoList(userInfo);
        if (sysWechatUserInfos.size() == 1) {
            SysWechatUserInfo userInfo1 = sysWechatUserInfos.get(0);
            userInfo1.setProcInstId(procInstId);
            result = updateSysWechatUserInfo(userInfo1);
        }
        return result == 1;
    }

    @Override
    public SysWechatUserInfo selectSysWechatUserInfoByLoginName(String loginName) {
        LambdaQueryWrapper<SysWechatUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysWechatUserInfo::getRelationLoginName,loginName);
        return getOne(queryWrapper);
    }
}
