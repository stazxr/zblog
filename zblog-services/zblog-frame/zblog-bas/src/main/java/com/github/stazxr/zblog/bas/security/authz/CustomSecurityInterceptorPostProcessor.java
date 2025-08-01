package com.github.stazxr.zblog.bas.security.authz;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * CustomSecurityInterceptorPostProcessor 是一个自定义的 {@link ObjectPostProcessor}，
 * 用于设置 {@link FilterSecurityInterceptor} 的安全元数据源和访问决策管理器。
 * <p>在 Spring Security 配置中，可以使用此类来为 {@link FilterSecurityInterceptor} 配置自定义的
 * {@link FilterInvocationSecurityMetadataSource} 和 {@link AccessDecisionManager}，从而实现基于资源的
 * 动态权限控制。</p>
 *
 * // @param securityMetadataSource 资源元数据源，用于获取资源的访问权限列表
 * // @param accessDecisionManager 访问决策管理器，用于基于用户角色和资源权限作出访问决策
 *
 * @author SunTao
 * @since 2024-11-13
 */
public class CustomSecurityInterceptorPostProcessor implements ObjectPostProcessor<FilterSecurityInterceptor> {
    private final SecurityMetadataSource securityMetadataSource;
    private final AccessDecisionManager accessDecisionManager;

    /**
     * 构造方法，初始化资源元数据源和访问决策管理器。
     *
     * @param securityMetadataSource 资源元数据源，负责定义各资源的访问权限
     * @param accessDecisionManager 访问决策管理器，负责根据用户权限和资源需求决定是否允许访问
     */
    public CustomSecurityInterceptorPostProcessor(
            SecurityMetadataSource securityMetadataSource, AccessDecisionManager accessDecisionManager) {
        this.securityMetadataSource = securityMetadataSource;
        this.accessDecisionManager = accessDecisionManager;
    }

    /**
     * 配置 {@link FilterSecurityInterceptor} 的安全元数据源和访问决策管理器。
     *
     * @param filterSecurityInterceptor 目标 {@link FilterSecurityInterceptor} 实例
     * @param <O> 泛型类型，限定为 {@link FilterSecurityInterceptor}
     * @return 配置完成的 {@link FilterSecurityInterceptor} 实例
     */
    @Override
    public <O extends FilterSecurityInterceptor> O postProcess(O filterSecurityInterceptor) {
        filterSecurityInterceptor.setSecurityMetadataSource((FilterInvocationSecurityMetadataSource) securityMetadataSource);
        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager);
        return filterSecurityInterceptor;
    }
}

