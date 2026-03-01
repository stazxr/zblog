package com.github.stazxr.zblog.base.service.impl;

import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.router.Resource;
import com.github.stazxr.zblog.bas.router.RouterParser;
import com.github.stazxr.zblog.bas.security.authz.metadata.ResourceCacheService;
import com.github.stazxr.zblog.base.mapper.ResourceMapper;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.util.collection.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 资源缓存服务的数据库实现。
 *
 * @author SunTao
 * @since 2025-07-07
 * @version 5.0
 */
@Service
public class DatabaseResourceServiceImpl implements ResourceCacheService, CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DatabaseResourceServiceImpl.class);

    private final ResourceMapper resourceMapper;

    public DatabaseResourceServiceImpl(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    /**
     * 根据请求的 URI 和请求方法查找匹配的资源信息。
     *
     * @param requestUri    请求路径
     * @param requestMethod 请求方法
     * @return 匹配到的 {@link Resource} 资源对象；若未找到匹配资源则返回 {@code null}
     */
    @Override
    public Resource findResource(String requestUri, String requestMethod) {
        BaseConst.GlobalCacheKey resourceKey = BaseConst.GlobalCacheKey.resource;
        String cacheKey = String.format(resourceKey.getKey(), requestUri, requestMethod);
        return GlobalCache.getOrLoad(
                cacheKey,
                () -> resourceMapper.selectByUriAndMethod(requestUri, requestMethod),
                GlobalCache.jitter(resourceKey.duration() * 1000L),
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * 清除资源缓存
     *
     * @param requestUri 请求路径
     * @param requestMethod 请求方法
     */
    @Override
    public void clearCache(String requestUri, String requestMethod) {
        BaseConst.GlobalCacheKey resourceKey = BaseConst.GlobalCacheKey.resource;
        String cacheKey = String.format(resourceKey.getKey(), requestUri, requestMethod);
        GlobalCache.remove(cacheKey);
    }

    /**
     * 清除全部缓存
     */
    @Override
    public void clearCache() {
        log.warn("不支持清理资源缓存池...");
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        try {
            long start = System.currentTimeMillis();
            final int batchSize = 50;
            List<Resource> resources = RouterParser.execute();
            resourceMapper.deleteAll();
            int batchCount = (resources.size() + batchSize - 1) / batchSize;
            List<List<Resource>> routers = ArrayUtils.averageAssign(resources, batchCount);
            routers.forEach(resourceMapper::insertBatch);
            long cost = System.currentTimeMillis() - start;
            log.info("Application startup - Resource initialization completed. " +
                            "Total: {}, BatchSize: {}, Batches: {}, Time: {} ms",
                    resources.size(), batchSize, routers.size(), cost);
        } catch (Exception e) {
            log.error("Application startup - Resource initialization failed. " +
                    "System will exit. Reason: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}
