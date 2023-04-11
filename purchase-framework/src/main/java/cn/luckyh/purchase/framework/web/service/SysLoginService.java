package cn.luckyh.purchase.framework.web.service;

import cn.luckyh.purchase.common.config.PurchaseConfig;
import cn.luckyh.purchase.common.constant.Constants;
import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import cn.luckyh.purchase.common.core.domain.model.LoginUser;
import cn.luckyh.purchase.common.core.redis.RedisCache;
import cn.luckyh.purchase.common.exception.CustomException;
import cn.luckyh.purchase.common.exception.user.CaptchaException;
import cn.luckyh.purchase.common.exception.user.CaptchaExpireException;
import cn.luckyh.purchase.common.exception.user.UserPasswordNotMatchException;
import cn.luckyh.purchase.common.utils.MessageUtils;
import cn.luckyh.purchase.framework.manager.AsyncManager;
import cn.luckyh.purchase.framework.manager.factory.AsyncFactory;
import cn.luckyh.purchase.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 登录校验方法
 */
@Component
@Slf4j
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        LoginUser loginUser = this.doLogin(username,password,code,uuid);
        // 生成token
        return tokenService.createToken(loginUser);
    }

    public String WechatLogin(String username, String password, String code, String uuid) {
        LoginUser loginUser = this.doLogin(username,password,code,uuid);
        // 生成token
        SysUser user = userService.selectUserByUserName(username);
        if(!"01".equals(user.getUserType())){
            //如果不是供应商用户
            throw new RuntimeException("非供应商用户禁止登录");
        }
        return tokenService.createToken(loginUser);
    }

    public String getSysToken(HttpServletRequest request, String userName) {
        String sysToken;
        SysUser sysUser = userService.selectUserByUserName(userName);
        LoginUser loginUser = new LoginUser(sysUser, permissionService.getMenuPermission(sysUser));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sysToken = tokenService.createToken(principal);
        return sysToken;
    }

    private LoginUser doLogin(String username, String password, String code, String uuid){
        if (PurchaseConfig.isCaptchaEnabled()) {
            String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser;
    }
}
