package com.github.stazxr.zblog.bas.security.autoconfigure;

import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.filter.SecurityFilterChainCustomizer;
import com.github.stazxr.zblog.bas.security.filter.UserPassAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 用户名密码认证过滤器配置
 *
 * @author SunTao
 * @since 2026-03-09
 */
@Configuration
@RequiredArgsConstructor
public class UserPassAuthenticationFilterConfig {
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final SecurityExtProperties securityExtProperties;

    private final AuthenticationManager authenticationManager;

    @Bean
    public UserPassAuthenticationFilter userPassAuthenticationFilter() {
        UserPassAuthenticationFilter authenticationFilter = new UserPassAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager);
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        authenticationFilter.setFilterProcessesUrl(securityExtProperties.getLoginUrl());
        authenticationFilter.setAllowSessionCreation(false);
        return authenticationFilter;
    }

    @Bean
    public SecurityFilterChainCustomizer userPassAuthenticationFilterCustomizer(UserPassAuthenticationFilter userPassAuthenticationFilter) {
        return http -> http.addFilterAt(userPassAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
