package com.github.stazxr.zblog.bas.cache.memory;

import com.github.stazxr.zblog.bas.cache.BaseCache;
import com.github.stazxr.zblog.util.collection.TimeMap;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 基于内存的缓存实现，使用 {@link TimeMap} 进行缓存存储和管理。
 *
 * @param <K> 键类型
 * @param <V> 值类型
 *
 * @author SunTao
 * @since 2024-08-22
 */
public class MemoryCache<K, V> extends BaseCache<K, V> {
    protected final TimeMap<K, V> cacheMap = new TimeMap<>();

    /**
     * 本地锁池（避免全局锁）
     */
    private final ConcurrentHashMap<K, Object> lockMap = new ConcurrentHashMap<>();

    /**
     * 获取缓存值
     *
     * @param key 缓存键
     * @return 缓存值，不存在返回 null
     */
    @Override
    public V get(K key) {
        return cacheMap.get(key);
    }

    /**
     * 放入缓存
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    @Override
    public void put(K key, V value, long timeout, TimeUnit unit) {
        if (value == null) {
            remove(key);
            return;
        }

        if (timeout > 0) {
            long millis = unit.toMillis(timeout);
            cacheMap.put(key, value, millis);
        } else {
            cacheMap.put(key, value);
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
            cacheMap.remove(key);
            lockMap.remove(key);
        }
    }

    /**
     * 仅当缓存中不存在指定 key 时，才写入缓存值（原子语义）。
     *
     * <p>并发情况下通过 key 级锁保证原子性。</p>
     *
     * @param key 缓存键
     * @param value 要写入的缓存值
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return
     * <ul>
     * <li>true：写入成功（key 原本不存在）</li>
     * <li>false：key 已存在，未写入</li>
     * </ul>
     * @throws IllegalArgumentException if value = null
     */
    @Override
    public Boolean setIfAbsent(K key, V value, long timeout, TimeUnit unit) {
        if (value == null) {
            throw new IllegalArgumentException("cache value must not be null");
        }

        // 快速路径，避免加锁
        if (cacheMap.get(key) != null) {
            return false;
        }

        // key 级别锁，避免并发写入
        synchronized (getLock(key)) {
            if (cacheMap.get(key) != null) {
                return false;
            }

            if (timeout > 0) {
                cacheMap.put(key, value, unit.toMillis(timeout));
            } else {
                cacheMap.put(key, value);
            }

            return true;
        }
    }

    /**
     * 获取缓存中的值；当缓存不存在或已过期时，通过加载器加载数据并写入缓存。
     *
     * @param key     缓存键
     * @param loader  数据加载器，用于在缓存未命中时获取数据
     * @param timeout 缓存过期时间
     * @param unit    过期时间单位
     * @return 缓存中的值或加载得到的值；若加载结果为空则返回 {@code null}
     */
    @Override
    public V getOrLoad(K key, Supplier<V> loader, long timeout, TimeUnit unit) {
        // 1, 先读缓存
        V value = cacheMap.get(key);
        if (value != null) {
            return value;
        }

        // 2, 本地 key 级别锁（防止同一 key 被并发击穿）
        synchronized (getLock(key)) {
            // 3, 双重检查
            value = cacheMap.get(key);
            if (value != null) {
                return value;
            }

            // 4, 加载数据
            value = loader.get();

            // 5, 写入缓存（遵循 put 的 TTL 语义）
            if (value != null) {
                if (timeout > 0) {
                    cacheMap.put(key, value, unit.toMillis(timeout));
                } else {
                    cacheMap.put(key, value);
                }
            }

            return value;
        }
    }

    private Object getLock(K key) {
        return lockMap.computeIfAbsent(key, k -> new Object());
    }
}
