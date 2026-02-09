package com.github.stazxr.zblog.bas.cache.redis;

import com.github.stazxr.zblog.bas.cache.BaseCache;
import com.github.stazxr.zblog.bas.exception.SystemException;
import com.github.stazxr.zblog.util.StringUtils;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 基于 Redis 的缓存实现，使用 {@link RedisTemplate} 进行缓存存储和管理。
 *
 * @param <K> 键类型
 * @param <V> 值类型
 *
 * @author SunTao
 * @since 2024-08-22
 */
public class RedisCache<K, V> extends BaseCache<K, V> {
    /** 防击穿锁前缀 */
    private static final String LOCK_PREFIX = "redisCacheLock:";

    /** 空值占位符（防穿透） */
    private static final String NULL_VALUE = "__NULL__";

    /** 默认锁超时时间（秒） */
    private static final Duration LOCK_TTL = Duration.ofSeconds(10);

    /** Lua 脚本：仅当值匹配时才删除锁 */
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setScriptText(
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        " return redis.call('del', KEYS[1]) " +
                        "else return 0 end"
        );
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取缓存值
     *
     * @param key 键
     * @return 当缓存中存储的是空值占位符时，返回 null
     */
    @Override
    @SuppressWarnings("unchecked")
    public V get(K key) {
        Object value = redisTemplate.opsForValue().get(buildKey(key));
        return NULL_VALUE.equals(value) ? null : (V) value;
    }

    /**
     * 模糊查询缓存池数据
     *
     * @param pattern 查询参数，如 rl:*
     * @return 缓存池数据
     */
    @Override
    @SuppressWarnings("all")
    public Map<K, V> scan(String pattern) {
        Map<K, V> result = new HashMap<>();
        if (StringUtils.isBlank(pattern)) {
            return result;
        }

        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(1000).build();
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            try (Cursor<byte[]> cursor = connection.scan(options)) {
                while (cursor.hasNext()) {
                    byte[] keyBytes = cursor.next();
                    K key = (K) redisTemplate.getStringSerializer().deserialize(keyBytes);
                    V value = (V) redisTemplate.opsForValue().get(key);
                    result.put(key, value);
                }
            } catch (Exception e) {
                throw new SystemException("Redis scan error", e);
            }
            return null;
        });

        return result;
    }

    /**
     * 写入缓存
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    @Override
    public void put(K key, V value, long timeout, TimeUnit unit) {
        String redisKey = buildKey(key);
        Object storeValue = value == null ? NULL_VALUE : value;
        if (timeout > 0) {
            redisTemplate.opsForValue().set(redisKey, storeValue, timeout, unit);
        } else {
            redisTemplate.opsForValue().set(redisKey, storeValue);
        }
    }

    /**
     * 删除缓存
     *
     * @param key 缓存键
     */
    @Override
    public void remove(K key) {
        if (key != null) {
            redisTemplate.delete(buildKey(key));
        }
    }

    /**
     * 仅当 key 不存在时写入缓存（原子语义）。
     *
     * @param key 缓存键
     * @param value 要写入的缓存值
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return
     * <ul>
     * <li>true：写入成功</li>
     * <li>false：key 已存在，写入失败</li>
     * </ul>
     * @throws IllegalArgumentException if value = null
     */
    @Override
    public Boolean setIfAbsent(K key, V value, long timeout, TimeUnit unit) {
        if (value == null) {
            throw new IllegalArgumentException("cache value must not be null");
        }

        String redisKey = buildKey(key);
        if (timeout > 0) {
            return redisTemplate.opsForValue().setIfAbsent(redisKey, value, timeout, unit);
        } else {
            return redisTemplate.opsForValue().setIfAbsent(redisKey, value);
        }
    }

    /**
     * 获取缓存；若不存在则加载数据并写入缓存。
     *
     * <p>流程：</p>
     * <ol>
     *     <li>先读缓存</li>
     *     <li>未命中尝试获取分布式锁</li>
     *     <li>双重检查缓存</li>
     *     <li>调用 loader 加载数据</li>
     *     <li>写入缓存</li>
     * </ol>
     *
     * @param key     缓存键
     * @param loader  数据加载器，用于在缓存未命中时获取数据
     * @param timeout 缓存过期时间
     * @param unit    过期时间单位
     * @return 缓存中的值或加载得到的值；若加载结果为空则返回 {@code null}
     */
    @Override
    @SuppressWarnings("unchecked")
    public V getOrLoad(K key, Supplier<V> loader, long timeout, TimeUnit unit) {
        String redisKey = buildKey(key);

        // 1, 先读缓存
        V value = (V) redisTemplate.opsForValue().get(redisKey);
        if (value != null) {
            return NULL_VALUE.equals(value) ? null : (V) value;
        }

        // 2, 尝试获取分布式锁（防缓存击穿）
        String lockKey = LOCK_PREFIX + redisKey;
        String lockValue = UUID.randomUUID().toString();
        boolean locked = Boolean.TRUE.equals(
            redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, LOCK_TTL)
        );

        try {
            if (locked) {
                // 3, 双重检查
                value = (V) redisTemplate.opsForValue().get(redisKey);
                if (value != null) {
                    return NULL_VALUE.equals(value) ? null : value;
                }

                // 4, 加载数据
                V newValue = loader.get();
                put(key, newValue, timeout, unit);
                return newValue;
            } else {
                // 5, 未拿到锁，短暂等待后重试一次
                sleep50();
                value = (V) redisTemplate.opsForValue().get(redisKey);
                return NULL_VALUE.equals(value) ? null : value;
            }
        } finally {
            if (locked) {
                redisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(lockKey), lockValue);
            }
        }
    }

    /**
     * 获取TTL
     *
     * @param key 键
     * @return TTL
     * | 返回值 | 含义      |
     * | --- | ------- |
     * | >0  | 剩余时间    |
     * | -1  | 永不过期    |
     * | -2  | key 不存在 |
     */
    @Override
    public Long getTtl(K key) {
        return redisTemplate.getExpire(buildKey(key));
    }

    /**
     * 构建 Redis Key
     */
    protected String buildKey(K key) {
        return String.valueOf(key);
    }

    private void sleep50() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
