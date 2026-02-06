package com.github.stazxr.zblog.bas.cache.autoconfigure;

import com.github.stazxr.zblog.bas.cache.Cache;
import com.github.stazxr.zblog.bas.cache.autoconfigure.properties.CacheConfigProperties;
import com.github.stazxr.zblog.bas.cache.memory.MemoryCache;
import com.github.stazxr.zblog.bas.cache.redis.RedisCache;
import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 自动配置全局缓存。
 *
 * @author SunTao
 * @since 2024-08-24
 */
@Configuration
@EnableConfigurationProperties(CacheConfigProperties.class)
public class CacheAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(CacheAutoConfiguration.class);

    /**
     * 创建内存缓存 Bean。
     *
     * @return 配置好的 MemoryCache 实例
     */
    @Bean
    @ConditionalOnProperty(prefix = "zblog.base.cache", name = "type", havingValue = "memory", matchIfMissing = true)
    public Cache<String, Object> memoryCache() {
        log.info("Global Cache Type: memory");
        MemoryCache<String, Object> cache = new MemoryCache<>();
        GlobalCache.init(cache);
        return cache;
    }

    /**
     * 创建 Redis 缓存 Bean。
     *
     * @return 配置好的 RedisCache 实例
     */
    @Bean
    @ConditionalOnProperty(prefix = "zblog.base.cache", name = "type", havingValue = "redis")
    public Cache<String, Object> redisCache(RedisTemplate<String, Object> redisTemplate) {
        log.info("Global Cache Type: redis");
        RedisCache<String, Object> cache = new RedisCache<>(redisTemplate);
        GlobalCache.init(cache);
        return cache;
    }
}
