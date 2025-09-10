package com.github.stazxr.zblog.base.service.impl.security;

import com.github.stazxr.zblog.bas.router.Resource;
import com.github.stazxr.zblog.bas.router.RouterParser;
import com.github.stazxr.zblog.bas.security.authz.metadata.ResourceCacheService;
import com.github.stazxr.zblog.base.mapper.ResourceMapper;
import com.github.stazxr.zblog.util.collection.ArrayUtils;
import com.github.stazxr.zblog.util.collection.TimeMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资源缓存服务的数据库实现。
 *
 * @author SunTao
 * @since 2025-07-07
 * @version 5.0
 */
@Slf4j
@Service
public class DatabaseResourceServiceImpl implements ResourceCacheService, InitializingBean {
    private static final TimeMap<String, Resource> RESOURCE_MAP = new TimeMap<>("ResourceCacheCheckThread");

    private ResourceMapper resourceMapper;

    /**
     * 根据请求的 URI 和请求方法查找匹配的资源信息。
     *
     * @param requestUri    请求路径
     * @param requestMethod 请求方法
     * @return 匹配到的 {@link Resource} 资源对象；若未找到匹配资源则返回 {@code null}
     */
    @Override
    public Resource findResource(String requestUri, String requestMethod) {
        String cacheKey = requestUri + ":" + requestMethod;
        if (RESOURCE_MAP.containsKey(cacheKey)) {
            return RESOURCE_MAP.get(cacheKey);
        }

        // 缓存数据
        Resource resource = resourceMapper.selectByUriAndMethod(requestUri, requestMethod);
        return RESOURCE_MAP.put(cacheKey, resource);
    }

    /**
     * 清除资源缓存
     *
     * @param cacheKey 缓存键
     */
    @Override
    public void clearCache(String cacheKey) {
        RESOURCE_MAP.remove(cacheKey);
    }

    /**
     * 清除全部缓存
     *
     * <strong>注意：</strong> 当权限修改时，需要清理缓存，防止权限不生效。
     */
    @Override
    public void clearCache() {
        RESOURCE_MAP.clear();
    }

    /**
     * 初始化资源信息
     */
    @Override
    public void afterPropertiesSet() {
        try {
            final int batchSize = 50;
            List<Resource> resources = RouterParser.execute();
            resourceMapper.deleteAll();
            int batchCount = (resources.size() % batchSize) > 0 ? (resources.size() / batchSize) + 1 : resources.size() / batchSize;
            List<List<Resource>> routers = ArrayUtils.averageAssign(resources, batchCount);
            routers.forEach(resourceMapper::insertBatch);
            log.info("Loaded {} Resources", resources.size());
        } catch (Exception e) {
            log.error("Loaded Resources Failed", e);
            System.exit(1);
        }
    }

    @Autowired
    public void setResourceMapper(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }
}
