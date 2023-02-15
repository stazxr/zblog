package com.github.stazxr.zblog.base.component.security;

import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.base.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

/**
 * 截获请求资源，并告诉程序访问这个地址需要哪些角色
 *
 * @author SunTao
 * @since 2020-11-16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private final RouterService routerService;

    /**
     * 自定义规则
     *
     *   这里可以对
     *      Set<String> roles = routerService.findRoles(requestUrl, requestMethod);
     *   做缓存处理
     *
     * @param object FilterInvocation
     * @return 可以访问该权限的角色列表
     * @throws IllegalArgumentException passed object eor
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 根据请求地址查询允许访问的角色列表
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        String requestMethod = ((FilterInvocation) object).getRequest().getMethod();
        Set<String> roles = routerService.findRoles(requestUrl, requestMethod);

        // 非 GET 请求标记测试用户不允许调用
        if (!HttpMethod.GET.name().equalsIgnoreCase(requestMethod)) {
            roles.add(Constants.SecurityRole.NO_TEST);
        }

        String[] attributes1 = new String[roles.size()];
        return SecurityConfig.createList(roles.toArray(attributes1));
    }

    /**
     * 返回所有定义好的权限资源
     * Spring Security在启动时会校验相关配置是否正确，如果不需要校验，直接返回null
     *
     * @return ConfigAttributes
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        // 支持校验
        return true;
    }
}
