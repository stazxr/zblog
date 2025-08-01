package com.github.stazxr.zblog.bas.security.authz;

import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.authz.metadata.SecurityResourceService;
import com.github.stazxr.zblog.util.collection.TimeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * CustomSecurityMetadataSource 是一个自定义的 {@link FilterInvocationSecurityMetadataSource} 实现，
 * 用于基于请求地址来动态获取访问该资源所需的角色列表，并缓存结果以提高查询效率。
 *
 * <p>
 * 该类通过 {@link SecurityResourceService} 获取与资源（URI + HTTP 方法）相关的角色列表。
 * 若资源为非 GET 请求，还会自动添加一个标记角色，表示测试用户无法调用非 GET 请求的资源。
 * </p>
 *
 * <p>
 * 该类重写了 {@code getAllConfigAttributes()} 方法并返回 {@code null}，以禁用 Spring Security 的全局权限预加载。
 * </p>
 *
 * @see FilterInvocationSecurityMetadataSource
 * @see SecurityResourceService
 * @see ConfigAttribute
 *
 * @author SunTao
 * @since 2024-11-12
 */
@Component
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    // TODO 使用全局缓存
    private final TimeMap<String, Collection<ConfigAttribute>> resourceMap = new TimeMap<>("SecurityMetadataSourceCacheThread");

    private SecurityResourceService securityResourceService;

    private SecurityExtProperties securityExtProperties;

    /**
     * 根据请求地址获取访问该资源所需的角色列表。若已缓存，则直接返回缓存结果，
     * 否则查询服务并将结果缓存一段时间。
     *
     * @param object 目标对象，必须为 {@link FilterInvocation}
     * @return 资源所需的角色列表，若无角色要求则返回空集合
     * @throws IllegalArgumentException 当对象不是 {@link FilterInvocation} 时抛出
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 获取请求 Uri 和 Method
        FilterInvocation invocation = (FilterInvocation) object;
        String requestUrl = invocation.getRequest().getRequestURI();
        String requestMethod = invocation.getRequest().getMethod();

        // 根据请求地址查询允许访问的角色列表
        String resourceKey = requestUrl.concat("_").concat(requestMethod);
        if (requestUrl.contains(resourceKey)) {
            // 从缓存中读取角色列表
            return resourceMap.get(resourceKey);
        }

        // 查询访问资源所需要的角色列表
        Set<String> roles = securityResourceService.getResourceRoles(requestUrl, requestMethod);

        // 非 GET 请求标记测试用户不允许调用
        if (!HttpMethod.GET.name().equalsIgnoreCase(requestMethod)) {
            roles.add(DefaultRoleCode.NO_TEST);
        }

        // 缓存并返回角色列表
        List<ConfigAttribute> configAttributes = SecurityConfig.createList(roles.toArray(new String[0]));
        return resourceMap.put(resourceKey, configAttributes, securityExtProperties.getResourceRolesCacheMills());
    }

    /**
     * 禁用全局权限预加载，不提供所有权限的全局缓存。
     *
     * @return 返回 {@code null}，表示不需要全局权限预加载
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * 确认该类支持的安全对象类型。
     *
     * @param clazz 传入的安全对象类
     * @return 如果是 {@link FilterInvocation}，则返回 {@code true}，否则返回 {@code false}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Autowired
    public void setSecurityResourceService(SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
    }

    @Autowired
    public void setSecurityExtProperties(SecurityExtProperties securityExtProperties) {
        this.securityExtProperties = securityExtProperties;
    }
}
