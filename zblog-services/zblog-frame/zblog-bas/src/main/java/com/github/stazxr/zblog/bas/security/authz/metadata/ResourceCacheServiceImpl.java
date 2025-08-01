package com.github.stazxr.zblog.bas.security.authz.metadata;

import com.github.stazxr.zblog.bas.router.Resource;
import com.github.stazxr.zblog.bas.router.RouterParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资源缓存服务的默认实现。
 *
 * @author SunTao
 * @since 2024-11-24
 * @version 5.0
 */
@Slf4j
public class ResourceCacheServiceImpl implements ResourceCacheService, InitializingBean {
    private static final Map<String, Resource> RESOURCE_MAP = new ConcurrentHashMap<>();

    /**
     * 根据请求的 URI 和请求方法查找匹配的资源信息。
     *
     * @param requestUri 请求路径
     * @param requestMethod 请求方法
     * @return 匹配到的 {@link Resource} 资源对象；若未找到匹配资源则返回 {@code null}
     */
    @Override
    public Resource findResource(String requestUri, String requestMethod) {
        // 处理请求地址和请求方式
        final String whLabel = "?";
        requestUri = requestUri.contains(whLabel) ? requestUri.substring(0, requestUri.indexOf(whLabel)) : requestUri;
        requestMethod = requestMethod.toUpperCase(Locale.ROOT);

        String cacheKey = requestUri + ":" + requestMethod;
        return RESOURCE_MAP.get(cacheKey);
    }

    @Override
    public void afterPropertiesSet() {
        List<Resource> resources = RouterParser.execute();
        for (Resource resource : resources) {
            String cacheKey = resource.getResourceUri() + ":" + resource.getResourceMethod();
            RESOURCE_MAP.put(cacheKey, resource);
        }

        log.info("Loaded {} Resources", RESOURCE_MAP.size());
    }
}
