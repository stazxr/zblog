package com.github.stazxr.zblog.cache;

/**
 * 基础缓存实现，提供了缓存的默认实现和通用方法。
 *
 * @param <K> 键类型
 * @param <V> 值类型
 *
 * @author SunTao
 * @since 2024-08-21
 */
public abstract class BaseCache<K, V> implements Cache<K, V> {
    private static final long serialVersionUID = -7601261922896894513L;

    /**
     * 返回缓存对象的默认失效时长，单位为毫秒。
     * 值为 {@code 0} 表示未设置失效时长。
     *
     * @return 默认失效时长，单位为毫秒
     */
    @Override
    public long timeout() {
        return 0;
    }

    /**
     * 将对象加入缓存，使用默认失效时长。
     *
     * @param key   键
     * @param value 要缓存的对象
     * @see #put(Object, Object, long)
     */
    @Override
    public void put(K key, V value) {
        put(key, value, timeout());
    }

    /**
     * 检查缓存中是否包含指定的键。
     *
     * @param key 要检查的键
     * @return 如果缓存中包含指定的键，则返回 {@code true}，否则返回 {@code false}
     */
    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }
}
