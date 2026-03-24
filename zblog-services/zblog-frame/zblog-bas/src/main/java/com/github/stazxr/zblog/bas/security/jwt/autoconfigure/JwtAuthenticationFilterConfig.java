package com.github.stazxr.zblog.bas.security.jwt.autoconfigure;

import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.authz.metadata.SecurityResourceService;
import com.github.stazxr.zblog.bas.security.filter.SecurityFilterChainCustomizer;
import com.github.stazxr.zblog.bas.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.bas.security.jwt.autoconfigure.properties.JwtProperties;
import com.github.stazxr.zblog.bas.security.jwt.decoder.JwtDecoder;
import com.github.stazxr.zblog.bas.security.jwt.filter.JwtAuthenticationFilter;
import com.github.stazxr.zblog.bas.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * JWT 过滤器配置
 *
 * @author SunTao
 * @since 2026-02-27
 */
@Configuration
@RequiredArgsConstructor
@AutoConfigureAfter(JwtAutoConfiguration.class)
public class JwtAuthenticationFilterConfig {
    private final JwtDecoder jwtDecoder;

    private final JwtTokenGenerator jwtTokenGenerator;

    private final JwtTokenStorage jwtTokenStorage;

    private final SecurityUserService securityUserService;

    private final SecurityResourceService securityResourceService;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final JwtProperties jwtProperties;

    private final SecurityExtProperties securityExtProperties;

    @Value("${error.path:/error}")
    private String errorPath;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
        // JWT 解码器
        jwtAuthenticationFilter.setJwtDecoder(jwtDecoder);
        // JWT Token 生成器
        jwtAuthenticationFilter.setJwtTokenGenerator(jwtTokenGenerator);
        // JWT Token 缓存器
        jwtAuthenticationFilter.setJwtTokenStorage(jwtTokenStorage);
        // JWT 配置对象
        jwtAuthenticationFilter.setJwtProperties(jwtProperties);
        // 安全用户服务
        jwtAuthenticationFilter.setSecurityUserService(securityUserService);
        // 资源管理服务
        jwtAuthenticationFilter.setSecurityResourceService(securityResourceService);
        // 认证接口
        jwtAuthenticationFilter.setAuthenticationEntryPoint(authenticationEntryPoint);
        // 错误处理地址
        jwtAuthenticationFilter.setErrorPath(errorPath);
        // 登录地址
        jwtAuthenticationFilter.setLoginPath(securityExtProperties.getLoginUrl());
        return jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChainCustomizer jwtAuthenticationFilterCustomizer(JwtAuthenticationFilter jwtAuthenticationFilter) {
        return http -> http.addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class);
    }
}
