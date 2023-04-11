package cn.luckyh.purchase.weave.service;

import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import cn.luckyh.purchase.common.core.domain.model.LoginUser;
import cn.luckyh.purchase.framework.web.service.SysPermissionService;
import cn.luckyh.purchase.framework.web.service.TokenService;
import cn.luckyh.purchase.system.service.ISysUserService;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/08/25 0025 11:29
 */

@Slf4j
@Service
@Transactional
public class SSOService {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    private static final String SSO_VALID_TOKEN_URL = "https://cjrboa.com/api/ssoAction/valiLoginToken";
    private static final String SSO_VALID_SUCCESS = "1";
    private static final String SSO_VALID_FAILED = "0";

    public String ssoLogin(HttpServletRequest request, String user, String token) {
        // 1. check token
        // requestResult(user, token);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("user", user);
        paramMap.put("token", token);
        String post = HttpUtil.post(SSO_VALID_TOKEN_URL, paramMap);
        log.debug("HttpUtil返回结果集:**************{}************", post);

        if (SSO_VALID_FAILED.equals(post)) {
            throw new RuntimeException("请求SSO认证失败");
        }
        String sysToken = "";
        // 2. if true create token
        if (SSO_VALID_SUCCESS.equals(post)) {
            log.info("返回结果:成功");
            //若用户存在关联账号，则进行系统登录授权
            sysToken = getSysToken(request, user);
        }
        log.info("SSO认证通过,生成Token为:{}", sysToken);
        return sysToken;
    }

    private void requestResult(String user, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("user", user);
        map.add("token", token);
        HttpEntity<MultiValueMap<String, String>> restRequest = new HttpEntity<>(map, headers);
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        List<HttpMessageConverter<?>> list = new ArrayList<>();
        MappingJackson2HttpMessageConverter e = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        e.setSupportedMediaTypes(mediaTypes);
        list.add(e);
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.addAll(list);
        restTemplate.setMessageConverters(messageConverters);

        String s = restTemplate.postForObject(SSO_VALID_TOKEN_URL, restRequest, String.class);
        log.debug("返回结果集:**************{}************", s);
    }

    public String getSysToken(HttpServletRequest request, String userName) {
        String sysToken;
        SysUser sysUser = userService.selectUserByUserName(userName);
        if (Objects.isNull(sysUser)) {
            throw new RuntimeException("单点登录成功,但是未匹配到系统用户: " + userName);
        }
        LoginUser loginUser = new LoginUser(sysUser, permissionService.getMenuPermission(sysUser));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sysToken = tokenService.createToken(principal);
        return sysToken;
    }


    public void publishMessage(String message) {
        rabbitTemplate.convertAndSend("weaver", "", message);
    }
}
