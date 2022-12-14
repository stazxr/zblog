package com.github.stazxr.zblog.core.cache.impl;

import com.github.stazxr.zblog.core.config.properties.CacheConfigProperties;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * 系统内置缓存接口规范的 Redis 实现方式
 *
 * @author SunTao
 * @since 2022-03-02
 */
@Slf4j
@RequiredArgsConstructor
public class RedisCacheImpl extends BaseCache {
    private final CacheConfigProperties cacheConfigProperties;

    private final StringRedisTemplate redisTemplate;

    /**
     * write into cache
     *
     * @param key   cache key
     * @param value cache content
     * @return param value
     */
    @Override
    public String put(String key, String value) {
        int duration = cacheConfigProperties.getDefaultDuration();
        return put(key, value, duration);
    }

    /**
     * write into cache
     *
     * @param key        cache key
     * @param value      cache content
     * @param expireTime valid time, unit seconds
     * @return param value
     */
    @Override
    public String put(String key, String value, int expireTime) {
        Assert.isTrue(StringUtils.isBlank(key), "cache key must not be empty.");
        Assert.notNull(value, "cache value must not be null.");
        Assert.isTrue(expireTime < 0, "cache expire time must not be negative.");
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        log.debug("Redis cache [{}, {}, {}]", key, value, expireTime);

        // set cache
        if (expireTime != 0) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }

        // return value
        return get(key);
    }

    /**
     * remove cache by key
     *
     * @param key cache key
     */
    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
        log.debug("Redis cache remove [{}]", key);
    }

    /**
     * remove cache by keys
     *
     * @param keys cache key list
     */
    @Override
    public void remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * read cache
     *
     * @param key cache key, not null
     * @return cache content
     */
    @Override
    public String get(String key) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }
}
