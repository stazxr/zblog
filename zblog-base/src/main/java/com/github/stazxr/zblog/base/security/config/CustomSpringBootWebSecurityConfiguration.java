package com.github.stazxr.zblog.base.security.config;

import com.github.stazxr.zblog.base.security.exception.CustomAccessDeniedHandler;
import com.github.stazxr.zblog.base.security.CustomUserDetailsService;
import com.github.stazxr.zblog.base.security.exception.CustomAuthenticationEntryPoint;
import com.github.stazxr.zblog.base.security.filter.JwtAuthenticationFilter;
import com.github.stazxr.zblog.base.security.handler.CustomAuthenticationFailureHandler;
import com.github.stazxr.zblog.base.security.handler.CustomAuthenticationSuccessHandler;
import com.github.stazxr.zblog.base.security.handler.CustomLogoutHandler;
import com.github.stazxr.zblog.base.security.handler.CustomLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 自定义安全配置
 *
 * @author SunTao
 * @since 2020-11-14
 */
@Configuration
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CustomSpringBootWebSecurityConfiguration {
    /**
     * login url
     */
    private static final String LOGIN_PROCESSING_URL = "/process";

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
     * custom security
     */
    @Configuration
    @RequiredArgsConstructor
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    static class SecurityConfig extends WebSecurityConfigurerAdapter {
        /**
         * 安全认证用户信息来源, {@link CustomUserDetailsService#loadUserByUsername}
         */
        private final CustomUserDetailsService userDetailsService;

        /**
         * 加密方式
         */
        private final BCryptPasswordEncoder passwordEncoder;

        /**
         * AccessDeniedException 异常处理
         */
        private final CustomAccessDeniedHandler accessDeniedHandler;

        /**
         * CustomAuthenticationEntryPoint
         */
        private final CustomAuthenticationEntryPoint authenticationEntryPoint;

        /**
         * 自定义登出控制器
         */
        private final CustomLogoutHandler logoutHandler;

        /**
         * 自定义登出成功控制器
         */
        private final CustomLogoutSuccessHandler logoutSuccessHandler;

        /**
         * 认证成功控制器
         */
        private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

        /**
         * 认证失败控制器
         */
        private final CustomAuthenticationFailureHandler authenticationFailureHandler;

        /**
         * jwt 认证过滤器
         */
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        /**
         * authenticationProvider 配置DB的认证方式
         *
         * @return DaoAuthenticationProvider
         */
        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(userDetailsService);
            provider.setPasswordEncoder(passwordEncoder);
            provider.setHideUserNotFoundExceptions(false); // 抛出用户找不到异常
            return provider;
        }

        /**
         * HttpSecurity config
         *  用于构建一个安全过滤器链 SecurityFilterChain，SecurityFilterChain 最终被注入核心过滤器。
         *
         * @param http 安全访问策略
         * @throws Exception patch eor
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // 所有资源都需要验证
            http.authorizeRequests().anyRequest().authenticated();

            // session 生成策略用无状态策略
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            // 表单登录配置
            http.formLogin().loginProcessingUrl(LOGIN_PROCESSING_URL)
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler);

            // 登出配置
            http.logout().addLogoutHandler(logoutHandler).logoutSuccessHandler(logoutSuccessHandler);

            // 异常处理
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler);

            // 配置过滤器链
            http.addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class);

            // 关闭CSRF保护, 开启跨域资源共享
            http.csrf().disable().cors();
        }

        /**
         * AuthenticationManager config，管理 UserDetails 和 PasswordEncoder
         *
         * @param auth AuthenticationManagerBuilder
         * @throws Exception patch eor
         */
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            super.configure(auth);
        }

        /**
         * WebSecurity config
         *  WebSecurity 是基于 Servlet Filter 用来配置 springSecurityFilterChain
         *  springSecurityFilterChain 又被委托给了 Spring Security 核心过滤器 DelegatingFilterProxy
         *
         * @param web WebSecurity
         * @throws Exception patch eor
         */
        @Override
        public void configure(WebSecurity web) throws Exception {
            super.configure(web);
        }
    }
}
