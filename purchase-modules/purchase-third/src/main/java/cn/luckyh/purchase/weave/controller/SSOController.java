package cn.luckyh.purchase.weave.controller;

import cn.luckyh.purchase.common.core.domain.Result;
import cn.luckyh.purchase.common.core.domain.ResultUtil;
import cn.luckyh.purchase.weave.service.SSOService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/08/25 0025 11:28
 */
@Slf4j
@RestController
@Api(tags = "单点接口")
@RequestMapping(value = "/sso")
public class SSOController {

    @Autowired
    private SSOService ssoService;

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public Result<String> ssoLogin(HttpServletRequest request, @RequestParam String user, @RequestParam String token) {
        String sysToken = ssoService.ssoLogin(request, user, token);
        if (StringUtils.hasText(sysToken)) {
            return ResultUtil.data(sysToken);
        } else {
            return ResultUtil.error("登陆失败");
        }
    }

    @PostMapping("/push")
    @ApiOperation(value = "推送消息")
    public Result<String> pushMessageTest(String message) {
        ssoService.publishMessage(message);
        return ResultUtil.data("success");
    }
}
