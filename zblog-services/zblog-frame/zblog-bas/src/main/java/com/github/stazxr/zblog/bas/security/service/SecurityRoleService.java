package com.github.stazxr.zblog.bas.security.service;

import java.util.Set;

/**
 * 用于管理系统中的安全角色服务接口。
 *
 * @see org.springframework.security.core.GrantedAuthority
 * @see com.github.stazxr.zblog.bas.security.core.SecurityRole
 *
 * @author SunTao
 * @since 2025-07-11
 */
public interface SecurityRoleService {
    /**
     * 获取指定资源的允许访问角色集合。
     *
     * @param requestUri    请求的 URL 路径，表示要访问的资源地址。
     * @param requestMethod 请求的 HTTP 方法类型（例如 GET、POST 等），
     *                      用于标识请求的操作类型。
     * @return 角色编码集合。
     */
    Set<String> selectResourceRoles(String requestUri, String requestMethod);
}

