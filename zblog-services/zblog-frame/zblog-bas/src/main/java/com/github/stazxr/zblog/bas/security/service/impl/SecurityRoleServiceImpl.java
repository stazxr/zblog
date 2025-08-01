package com.github.stazxr.zblog.bas.security.service.impl;

import com.github.stazxr.zblog.bas.security.authz.DefaultRoleCode;
import com.github.stazxr.zblog.bas.security.service.SecurityRoleService;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于管理系统中的安全角色服务接口的默认实现。
 *
 * @see org.springframework.security.core.GrantedAuthority
 * @see com.github.stazxr.zblog.bas.security.core.SecurityRole
 *
 * @author SunTao
 * @since 2025-07-11
 */
public class SecurityRoleServiceImpl implements SecurityRoleService {
    /**
     * 获取指定资源的允许访问角色集合。默认返回 {@code ROLE_PUBLIC}
     *
     * @param requestUri    请求的 URL 路径，表示要访问的资源地址。
     * @param requestMethod 请求的 HTTP 方法类型（例如 GET、POST 等），
     *                      用于标识请求的操作类型。
     * @return 角色编码集合。
     */
    @Override
    public Set<String> selectResourceRoles(String requestUri, String requestMethod) {
        Set<String> resourceRoles = new HashSet<>();
        resourceRoles.add(DefaultRoleCode.PUBLIC);
        return resourceRoles;
    }
}

