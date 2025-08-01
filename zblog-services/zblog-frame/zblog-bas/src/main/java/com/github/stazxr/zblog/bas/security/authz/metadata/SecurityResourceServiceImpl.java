package com.github.stazxr.zblog.bas.security.authz.metadata;

import com.github.stazxr.zblog.bas.router.Resource;
import com.github.stazxr.zblog.bas.router.RouterExtLevel;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.security.cache.BlackWhiteListCache;
import com.github.stazxr.zblog.bas.security.authz.DefaultRoleCode;
import com.github.stazxr.zblog.bas.security.service.SecurityRoleService;
import com.github.stazxr.zblog.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * SecurityResourceService 的默认实现，用于提供默认的行为，默认资源可以公开访问。
 *
 * <p>可在不需要资源角色管理的场景中直接使用，或在实际实现不可用时作为备用。</p>
 *
 * @author SunTao
 * @since 2024-11-18
 */
@Slf4j
public class SecurityResourceServiceImpl implements SecurityResourceService {
    private final ResourceCacheService resourceCacheService;

    private final SecurityRoleService securityRoleService;

    public SecurityResourceServiceImpl(ResourceCacheService resourceCacheService, SecurityRoleService securityRoleService) {
        Assert.notNull(resourceCacheService, "ResourceCacheService is required");
        Assert.notNull(resourceCacheService, "SecurityRoleService is required");
        this.resourceCacheService = resourceCacheService;
        this.securityRoleService = securityRoleService;
        log.info("Use ResourceCacheService: " + resourceCacheService);
        log.info("Use SecurityRoleService: " + securityRoleService);
    }

    /**
     * 获取指定资源的允许访问角色集合。
     *
     * @param requestUri    请求的 URL 路径，表示要访问的资源地址。
     * @param requestMethod 请求的 HTTP 方法类型（例如 GET、POST 等），
     *                      用于标识请求的操作类型。
     *
     * @see com.github.stazxr.zblog.bas.security.authz.CustomSecurityMetadataSource
     *
     * @return 角色编码集合。
     */
    @Override
    public Set<String> getResourceRoles(String requestUri, String requestMethod) {
        // 获取资源等级
        int resourceLevel = getResourceLevel(requestUri, requestMethod);

        // 获取访问资源所需要的角色
        Set<String> roles = new HashSet<>();
        if (RouterLevel.OPEN == resourceLevel) {
            roles.add(DefaultRoleCode.OPEN);
        } else if (RouterLevel.PUBLIC == resourceLevel) {
            roles.add(DefaultRoleCode.PUBLIC);
        } else if (RouterExtLevel.FORBIDDEN == resourceLevel) {
            roles.add(DefaultRoleCode.FORBIDDEN);
        } else if (RouterExtLevel.NULL == resourceLevel) {
            roles.add(DefaultRoleCode.NULL);
        } else {
            Set<String> resourceRoles = doSelectResourceRoles(requestUri, requestMethod);
            roles.addAll(resourceRoles);
        }
        return roles;
    }

    /**
     * 获取指定资源的允许访问角色集合。
     *
     * @param requestUri    请求的 URL 路径，表示要访问的资源地址。
     * @param requestMethod 请求的 HTTP 方法类型（例如 GET、POST 等），
     *                      用于标识请求的操作类型。
     * @return 角色编码集合。
     */
    protected Set<String> doSelectResourceRoles(String requestUri, String requestMethod) {
        return securityRoleService.selectResourceRoles(requestUri, requestMethod);
    }

    /**
     * 根据请求地址查询资源的访问级别
     *
     * @param requestUri    请求的 URL 路径
     * @param requestMethod 请求的 HTTP 方法类型（GET, POST, etc.）
     * @return 资源访问级别
     */
    @Override
    public int getResourceLevel(String requestUri, String requestMethod) {
        // 处理请求地址和请求方式
        final String whLabel = "?";
        requestUri = requestUri.contains(whLabel) ? requestUri.substring(0, requestUri.indexOf(whLabel)) : requestUri;
        requestMethod = requestMethod.toUpperCase(Locale.ROOT);

        // 白名单校验
        String checkKey = requestUri + ":" + requestMethod;
        if (BlackWhiteListCache.isWhitelisted(checkKey)) {
            return RouterExtLevel.OPEN;
        }

        // 黑名单校验
        if (BlackWhiteListCache.isBlacklisted(checkKey)) {
            return RouterExtLevel.FORBIDDEN;
        }

        // 查找资源信息（带有缓存）
        Resource resource = resourceCacheService.findResource(requestUri, requestMethod);
        if (resource == null) {
            // 访问资源不存在
            return RouterExtLevel.NULL;
        } else {
            return resource.getResourceLevel();
        }
    }
}
