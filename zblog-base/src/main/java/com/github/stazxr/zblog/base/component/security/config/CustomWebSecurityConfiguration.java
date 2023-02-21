package com.github.stazxr.zblog.base.component.security.config;

import com.github.stazxr.zblog.base.component.security.CustomAccessDecisionManager;
import com.github.stazxr.zblog.base.component.security.CustomRememberMeServices;
import com.github.stazxr.zblog.base.component.security.CustomSecurityMetadataSource;
import com.github.stazxr.zblog.base.component.security.UserDetailsServiceImpl;
import com.github.stazxr.zblog.base.component.security.exception.CustomAccessDeniedHandler;
import com.github.stazxr.zblog.base.component.security.exception.CustomAuthenticationEntryPoint;
import com.github.stazxr.zblog.base.component.security.filter.*;
import com.github.stazxr.zblog.base.component.security.handler.CustomAuthenticationFailureHandler;
import com.github.stazxr.zblog.base.component.security.handler.CustomAuthenticationSuccessHandler;
import com.github.stazxr.zblog.base.component.security.handler.CustomLogoutHandler;
import com.github.stazxr.zblog.base.component.security.handler.CustomLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.sql.DataSource;

/**
 * 自定义安全配置
 *
 * @author SunTao
 * @since 2020-11-14
 */
@Configuration
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CustomWebSecurityConfiguration {
    private static final String REMEMBER_ME_UUID = "19960312&2014109078ST";

    /**
     * login url
     */
    public static final String LOGIN_PROCESSING_URL = "/api/process";

    /**
     * logout url
     */
    public static final String LOGOUT_URL = "/api/logout";

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
         * 安全认证用户信息来源, {@link UserDetailsServiceImpl#loadUserByUsername}
         */
        private final UserDetailsServiceImpl userDetailsService;

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
         * 令牌认证过滤器
         */
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        /**
         * 自定义元数据加载器
         */
        private final CustomSecurityMetadataSource securityMetadataSource;

        /**
         * 访问决策管理器
         */
        private final CustomAccessDecisionManager accessDecisionManager;

        /**
         * 跨域支持
         */
        private final CorsFilter corsFilter;

        /**
         * 数据源
         */
        private final DataSource dataSource;

        /**
         * 设置登录参数过滤器
         */
        private final ParseLoginParamFilter parseLoginParamFilter;

        /**
         * 登录验证码校验过滤器
         */
        private final ValidateLoginCodeFilter validateLoginCodeFilter;

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
         * 登录认证拦截器
         *
         * @return LoginAuthenticationFilter
         * @throws Exception e
         */
        @Bean
        public CustomLoginAuthenticationFilter customLoginAuthenticationFilter() throws Exception {
            CustomLoginAuthenticationFilter filter = new CustomLoginAuthenticationFilter();
            filter.setFilterProcessesUrl(LOGIN_PROCESSING_URL);
            filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
            filter.setAuthenticationFailureHandler(authenticationFailureHandler);
            filter.setAuthenticationManager(authenticationManagerBean());
            filter.setRememberMeServices(rememberMeServices());
            return filter;
        }

        /**
         * 登录认证拦截器
         *
         * @return LoginAuthenticationFilter
         * @throws Exception e
         */
        @Bean
        public CustomRememberMeFilter customRememberMeFilter() throws Exception {
            CustomRememberMeFilter filter = new CustomRememberMeFilter(
                    authenticationManagerBean(), rememberMeServices()
            );

            filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
            return filter;
        }

        /**
         * 配置TokenRepository
         *
         * @return PersistentTokenRepository
         */
        @Bean
        public PersistentTokenRepository persistentTokenRepository() {
            JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
            jdbcTokenRepository.setCreateTableOnStartup(false);
            jdbcTokenRepository.setDataSource(dataSource);
            return jdbcTokenRepository;
        }

        /**
         * 记住我
         *
         * @return RememberMeServices
         */
        @Bean
        public RememberMeServices rememberMeServices() {
            return new CustomRememberMeServices(REMEMBER_ME_UUID, userDetailsService, persistentTokenRepository());
        }

        /**
         * AuthenticationManager
         *
         * @return AuthenticationManager
         * @throws Exception e
         */
        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
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
            // 自定义访问决策器、资源池查询
            http.authorizeRequests().anyRequest().authenticated().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                @Override
                public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                    o.setSecurityMetadataSource(securityMetadataSource);
                    o.setAccessDecisionManager(accessDecisionManager);
                    return o;
                }
            });

            // session 生成策略用无状态策略
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            // 表单登录配置
            http.formLogin().loginProcessingUrl(LOGIN_PROCESSING_URL)
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler);

            // 登出配置
            http.logout().logoutUrl(LOGOUT_URL).addLogoutHandler(logoutHandler).logoutSuccessHandler(logoutSuccessHandler);

            // 异常处理
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler);

            // 记住我
            http.rememberMe().rememberMeServices(rememberMeServices());

            // 配置过滤器链，see: FilterOrderRegistration
            http.addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class);
            http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);
            http.addFilterBefore(parseLoginParamFilter, UsernamePasswordAuthenticationFilter.class);
            http.addFilterAfter(validateLoginCodeFilter, ParseLoginParamFilter.class);
            http.addFilterAt(customLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            http.addFilterAt(customRememberMeFilter(), RememberMeAuthenticationFilter.class);

            // 防止iframe 造成跨域
            http.headers().frameOptions().disable();

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
