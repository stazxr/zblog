package com.github.stazxr.zblog.cache.memory;

import com.github.stazxr.zblog.cache.BaseCache;
import com.github.stazxr.zblog.util.collection.TimeMap;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于内存的缓存实现，使用 {@link TimeMap} 进行缓存存储和管理。
 *
 * @param <K> 键类型
 * @param <V> 值类型
 *
 * @author SunTao
 * @since 2024-08-22
 */
@Slf4j
public class MemoryCache<K, V> extends BaseCache<K, V> {
    private static final long serialVersionUID = -8726954118158129110L;

    protected final TimeMap<K, V> cacheMap = new TimeMap<>();

    /**
     * 将对象加入缓存，使用指定的失效时长。
     *
     * @param key     键
     * @param value   要缓存的对象
     * @param timeout 失效时长，单位为毫秒
     */
    @Override
    public void put(K key, V value, long timeout) {
        if (value == null) {
            remove(key);
        } else {
            log.trace("MemoryCachePut {}[{}]: {}", key, timeout, value);
            cacheMap.put(key, value, timeout);
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
        return cacheMap.get(key);
    }

    /**
     * 从缓存中移除对象。
     *
     * @param key 键
     */
    @Override
    public void remove(K key) {
        log.trace("MemoryCacheRemove: {}", key);
        cacheMap.remove(key);
    }

    /**
     * 清空缓存中的所有对象。
     */
    @Override
    public void clear() {
        log.info("MemoryCacheClear");
        cacheMap.clear();
    }
}
