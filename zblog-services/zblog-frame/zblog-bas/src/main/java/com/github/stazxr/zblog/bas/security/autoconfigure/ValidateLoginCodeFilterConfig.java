package com.github.stazxr.zblog.bas.security.autoconfigure;

import com.github.stazxr.zblog.bas.captcha.handler.CaptchaHandler;
import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.filter.ParseLoginParamFilter;
import com.github.stazxr.zblog.bas.security.filter.SecurityFilterChainCustomizer;
import com.github.stazxr.zblog.bas.security.filter.ValidateLoginCodeFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 验证码校验过滤器配置
 *
 * @author SunTao
 * @since 2026-03-09
 */
@Configuration
@RequiredArgsConstructor
public class ValidateLoginCodeFilterConfig {
    private final CaptchaHandler captchaHandler;

    private final SecurityExtProperties securityExtProperties;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public ValidateLoginCodeFilter validateLoginCodeFilter() {
        ValidateLoginCodeFilter validateLoginCodeFilter = new ValidateLoginCodeFilter();
        // 验证码处理器
        validateLoginCodeFilter.setCaptchaHandler(captchaHandler);
        // 登录地址
        validateLoginCodeFilter.setLoginPath(securityExtProperties.getLoginUrl());
        // 认证接口
        validateLoginCodeFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return validateLoginCodeFilter;
    }

    @Bean
    public SecurityFilterChainCustomizer validateLoginCodeFilterCustomizer(ValidateLoginCodeFilter validateLoginCodeFilter) {
        return http -> http.addFilterAfter(validateLoginCodeFilter, ParseLoginParamFilter.class);
    }
}
