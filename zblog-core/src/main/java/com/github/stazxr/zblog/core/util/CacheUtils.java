package com.github.stazxr.zblog.core.util;

import com.github.stazxr.zblog.core.cache.Cache;

/**
 * 系统内置缓存工具类
 *
 * @author SunTao
 * @since 2022-03-02
 */
public class CacheUtils {
    private static Cache cache;

    private static Cache instance() {
        if (CacheUtils.cache == null) {
            CacheUtils.cache = SpringContextUtils.getBean("sysCache", Cache.class);
        }
        return CacheUtils.cache;
    }

    /**
     * write into cache
     *
     * @param key   cache key, not null
     * @param value cache content
     * @return param value
     */
    public static String put(String key, String value) {
        return instance().put(key, value);
    }

    /**
     * write into cache
     *
     * @param key        cache key, not null
     * @param value      cache content
     * @param expireTime valid time, unit seconds
     * @return param value
     */
    public static String put(String key, String value, int expireTime) {
        return instance().put(key, value, expireTime);
    }

    /**
     * remove cache by key
     *
     * @param key cache key, not null
     */
    public static void remove(String key) {
        instance().remove(key);
    }

    /**
     * remove cache by keys
     *
     * @param key cache key list
     */
    public static void remove(String... key) {
        instance().remove(key);
    }

    /**
     * read cache
     *
     * @param key cache key, not null
     * @return cache content
     */
    public static String get(String key) {
        return instance().get(key);
    }

    /**
     * read cache then remove cache
     *
     * @param key cache key, not null
     * @return cache content
     */
    public static String getThenRemove(String key) {
        String result = instance().get(key);
        instance().remove(key);
        return result;
    }
}
