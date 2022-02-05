package com.github.stazxr.zblog.base.security;

import com.github.stazxr.zblog.base.domain.entity.Permission;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.Router;
import com.github.stazxr.zblog.base.security.config.CustomWebSecurityConfiguration;
import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.base.service.RoleService;
import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.core.base.BaseConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.stazxr.zblog.base.util.Constants.SecurityRole.*;

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
    private final PermissionService permissionService;

    private final RoleService roleService;

    private final RouterService routerService;

    /**
     * 自定义规则
     *
     *
     * @param object FilterInvocation
     * @return 权限集合
     * @throws IllegalArgumentException passed object eor
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 拦截用户请求的地址，返回访问该地址需要的所有权限
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        if (requestUrl.contains("?")) {
            requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
        }

        // 放行登录请求
        if (CustomWebSecurityConfiguration.LOGIN_PROCESSING_URL.equalsIgnoreCase(requestUrl)) {
            return SecurityConfig.createList(OPEN);
        }

        // 放行白名单
        if (RouterBlackWhiteListCache.containsWhite(requestUrl)) {
            return SecurityConfig.createList(OPEN);
        }

        // 黑名单禁止访问
        if (RouterBlackWhiteListCache.containsBlack(requestUrl)) {
            return SecurityConfig.createList(FORBIDDEN);
        }

        String requestMethod = ((FilterInvocation) object).getRequest().getMethod();
        Router router = routerService.selectByUrlAndMethod(requestUrl, requestMethod);
        if (router == null || BaseConst.PermLevel.OPEN == router.getLevel()) {
            // 公开级别的资源，可直接访问
            return SecurityConfig.createList(OPEN);
        }

        if (BaseConst.PermLevel.PUBLIC == router.getLevel()) {
            // 公共级别的资源，登录即可访问
            return SecurityConfig.createList(PUBLIC);
        }

        // 受控权限，授权才可以访问
        Permission permission = permissionService.selectPermByPath(requestUrl);
        if (permission == null) {
            // 资源未纳管，不允许访问
            return SecurityConfig.createList(NONE);
        }

        // 获取访问该权限需要的角色列表
        List<Role> roles = roleService.queryRolesByPermissionId(permission.getId());
        List<String> attributes = new ArrayList<>();
        roles.stream().filter(Role::getEnabled).forEach(dd -> attributes.add(dd.getRoleCode()));
        String[] attributes1 = new String[attributes.size()];
        return SecurityConfig.createList(attributes.toArray(attributes1));
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
