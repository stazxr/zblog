package com.github.stazxr.zblog.bas.cache.util;

import com.github.stazxr.zblog.bas.cache.Cache;

/**
 * 全局缓存助手类，用于对缓存进行操作。
 * <p>
 * 通过静态方法提供对缓存的基本操作，包括放入、获取、删除缓存项。
 * 需要确保在使用前已正确配置和初始化缓存。
 * </p>
 *
 * @author SunTao
 * @since 2024-08-22
 */
public class GlobalCache {
    private static Cache<String, String> cache;

    /**
     * 设置缓存实例
     *
     * @param cache 缓存实例
     */
    public synchronized static void setCache(Cache<String, String> cache) {
        if (GlobalCache.cache == null) {
            // 只能在项目启动时设置一次
            GlobalCache.cache = cache;
        }
    }

    /**
     * 将对象放入缓存中，使用默认的失效时长。
     *
     * @param key   缓存的键
     * @param value 缓存的值
     */
    public static synchronized void put(String key, String value) {
        checkCacheInitialized();
        cache.put(key, value);
    }

    /**
     * 将对象放入缓存中，使用指定的失效时长。
     *
     * @param key     缓存的键
     * @param value   缓存的值
     * @param timeout 失效时长，单位为毫秒
     */
    public static synchronized void put(String key, String value, long timeout) {
        checkCacheInitialized();
        cache.put(key, value, timeout);
    }

    /**
     * 从缓存中移除指定的键。
     *
     * @param keys 要移除的键列表
     */
    public static synchronized void remove(String... keys) {
        checkCacheInitialized();
        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                cache.remove(key);
            }
        }
    }

    /**
     * 从缓存中获取指定的值。
     *
     * @param key 缓存的键
     * @return 与键关联的值，如果键不存在则返回 {@code null}
     */
    public static synchronized String get(String key) {
        checkCacheInitialized();
        return cache.get(key);
    }

    /**
     * 从缓存中获取指定的值并同时将其移除。
     *
     * @param key 缓存的键
     * @return 与键关联的值，如果键不存在则返回 {@code null}
     */
    public static synchronized String getThenRemove(String key) {
        checkCacheInitialized();
        String result = cache.get(key);
        cache.remove(key);
        return result;
    }

    /**
     * 检查缓存是否已初始化，如果未初始化则抛出异常。
     */
    private static void checkCacheInitialized() {
        if (cache == null) {
            throw new IllegalStateException("Cache not initialized. Please configure and initialize cache.");
        }
    }
}