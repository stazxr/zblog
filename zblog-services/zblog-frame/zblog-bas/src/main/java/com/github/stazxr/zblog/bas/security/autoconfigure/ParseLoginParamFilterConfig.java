package com.github.stazxr.zblog.bas.security.autoconfigure;

import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.filter.ParseLoginParamFilter;
import com.github.stazxr.zblog.bas.security.filter.SecurityFilterChainCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 登录参数解析过滤器配置
 *
 * @author SunTao
 * @since 2026-02-27
 */
@Configuration
@RequiredArgsConstructor
public class ParseLoginParamFilterConfig {
    @Value("${zblog.security.PrivateKey}")
    private String secretKey;

    private final SecurityExtProperties securityExtProperties;

    @Bean
    public ParseLoginParamFilter parseLoginParamFilter() {
        return new ParseLoginParamFilter(secretKey, securityExtProperties.getLoginUrl());
    }

    @Bean
    public SecurityFilterChainCustomizer parseLoginParamFilterCustomizer(ParseLoginParamFilter parseLoginParamFilter) {
        return http ->  http.addFilterBefore(parseLoginParamFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
