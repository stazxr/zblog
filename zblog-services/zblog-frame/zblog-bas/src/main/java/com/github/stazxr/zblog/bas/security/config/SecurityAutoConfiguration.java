package com.github.stazxr.zblog.bas.security.config;

import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.authn.userpass.UserPassAuthenticationFilter;
import com.github.stazxr.zblog.bas.security.authn.userpass.numcode.ValidateLoginCodeFilter;
import com.github.stazxr.zblog.bas.security.authz.CustomAccessDecisionManager;
import com.github.stazxr.zblog.bas.security.authz.CustomSecurityInterceptorPostProcessor;
import com.github.stazxr.zblog.bas.security.authz.metadata.ResourceCacheService;
import com.github.stazxr.zblog.bas.security.authz.metadata.ResourceCacheServiceImpl;
import com.github.stazxr.zblog.bas.security.authz.metadata.SecurityResourceServiceImpl;
import com.github.stazxr.zblog.bas.security.authz.metadata.SecurityResourceService;
import com.github.stazxr.zblog.bas.security.filter.IpCheckFilter;
import com.github.stazxr.zblog.bas.security.filter.JwtAuthenticationFilter;
import com.github.stazxr.zblog.bas.security.filter.ParseLoginParamFilter;
import com.github.stazxr.zblog.bas.security.service.SecurityRoleService;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import com.github.stazxr.zblog.bas.security.service.SecurityTokenService;
import com.github.stazxr.zblog.bas.security.service.SecurityLogoutService;
import com.github.stazxr.zblog.bas.security.service.impl.SecurityTokenServiceImpl;
import com.github.stazxr.zblog.bas.security.service.impl.SecurityRoleServiceImpl;
import com.github.stazxr.zblog.bas.security.service.impl.SecurityUserServiceImpl;
import com.github.stazxr.zblog.bas.security.service.impl.SecurityLogoutServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.filter.CorsFilter;

