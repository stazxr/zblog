package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.captcha.Captcha;
import com.github.stazxr.zblog.bas.captcha.handler.CaptchaHandler;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.core.base.BaseConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录管理
 *
 * @author SunTao
 * @since 2022-07-24
 */
@RestController
@RequiredArgsConstructor
@Api(value = "LoginController", tags = { "登录管理" })
public class LoginController {
    private final CaptchaHandler captchaHandler;

    /**
     * 获取登录验证码
     *
     * @return {img: %二维码的Base64编码%; uuid: %存储二维码的缓存主键%}
     */
    @GetMapping("/api/loginCode")
    @ApiOperation(value = "获取登录验证码")
    @ApiVersion(BaseConst.ApiVersion.V_5_0_0)
    @Router(name = "获取登录验证码", code = "LOGI0001", level = RouterLevel.OPEN)
    public Map<String, Object> loginCode() {
        Map<String, Object> data = new HashMap<>(2);
        Captcha captcha = captchaHandler.createCaptcha("loginCode");
        data.put("img", captcha.getBase64());
        data.put("uuid", captcha.getCaptchaId());
        return data;
    }
}
