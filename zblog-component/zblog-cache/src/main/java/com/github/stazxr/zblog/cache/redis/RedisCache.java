package com.github.stazxr.zblog.cache.redis;

import com.github.stazxr.zblog.cache.BaseCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 的缓存实现，使用 {@link RedisTemplate} 进行缓存存储和管理。
 *
 * @param <K> 键类型
 * @param <V> 值类型
 *
 * @author SunTao
 * @since 2024-08-22
 */
@Slf4j
public class RedisCache<K, V> extends BaseCache<K, V> {
    private static final long serialVersionUID = -3678908379909346494L;

    private RedisTemplate<K, V> redisTemplate;

    /**
     * 将对象加入缓存，使用指定的失效时长。
     *
     * @param key     键
     * @param value   要缓存的对象
     * @param timeout 失效时长，单位为毫秒
     */
    @Override
    public void put(K key, V value, long timeout) {
        log.trace("RedisCachePut {}[{}]: {}", key, timeout, value);
        ValueOperations<K, V> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        if (timeout > 0) {
            redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 从缓存中获取对象。如果对象不存在或已过期，则返回 {@code null}。
     *
     * @param key 键
     * @return 与键关联的对象，若不存在或已过期则返回 {@code null}
     */
    @Override
    public V get(K key) {
        ValueOperations<K, V> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 从缓存中移除对象。
     *
     * @param key 键
     */
    @Override
    public void remove(K key) {
        log.trace("RedisCacheRemove: {}", key);
        redisTemplate.delete(key);
    }

    /**
     * 清空缓存中的所有对象。
     */
    @Override
    public void clear() {
        log.info("RedisCacheClear");
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        if (connectionFactory != null) {
            connectionFactory.getConnection().flushDb();
        }
    }

    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
