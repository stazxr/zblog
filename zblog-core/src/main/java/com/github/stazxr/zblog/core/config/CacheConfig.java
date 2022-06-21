package com.github.stazxr.zblog.core.config;

import com.github.stazxr.zblog.core.cache.Cache;
import com.github.stazxr.zblog.core.cache.impl.MemoryCacheImpl;
import com.github.stazxr.zblog.core.cache.impl.RedisCacheImpl;
import com.github.stazxr.zblog.core.config.properties.CacheConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 系统内置缓存配置注入
 *
 * @author SunTao
 * @since 2022-03-03
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheConfigProperties.class)
public class CacheConfig {
    @Bean("sysCache")
    @ConditionalOnProperty(name = "cache.type", havingValue = "memory")
    public Cache memoryCache(CacheConfigProperties cacheConfigProperties) {
        log.info("Cache Type: {}", MemoryCacheImpl.class);
        return new MemoryCacheImpl(cacheConfigProperties);
    }

    @Bean("sysCache")
    @ConditionalOnProperty(name = "cache.type", havingValue = "redis")
    public Cache redisCache(CacheConfigProperties cacheConfigProperties, StringRedisTemplate redisTemplate) {
        log.info("Cache Type: {}", RedisCacheImpl.class);
        return new RedisCacheImpl(cacheConfigProperties, redisTemplate);
    }
}
