package com.github.stazxr.zblog.base.controller;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.component.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.base.component.security.jwt.ZblogToken;
import com.github.stazxr.zblog.base.component.security.jwt.cache.JwtTokenStorage;
import com.github.stazxr.zblog.base.service.RoleService;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.base.component.captcha.CaptchaCodeEnum;
import com.github.stazxr.zblog.base.component.captcha.CaptchaCodeProperties;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.core.util.CacheUtils;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.UuidUtils;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 认证管理
 *
 * @author SunTao
 * @since 2022-01-15
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    private final RoleService roleService;

    private final JwtTokenGenerator jwtTokenGenerator;

    private final JwtTokenStorage jwtTokenStorage;

    private final JwtDecoder jwtDecoder;

    /**
     * 获取当前登录用户的用户名
     *
     * @return login username
     */
    @GetMapping("/loginId")
    @Router(name = "获取当前登录用户的用户名", code = "loginId", level = BaseConst.PermLevel.PUBLIC)
    public Result currentUserName(@AuthenticationPrincipal User user) {
        if (user == null) {
            return Result.failure(ResultCode.TOKEN_FAILED_XXX);
        }
        return Result.success().data(user);
    }

    /**
     * 获取登录验证码
     *
     * @return {img: %二维码的Base64编码%; uuid: %存储二维码的缓存主键%}
     */
    @GetMapping("/loginCode")
    @Router(name = "获取当前登录用户的用户名", code = "loginCode", level = BaseConst.PermLevel.OPEN)
    public Result loginCode() {
        // 从缓存中获取登录验证码的配置
        String cacheKey = Constants.CacheKey.captchaConfig.cacheKey().concat(":loginCode");
        String captchaConfigStr = CacheUtils.get(cacheKey);
        if (StringUtils.isBlank(captchaConfigStr)) {
            throw new ServiceException("获取验证码配置信息失败");
        }

        // 生成验证码
        CaptchaCodeProperties codeProperties = JSON.parseObject(captchaConfigStr, CaptchaCodeProperties.class);
        Captcha captcha = codeProperties.getCaptcha();

        // 缓存验证码
        Constants.CacheKey loginCode = Constants.CacheKey.loginCode;
        String uuid = loginCode.cacheKey().concat(":").concat(UuidUtils.generateShortUuid());

        // 当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        String point = ".";
        if (captcha.getCharType() - 1 == CaptchaCodeEnum.Arithmetic.ordinal() && captchaValue.contains(point)) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        CacheUtils.put(uuid, captchaValue, codeProperties.getDuration());

        // 返回验证码信息
        Map<String, Object> data = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return Result.success().data(data);
    }

    /**
     * 续签
     *
     * @param rtk refresh token
     * @return token pair
     */
    @PostMapping("renewToken")
    @Router(name = "获取当前登录用户的用户名", code = "renewToken", level = BaseConst.PermLevel.OPEN)
    public Result renewToken(@RequestPostSingleParam String rtk) {
        Assert.isTrue(StringUtils.isBlank(rtk), "续签失败，rtk不能为空");

        try {
            // decode token
            Jwt jwt = jwtDecoder.decode(rtk);
            List<String> audiences = jwt.getAudience();
            if (audiences != null && audiences.size() == 1) {
                // get zblog token from cache
                String username = audiences.get(0);
                ZblogToken token = jwtTokenStorage.get(username);
                if (Objects.nonNull(token)) {
                    ZblogToken.RefreshToken refreshToken = token.getRefreshToken();
                    if (rtk.equals(refreshToken.getTokenValue())) {
                        // generate new token pair
                        User user = userService.queryUserByUsername(username);
                        if (user != null && user.getEnabled()) {
                            List<Role> roles = roleService.queryRolesByUserId(user.getId());
                            user.setAuthorities(roles);

                            jwtTokenStorage.expire(username);
                            ZblogToken newToken = jwtTokenGenerator.getTokenResponse(user);
                            ZblogToken.AccessToken accessToken = newToken.getAccessToken();
                            Map<String, Object> data = new HashMap<>(2);
                            data.put("atk", accessToken.getTokenType().concat(" ").concat(accessToken.getTokenValue()));
                            data.put("rtk", newToken.getRefreshToken().getTokenValue());

                            log.info("=========> 续签成功: {}", username);
                            return Result.success("续签成功").data(data);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("=========> 续签过程发生错误", e);
        }

        log.warn("=========> 续签失败: {}", rtk);
        return Result.failure(ResultCode.RENEW_TOKEN_FAILED);
    }
}
