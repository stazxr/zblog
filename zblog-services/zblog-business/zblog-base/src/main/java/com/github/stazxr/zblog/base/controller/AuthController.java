package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.captcha.Captcha;
import com.github.stazxr.zblog.bas.captcha.handler.CaptchaHandler;
import com.github.stazxr.zblog.bas.msg.Result;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.security.authn.userpass.numcode.ValidateLoginCodeFilter;
import com.github.stazxr.zblog.base.service.ZblogService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.log.annotation.IgnoredLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证管理
 *
 * @author SunTao
 * @since 2022-01-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Api(value = "AuthController", tags = { "认证控制器" })
public class AuthController {
    private final ZblogService zblogService;

    private final CaptchaHandler captchaHandler;

    /**
     * 获取当前登录用户信息
     *
     * @return login username
     */
    @IgnoredLog
    @GetMapping("/loginId")
    @ApiOperation(value = "获取当前登录用户信息")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "获取当前登录用户信息", code = "loginId", level = RouterLevel.PUBLIC)
    public Result currentUserDetail() {
        return Result.success().data(SecurityUtils.getLoginUser());
    }

    /**
     * 获取登录验证码
     *
     * @return {img: %二维码的Base64编码%; uuid: %存储二维码的缓存主键%}
     */
    @GetMapping("/loginCode")
    @ApiOperation(value = "获取登录验证码")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "获取登录验证码", code = "loginCode", level = RouterLevel.OPEN)
    public Result loginCode() {
        Map<String, Object> data = new HashMap<>(2);
        Captcha captcha = captchaHandler.createCaptcha("loginCode");
        data.put("img", captcha.getBase64());
        data.put(ValidateLoginCodeFilter.DEFAULT_CACHE_KEY, captcha.getCaptchaId());
        return Result.success().data(data);
    }

    /**
     * 检查用户的登录状态
     *
     * @param request 请求信息
     * @return userId
     */
    @IgnoredLog
    @PostMapping("/checkUserLoginStatus")
    @ApiOperation(value = "检查用户的登录状态")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_2_0 })
    @Router(name = "检查用户的登录状态", code = "checkUserLoginStatus", level = RouterLevel.OPEN)
    public Result checkUserLoginStatus(HttpServletRequest request) {
        return Result.success().data(zblogService.checkUserLoginStatus(request));
    }
}
