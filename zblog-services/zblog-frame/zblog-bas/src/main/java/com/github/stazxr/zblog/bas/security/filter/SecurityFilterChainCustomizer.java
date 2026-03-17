package com.github.stazxr.zblog.bas.security.filter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Spring Security 过滤器函数
 *
 * @author SunTao
 * @since 2026-03-09
 */
@FunctionalInterface
public interface SecurityFilterChainCustomizer {
    void customize(HttpSecurity http) throws Exception;
}
