package cn.luckyh.purchase.supplier.service.impl;

import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.domain.Result;
import cn.luckyh.purchase.common.core.domain.ResultUtil;
import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import cn.luckyh.purchase.common.utils.WeiXin.AccessToken;
import cn.luckyh.purchase.common.utils.WeiXin.WeiXinUtil;
import cn.luckyh.purchase.framework.web.service.SysLoginService;
import cn.luckyh.purchase.framework.web.service.SysPermissionService;
import cn.luckyh.purchase.framework.web.service.TokenService;
import cn.luckyh.purchase.supplier.domain.Supplier;
import cn.luckyh.purchase.supplier.domain.WeChatInfo;
import cn.luckyh.purchase.supplier.properties.WeChatProperties;
import cn.luckyh.purchase.supplier.service.ISupplierService;
import cn.luckyh.purchase.supplier.service.IWeChatService;
import cn.luckyh.purchase.supplier.vo.WeChatRepresentation;
import cn.luckyh.purchase.system.domain.SysWechatUserInfo;
import cn.luckyh.purchase.system.service.impl.SysUserServiceImpl;
import cn.luckyh.purchase.system.service.impl.SysWechatUserInfoServiceImpl;
import cn.luckyh.purchase.workflow.service.history.HistoryQueryService;
import cn.luckyh.purchase.workflow.vo.history.HistoryRecordResult;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class WeChatServiceImpl implements IWeChatService {

    @Autowired
    private SysWechatUserInfoServiceImpl wechatUserInfoService;

    @Autowired
    private SysUserServiceImpl userService;

    //Fixme: 修改Service 直接创建LoginUser    --Add By heng.wang 2021/05/19 17:33
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private HistoryQueryService historyQueryService;

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private ISupplierService supplierService;

    @Autowired
    private SysLoginService loginService;

    @Override
    public R weChatLogin(String code, HttpServletRequest request) {

        String corpid = WeChatInfo.corpId;
        String corpsecret = WeChatInfo.corpSecret;//正式系统
        if (StringUtil.isEmpty(code)) {
            return R.error("code值为空");
        }

        //获取微信用户唯一标识openId
        AccessToken accessToken = WeiXinUtil.getAccessToken(corpid, corpsecret, code);
        String wechatOpenId = accessToken.getOpenId();
        System.out.println("openId:" + wechatOpenId);
        if (StringUtil.isEmpty(wechatOpenId)) {
            return R.error("未查询到用户openId;错误代码：" + accessToken.getErrorCode());
        }
        HashMap<String, Object> resultMap = new HashMap();
        SysWechatUserInfo sysWechatUserInfo = wechatUserInfoService.selectSysWechatUserInfoByOpenId(wechatOpenId);
        //TODO: 模拟登陆,流程测完需要删除    --Add By heng.wang 2021/05/19 11:49
//        sysWechatUserInfo.setRelationLoginName("admin");

        //用户初次登陆小程序，创建微信用户信息
        if (sysWechatUserInfo == null) {
            SysWechatUserInfo newSysWechatUserInfo = new SysWechatUserInfo();
            newSysWechatUserInfo.setWechatLoginName(wechatOpenId);
            //并跳转供应商注册界面
            int result = wechatUserInfoService.insertSysWechatUserInfo(newSysWechatUserInfo);
            resultMap.put("status", "register");
            resultMap.put("openId", wechatOpenId);
            resultMap.put("msg", "供应商未注册,请先进行注册");
            return result == 1 ? R.success(resultMap) : R.error("系统异常");
        } else if (StringUtil.isEmpty(sysWechatUserInfo.getRelationLoginName())) {
            //微信用户openId已存在，但是关联用户账号不存在
            //则查询是否有关联的注册审批信息
            if (StringUtil.isEmpty(sysWechatUserInfo.getProcInstId())) {
                //没有注册审批信息，则跳转注册界面
                resultMap.put("status", "register");
                resultMap.put("openId", wechatOpenId);
                resultMap.put("msg", "供应商未注册,请先进行注册");
                return R.success(resultMap);
            } else {
                //有注册审批信息
                HistoryRecordResult result = historyQueryService.historyProcessRecordQuery(sysWechatUserInfo.getProcInstId());
                if (result.getIsFirst() == 0) {
                    resultMap.put("status", "approve");
                    resultMap.put("openId", wechatOpenId);
                    resultMap.put("msg", "您的注册信息正在审批中");
                    return R.success(resultMap);
                } else {
                    //若流程在第一步，则跳转审批修改界面
                    resultMap.put("status", "approveUpdate");
                    resultMap.put("openId", wechatOpenId);
                    resultMap.put("msg", "请继续完善您的信息并提交");
                    return R.success(resultMap);
                }

            }
        }
        //若用户存在关联账号，则进行系统登录授权
        SysUser user = userService.selectUserByUserName(sysWechatUserInfo.getRelationLoginName());

        // LoginUser loginUser = new LoginUser(user, permissionService.getMenuPermission(user));
        // UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        // authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // String token = tokenService.createToken(principal);
        String token = loginService.getSysToken(request, sysWechatUserInfo.getRelationLoginName());
        resultMap.put("user", user);
        resultMap.put("token", token);
        resultMap.put("status", "success");
        resultMap.put("openId", wechatOpenId);
        resultMap.put("msg", "登录成功");
        return R.success(resultMap);
    }


    /**
     * 未注册
     * 提交审核
     * 审核中
     * 待完善
     * 待分配账号
     * 注册完成
     *
     * @param code    微信 code 用于请求 Access-Token openId
     * @param request used to build request security info
     * @return
     */
    public Result<WeChatRepresentation> weChatLogin2(String code, HttpServletRequest request) {
        if (StringUtil.isEmpty(code)) {
            return ResultUtil.error("请求异常,缺少参数openId");
        }

        String openId = requestOpenId(code);
        WeChatRepresentation representation = new WeChatRepresentation();
        representation.setOpenId(openId);
        //1.用户是否存在
        checkSupplierExist(openId);
        Supplier supplier = supplierService.getByOpenId(openId);
        if (Objects.isNull(supplier)) {
            log.debug("当前openId未关联供应商信息");
            representation.setStatus(WeChatRepresentation.REGISTER);
            return ResultUtil.data(representation, "未找到用户信息,请注册");
        }

        //2.存在,是否审批通过状态
        if (!"1".equals(supplier.getIsApprove())) {
            //直接返回正确信息
            if (StringUtils.hasText(supplier.getLoginName())) {
                return checkSuccess(supplier, openId, request);
            } else {
                return ResultUtil.success("审批完成,等待分配账号");
            }

        } else {
            //审批状态
            //3.审批中
            //2.是否有在途审批
            if (StringUtils.hasText(supplier.getProcessInstanceId())) {
                HistoryRecordResult result = historyQueryService.historyProcessRecordQuery(supplier.getProcessInstanceId());
                if (result.getEndTime() != null) {
                    //在途
                    if (1 == result.getIsFirst()) {
                        //填报环节
                        representation.setStatus(WeChatRepresentation.APPROVE_UPDATE);
                        return ResultUtil.data(representation, "请完善信息提交");
                    } else {
                        representation.setStatus(WeChatRepresentation.APPROVE);
                        return ResultUtil.data(representation, "流程审批中");
                    }
                }
            }
        }


        return null;
        //3.


//        //用户初次登陆小程序，创建微信用户信息
//        if (sysWechatUserInfo == null) {
//            SysWechatUserInfo newSysWechatUserInfo = new SysWechatUserInfo();
//            newSysWechatUserInfo.setWechatLoginName(wechatOpenId);
//            //并跳转供应商注册界面
//            int result = wechatUserInfoService.insertSysWechatUserInfo(newSysWechatUserInfo);
//            resultMap.put("status", "register");
//            resultMap.put("openId", wechatOpenId);
//            resultMap.put("msg", "供应商未注册,请先进行注册");
//            return result == 1 ? R.success(resultMap) : R.error("系统异常");
//        } else if (StringUtil.isEmpty(sysWechatUserInfo.getRelationLoginName())) {
//            //微信用户openId已存在，但是关联用户账号不存在
//            //则查询是否有关联的注册审批信息
//            if (StringUtil.isEmpty(sysWechatUserInfo.getProcInstId())) {
//                //没有注册审批信息，则跳转注册界面
//                resultMap.put("status", "register");
//                resultMap.put("openId", wechatOpenId);
//                resultMap.put("msg", "供应商未注册,请先进行注册");
//                return R.success(resultMap);
//            } else {
//                //有注册审批信息
//                HistoryRecordResult result = historyQueryService.historyProcessRecordQuery(sysWechatUserInfo.getProcInstId());
//                if (result.getIsFirst() == 1) {
//                    resultMap.put("status", "approve");
//                    resultMap.put("openId", wechatOpenId);
//                    resultMap.put("msg", "您的注册信息正在审批中");
//                    return R.success(resultMap);
//                } else {
//                    //若流程在第一步，则跳转审批修改界面
//                    resultMap.put("status", "approveUpdate");
//                    resultMap.put("openId", wechatOpenId);
//                    resultMap.put("msg", "请继续完善您的信息并提交");
//                    return R.success(resultMap);
//                }
//            }
//        }
//        //若用户存在关联账号，则进行系统登录授权
//        SysUser user = userService.selectUserByUserName(sysWechatUserInfo.getRelationLoginName());
//
//        LoginUser loginUser = new LoginUser(user, permissionService.getMenuPermission(user));
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
//        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String token = tokenService.createToken(principal);
//
//        resultMap.put("user", user);
//        resultMap.put("token", token);
//        resultMap.put("status", "success");
//        resultMap.put("openId", wechatOpenId);
//        resultMap.put("msg", "登录成功");
//        return R.success(resultMap);
    }

    private Result<WeChatRepresentation> checkSuccess(Supplier supplier, String openId, HttpServletRequest request) {
        WeChatRepresentation representation = new WeChatRepresentation();
        representation.setStatus(WeChatRepresentation.SUCCESS);
        //        //若用户存在关联账号，则进行系统登录授权
        String sysToken = loginService.getSysToken(request
                , supplier.getLoginName());
/*        SysUser user = userService.selectUserByUserName(supplier.getLoginName());
        LoginUser loginUser = new LoginUser(user, permissionService.getMenuPermission(user));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = tokenService.createToken(principal);*/
        representation.setToken(sysToken);
        representation.setOpenId(openId);
        return ResultUtil.data(representation);
    }

    private void checkSupplierExist(String openId) {

    }

    private String requestOpenId(String code) {
        //获取AccessToken
        AccessToken accessToken = WeiXinUtil.getAccessToken(weChatProperties.getCorpId(), weChatProperties.getCorpSecret(), code);
        String wechatOpenId = accessToken.getOpenId();
        if (StringUtil.isEmpty(wechatOpenId)) {
            log.error("获取accessToken异常,异常代码:{}", accessToken.getErrorCode());
            throw new RuntimeException("微信登录异常,请联系管理员");
        }
        if (log.isDebugEnabled()) {
            log.debug("获取openId:{}", wechatOpenId);
        }
        return wechatOpenId;
    }

    @Override
    public HistoryRecordResult historyProcessRecordQuery(String openId) {
        SysWechatUserInfo userInfo = new SysWechatUserInfo();
        userInfo.setWechatLoginName(openId);
        List<SysWechatUserInfo> sysWechatUserInfos = wechatUserInfoService.selectSysWechatUserInfoList(userInfo);
        if (sysWechatUserInfos.size() == 1) {
            return historyQueryService.historyProcessRecordQuery(sysWechatUserInfos.get(0).getProcInstId());
        }
        return null;
    }
}