/**
 * Spring Security 自动配置类，提供了安全配置相关的 Bean 和定制化的 Web 安全配置。
 *
 * <p>1、确保 WebSecurityConfigurerAdapter 存在时加载此配置。</p>
 * <p>2、仅在基于 Servlet 的 Web 应用中启用此配置。</p>
 *
 * @author SunTao
 * @since 2024-11-08
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityExtProperties.class)
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityAutoConfiguration {
    /**
     * Spring Security 配置
     */
    private final SecurityProperties securityProperties;

    /**
     * 创建安全配置相关的BEAN。
     */
    @Configuration
    class SecurityBeanConfig {
        /**
         * 提供一个 BCryptPasswordEncoder 实例，用于密码的加密与验证。
         *
         * 该方法定义了一个 Bean，通过 Spring 容器管理密码加密器。在 Spring Security 中，
         * BCryptPasswordEncoder 是常用的加密实现，能够提供安全的加密算法并且支持加盐处理。
         *
         * @return 返回一个 BCryptPasswordEncoder 实例，用于处理密码的加密和校验。
         */
        @Bean("passwordEncoder")
        public PasswordEncoder passwordEncoder() {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            log.info("Use Default PasswordEncoder: {}", passwordEncoder);
            return passwordEncoder;
        }

        /**
         * 创建并返回一个 SecurityUserService 实例，处理与安全用户相关的业务逻辑。
         *
         * <p>该方法根据条件创建一个默认的 SecurityUserServiceImpl 实例。
         * 基于 {@link org.springframework.boot.autoconfigure.security.SecurityProperties#getUser} 创建一个内存存储的用户。</p>
         *
         * <p>建议覆盖当前BEAN，实现基于DB或其他机制的安全用户服务。</p>
         *
         * <p>该方法仅在以下条件下创建 Bean：
         * <li>1. Spring 容器中存在一个 PasswordEncoder 实例（用于密码加密与校验）。</li>
         * <li>2. Spring 容器中不存在 SecurityUserService 的 Bean 实例（确保不会重复创建）。</li>
         * </p>
         *
         * @param passwordEncoder 用于密码加密与校验的 PasswordEncoder 实例
         * @return 返回一个 SecurityUserServiceImpl 实例
         */
        @Bean("securityUserService")
        @ConditionalOnBean(PasswordEncoder.class)
        @ConditionalOnMissingBean(SecurityUserService.class)
        public SecurityUserService securityUserService(PasswordEncoder passwordEncoder) {
            SecurityUserServiceImpl securityUserService = new SecurityUserServiceImpl(securityProperties, passwordEncoder);
            log.info("Use Default SecurityUserService: {}", securityUserService);
            return securityUserService;
        }

        /**
         * 创建并返回一个 SecurityRoleService 实例。
         *
         * <p>该方法根据条件创建一个默认的 SecurityRoleServiceImpl 实例。
         * SecurityRoleService 主要用于管理和操作用户角色、权限等信息。</p>
         *
         * <p>建议覆盖当前BEAN，实现基于DB或其他机制的更复杂的安全角色服务。</p>
         *
         * <p>该方法仅在 Spring 容器中没有 SecurityRoleService 类型的 Bean 时才会创建新的实例。</p>
         *
         * @return 返回一个 SecurityRoleServiceImpl 实例
         */
        @Bean("securityRoleService")
        @ConditionalOnMissingBean(SecurityRoleService.class)
        public SecurityRoleService securityRoleService() {
            SecurityRoleServiceImpl securityRoleService = new SecurityRoleServiceImpl();
            log.info("Use Default SecurityRoleService: {}", securityRoleService);
            return securityRoleService;
        }

        /**
         * 创建并注册一个默认的 {@link SecurityTokenService} 实例。
         *
         * <p>
         * 该方法会在 Spring 容器中没有 {@link SecurityTokenService} 类型的 bean 时调用。
         * 如果容器中已经存在该类型的 bean，则不会重复创建实例。
         * </p>
         *
         * <p>建议覆盖当前BEAN，实现基于DB或其他机制的安全令牌服务。</p>
         *
         * <strong>注意：</strong>该服务处理与用户安全令牌相关的所有操作，如生成、验证和过期管理。
         *
         * @return 返回一个 {@link SecurityTokenService} 类型的 bean，默认实现为 {@link SecurityTokenServiceImpl}
         */
        @Bean("securityTokenService")
        @ConditionalOnMissingBean(SecurityTokenService.class)
        public SecurityTokenService securityTokenService() {
            SecurityTokenServiceImpl securityTokenService = new SecurityTokenServiceImpl();
            log.info("Use Default SecurityTokenService: {}", securityTokenService);
            return securityTokenService;
        }

        /**
         * 创建并注册一个默认的 {@link SecurityLogoutService} 实例。
         *
         * <p>
         * 该方法会在 Spring 容器中没有 {@link SecurityLogoutService} 类型的 bean 时调用。
         * 如果容器中已经存在该类型的 bean，则不会重复创建实例。
         * </p>
         *
         * <strong>注意：</strong>该服务主要用于处理用户注销操作，清除用户的安全信息和令牌等内容。
         *
         * @return 返回一个 {@link SecurityLogoutService} 类型的 bean，默认实现为 {@link SecurityLogoutServiceImpl}
         */
        @Bean("securityLogoutService")
        @ConditionalOnMissingBean(SecurityLogoutService.class)
        public SecurityLogoutService securityLogoutService() {
            SecurityLogoutServiceImpl securityLogoutService = new SecurityLogoutServiceImpl();
            log.info("Use Default SecurityLogoutService: {}", securityLogoutService);
            return securityLogoutService;
        }

        /**
         * 创建并注册一个默认的 {@link ResourceCacheService} 实例。
         *
         * <p>
         * 该方法会在 Spring 容器中没有 {@link ResourceCacheService} 类型的 bean 时调用。
         * 如果容器中已经存在该类型的 bean，则不会重复创建实例。
         * </p>
         *
         * <p>建议覆盖当前BEAN，实现基于DB或其他机制的资源缓存服务。</p>
         *
         * <strong>注意：</strong>该服务用于处理与资源缓存相关的操作，如缓存资源文件、缓存策略、缓存过期等。
         *
         * @return 返回一个 {@link ResourceCacheService} 类型的 bean，默认实现为 {@link ResourceCacheServiceImpl}
         */
        @Bean("resourceCacheService")
        @ConditionalOnMissingBean(ResourceCacheService.class)
        public ResourceCacheService resourceCacheService() {
            ResourceCacheServiceImpl resourceCacheService = new ResourceCacheServiceImpl();
            log.info("Use Default ResourceCacheService: {}", resourceCacheService);
            return resourceCacheService;
        }

        /**
         * 创建并注册一个默认的 {@link SecurityResourceService} 实例。
         *
         * <p>
         * 该方法会在 Spring 容器中没有 {@link SecurityResourceService} 类型的 bean 时调用。
         * 如果容器中已经存在该类型的 bean，则不会重复创建实例。
         * </p>
         *
         * <p>非必要不建议覆盖当前BEAN，可以通过覆写 {@link ResourceCacheService} 和 {@link SecurityRoleService} 进行定制。</p>
         *
         * <strong>注意：</strong>该服务用于处理与资源权限相关的操作，如资源的访问控制、权限校验等。
         * 该服务依赖于 {@link ResourceCacheService} 和 {@link SecurityRoleService} 服务，确保资源缓存和角色服务已加载。
         * </p>
         *
         * @param resourceCacheService 资源缓存服务，负责缓存资源信息
         * @param securityRoleService  安全角色服务，负责角色管理
         * @return 返回一个 {@link SecurityResourceService} 类型的 bean，默认实现为 {@link SecurityResourceServiceImpl}
         */
        @Bean("securityResourceService")
        @ConditionalOnMissingBean(SecurityResourceService.class)
        public SecurityResourceService securityResourceService(ResourceCacheService resourceCacheService, SecurityRoleService securityRoleService) {
            SecurityResourceServiceImpl securityResourceService = new SecurityResourceServiceImpl(resourceCacheService, securityRoleService);
            log.info("Use Default SecurityResourceService: {}", securityResourceService);
            return securityResourceService;
        }
    }

    /**
     * 内部类 CustomWebSecurityConfigurerAdapter 是 Spring Security 的配置适配器，用于定制 Web 安全配置。
     *
     * <p>
     * 该类继承自 {@link WebSecurityConfigurerAdapter}，并重写了其配置方法，以便根据应用需求进行更细粒度的安全配置。
     * </p>
     */
    @Configuration
    @RequiredArgsConstructor
    @Order(org.springframework.boot.autoconfigure.security.SecurityProperties.BASIC_AUTH_ORDER)
    static class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        /**
         * Spring Security 扩展配置
         */
        private final SecurityExtProperties securityExtProperties;

        /**
         *
         */
        private final AuthenticationSuccessHandler authenticationSuccessHandler;

        /**
         *
         */
        private final AuthenticationFailureHandler authenticationFailureHandler;



        private final DaoAuthenticationProvider daoAuthenticationProvider;

        private final LogoutHandler logoutHandler;

        private final LogoutSuccessHandler logoutSuccessHandler;

        private final AuthenticationEntryPoint authenticationEntryPoint;

        private final AccessDeniedHandler accessDeniedHandler;

        private final IpCheckFilter ipCheckFilter;

        private final ValidateLoginCodeFilter validateLoginCodeFilter;

        private final ParseLoginParamFilter parseLoginParamFilter;

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        private final SecurityMetadataSource securityMetadataSource;

        private final CustomAccessDecisionManager accessDecisionManager;

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean
        public UserPassAuthenticationFilter userPassAuthenticationFilter() throws Exception {
            UserPassAuthenticationFilter authenticationFilter = new UserPassAuthenticationFilter();
            authenticationFilter.setAuthenticationManager(authenticationManagerBean());
            authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
            authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
            authenticationFilter.setFilterProcessesUrl(securityExtProperties.getLoginUrl());
            authenticationFilter.setAllowSessionCreation(false);
            return authenticationFilter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // 自定义访问决策器、资源池查询
            http.authorizeRequests().anyRequest().authenticated().withObjectPostProcessor(
                new CustomSecurityInterceptorPostProcessor(securityMetadataSource, accessDecisionManager)
            );

            // session 生成策略用无状态策略
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            // 登录配置
            http.formLogin().loginProcessingUrl(securityExtProperties.getLoginUrl())
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler);

            // 登出配置
            http.logout().logoutUrl(securityExtProperties.getLogoutUrl())
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler(logoutSuccessHandler);

            // 异常处理
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler);

            // 配置过滤器链，see: FilterOrderRegistration
            http.addFilterBefore(ipCheckFilter, LogoutFilter.class);
            http.addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class);
            http.addFilterBefore(parseLoginParamFilter, UsernamePasswordAuthenticationFilter.class);
            http.addFilterAfter(validateLoginCodeFilter, ParseLoginParamFilter.class);
            http.addFilterAt(userPassAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

            // 防止iframe 造成跨域
            http.headers().frameOptions().disable();

            // 关闭CSRF保护, 开启跨域资源共享
            http.csrf().disable().cors();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(daoAuthenticationProvider);
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            super.configure(web);
        }
    }
}
