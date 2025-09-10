package com.github.stazxr.zblog.bas.security.authz.metadata;

import com.github.stazxr.zblog.bas.router.Resource;

/**
 * 资源缓存服务。
 *
 * @author SunTao
 * @since 2024-11-24
 * @version 5.0
 */
public interface ResourceCacheService {
    /**
     * 根据请求的 URI 和请求方法查找匹配的资源信息。
     *
     * @param requestUri 请求路径
     * @param requestMethod 请求方法
     * @return 匹配到的 {@link Resource} 资源对象；若未找到匹配资源则返回 {@code null}
     */
    Resource findResource(String requestUri, String requestMethod);

    /**
     * 清除资源缓存
     *
     * @param cacheKey 缓存键
     */
    default void clearCache(String cacheKey) {
    }

    /**
     * 清除全部缓存
     */
    default void clearCache() {
    }
}
