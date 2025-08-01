package com.github.stazxr.zblog.bas.security.authz.metadata;

import java.util.Set;

/**
 * SecurityResourceService 接口定义了获取资源访问权限的操作。
 *
 * <p>实现类应当负责从资源库中检索指定 URL 的角色权限，以支持访问控制决策。</p>
 *
 * @author SunTao
 * @since 2024-11-13
 */
public interface SecurityResourceService {
    /**
     * 获取指定资源的允许访问角色集合。
     *
     * @param requestUri    请求的 URL 路径，表示要访问的资源地址。
     * @param requestMethod 请求的 HTTP 方法类型（例如 GET、POST 等），
     *                      用于标识请求的操作类型。
     *
     * @see com.github.stazxr.zblog.bas.security.authz.CustomSecurityMetadataSource#getAttributes
     *
     * @return 角色编码集合。
     */
    Set<String> getResourceRoles(String requestUri, String requestMethod);

    /**
     * 根据请求地址查询资源的访问级别
     *
     * @param requestUri    请求的 URL 路径
     * @param requestMethod 请求的 HTTP 方法类型（GET, POST, etc.）
     * @return 资源访问级别
     */
    int getResourceLevel(String requestUri, String requestMethod);
}
