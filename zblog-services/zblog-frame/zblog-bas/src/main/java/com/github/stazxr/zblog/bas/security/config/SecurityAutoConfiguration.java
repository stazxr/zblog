package com.github.stazxr.zblog.bas.security.config;

import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.authz.CustomAccessDecisionManager;
import com.github.stazxr.zblog.bas.security.authz.CustomSecurityInterceptorPostProcessor;
import com.github.stazxr.zblog.bas.security.authz.metadata.ResourceCacheService;
import com.github.stazxr.zblog.bas.security.authz.metadata.ResourceCacheServiceImpl;
import com.github.stazxr.zblog.bas.security.authz.metadata.SecurityResourceServiceImpl;
import com.github.stazxr.zblog.bas.security.authz.metadata.SecurityResourceService;
import com.github.stazxr.zblog.bas.security.filter.IpCheckFilter;
import com.github.stazxr.zblog.bas.security.filter.SecurityFilterChainCustomizer;
import com.github.stazxr.zblog.bas.security.service.SecurityRoleService;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import com.github.stazxr.zblog.bas.security.service.SecurityLogoutService;
import com.github.stazxr.zblog.bas.security.service.impl.SecurityRoleServiceImpl;
import com.github.stazxr.zblog.bas.security.service.impl.SecurityUserServiceImpl;
import com.github.stazxr.zblog.bas.security.service.impl.SecurityLogoutServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring Security 自动配置类，提供了安全配置相关的 Bean 和定制化的 Web 安全配置。
 *
 * <p>1、确保 WebSecurityConfigurerAdapter 存在时加载此配置。</p>
 * <p>2、仅在基于 Servlet 的 Web 应用中启用此配置。</p>
 *
 * @author SunTao
 * @since 2024-11-08
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityExtProperties.class)
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SecurityAutoConfiguration.class);

    /**
     * Spring Security 默认配置
     */
    private final SecurityProperties securityProperties;

    /**
     * Spring Security 扩展配置
     */
    private final SecurityExtProperties securityExtProperties;

    /**
     * 创建安全配置相关的BEAN。
     */
    @Configuration
    class SecurityBeanConfig {
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
         * @return 返回一个 {@link ResourceCacheService} 类型的 bean，默认实现为 {@link ResourceCacheServiceImpl}
         */
        @Bean("resourceCacheService")
        @ConditionalOnMissingBean(ResourceCacheService.class)
        public ResourceCacheService resourceCacheService() {
            ResourceCacheServiceImpl resourceCacheService = new ResourceCacheServiceImpl();
            log.info("[AutoConfiguration] No custom ResourceCacheService bean found, " +
                    "using default implementation: {}", ResourceCacheServiceImpl.class.getName());
            return resourceCacheService;
        }

        /**
         * 创建并返回一个 SecurityRoleService 实例。
         *
         * <p>该方法根据条件创建一个默认的 SecurityRoleServiceImpl 实例。
         * SecurityRoleService 主要用于管理和操作用户角色、权限等信息。</p>
         *
         * <p>建议覆盖当前BEAN，实现基于DB或其他机制的更复杂的安全角色服务。</p>
         *
         * @return 返回一个 SecurityRoleServiceImpl 实例
         */
        @Bean("securityRoleService")
        @ConditionalOnMissingBean(SecurityRoleService.class)
        public SecurityRoleService securityRoleService() {
            SecurityRoleServiceImpl securityRoleService = new SecurityRoleServiceImpl();
            log.info("[AutoConfiguration] No custom SecurityRoleService bean found, " +
                    "using default implementation: {}", SecurityRoleServiceImpl.class.getName());
            return securityRoleService;
        }

        /**
         * 创建并注册一个默认的 {@link SecurityResourceService} 实例。
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
        public SecurityResourceService securityResourceService(
                ResourceCacheService resourceCacheService, SecurityRoleService securityRoleService) {
            SecurityResourceServiceImpl securityResourceService = new SecurityResourceServiceImpl(resourceCacheService, securityRoleService);
            log.info("[AutoConfiguration] No custom SecurityResourceService bean found, " +
                    "using default implementation: {}", SecurityResourceService.class.getName());
            return securityResourceService;
        }

        /**
         * 提供一个 DelegatingPasswordEncoder 实例，用于密码的加密与验证。
         *
         * @return 返回一个 DelegatingPasswordEncoder 实例，用于处理密码的加密和校验。
         */
        @Bean("passwordEncoder")
        @ConditionalOnMissingBean(PasswordEncoder.class)
        public PasswordEncoder passwordEncoder() {
            String idForEncode = "bcrypt";
            Map<String, PasswordEncoder> encoders = new HashMap<>();
            encoders.put("bcrypt", new BCryptPasswordEncoder(12));
            encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
            encoders.put("scrypt", new SCryptPasswordEncoder());
            log.info("[AutoConfiguration] No custom PasswordEncoder bean found, " +
                    "using default implementation: {}", DelegatingPasswordEncoder.class.getName());
            return new DelegatingPasswordEncoder(idForEncode, encoders);
        }

        /**
         * 创建并返回一个 SecurityUserService 实例，处理与安全用户相关的业务逻辑。
         *
         * <p>该方法根据条件创建一个默认的 SecurityUserServiceImpl 实例。
         * 基于 {@link org.springframework.boot.autoconfigure.security.SecurityProperties#getUser} 创建一个内存存储的用户。</p>
         *
         * <p>建议覆盖当前BEAN，实现基于DB或其他机制的安全用户服务。</p>
         *
         * @param passwordEncoder 用于密码加密与校验的 PasswordEncoder 实例
         * @return 返回一个 SecurityUserServiceImpl 实例
         */
        @Bean("securityUserService")
        @ConditionalOnBean(PasswordEncoder.class)
        @ConditionalOnMissingBean(SecurityUserService.class)
        public SecurityUserService securityUserService(PasswordEncoder passwordEncoder) {
            SecurityUserServiceImpl securityUserService = new SecurityUserServiceImpl(securityProperties, passwordEncoder);
            log.info("[AutoConfiguration] No custom SecurityUserService bean found, " +
                    "using default implementation: {}", SecurityUserServiceImpl.class.getName());
            return securityUserService;
        }

        /**
         * 创建并注册一个默认的 {@link SecurityLogoutService} 实例。
         *
         * <strong>注意：</strong>该服务主要用于处理用户注销操作，清除用户的安全信息和令牌等内容。
         *
         * @return 返回一个 {@link SecurityLogoutService} 类型的 bean，默认实现为 {@link SecurityLogoutServiceImpl}
         */
        @Bean("securityLogoutService")
        @ConditionalOnMissingBean(SecurityLogoutService.class)
        public SecurityLogoutService securityLogoutService() {
            SecurityLogoutServiceImpl securityLogoutService = new SecurityLogoutServiceImpl();
            log.info("[AutoConfiguration] No custom SecurityLogoutService bean found, " +
                    "using default implementation: {}", SecurityLogoutServiceImpl.class.getName());
            return securityLogoutService;
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

        /** 自定义过滤器链配置 */
        private final List<SecurityFilterChainCustomizer> customizers;

        /**
         *
         */
        private final AuthenticationSuccessHandler authenticationSuccessHandler;

        /**
         *
         */
        private final AuthenticationFailureHandler authenticationFailureHandler;

        private final LogoutHandler logoutHandler;

        private final LogoutSuccessHandler logoutSuccessHandler;

        private final AuthenticationEntryPoint authenticationEntryPoint;

        private final AccessDeniedHandler accessDeniedHandler;

        private final IpCheckFilter ipCheckFilter;

        private final SecurityMetadataSource securityMetadataSource;

        private final CustomAccessDecisionManager accessDecisionManager;

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
                    .invalidateHttpSession(false)
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler(logoutSuccessHandler);

            // 异常处理
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler);

            // 配置过滤器链，see: FilterOrderRegistration
            http.addFilterBefore(ipCheckFilter, LogoutFilter.class);
            for (SecurityFilterChainCustomizer customizer : customizers) {
                customizer.customize(http);
            }

            // 防止iframe 造成跨域
            http.headers().frameOptions().disable();

            // 关闭CSRF保护, 开启跨域资源共享
            http.csrf().disable().cors();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            super.configure(web);
        }
    }
}
