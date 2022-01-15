package com.github.stazxr.zblog.base.config;

import com.github.stazxr.zblog.base.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 安全配置
 *
 * @author SunTao
 * @since 2020-11-14
 */
@Configuration
@RequiredArgsConstructor
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 安全认证用户信息来源, {@link UserDetailsServiceImpl#loadUserByUsername}
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * 使用 Security 推荐的 BCryptPasswordEncoder 进行加解密
     *
     * @return 加密手段 BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * authenticationProvider 配置DB的认证方式
     *
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false); // 抛出用户找不到异常
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 所有资源都需要验证
        http.authorizeRequests().anyRequest().authenticated();

        // 表单登录配置
        http.formLogin().loginProcessingUrl("/process")
                .successForwardUrl("/api/auth/login_success")
                .failureForwardUrl("/api/auth/login_failure");

        // 关闭CSRF保护
        http.csrf().disable().cors();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}
