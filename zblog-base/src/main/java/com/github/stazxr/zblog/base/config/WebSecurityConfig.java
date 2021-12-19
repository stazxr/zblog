package com.github.stazxr.zblog.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security Config
 *
 * @author SunTao
 * @since 2020-11-14
 */
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 使用 Security 推荐的 BCryptPasswordEncoder 进行加解密
     *
     * @return 加密手段 BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
