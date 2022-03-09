package com.github.stazxr.zblog.core.cache;

/**
 * 系统内置缓存接口规范
 *
 * @author SunTao
 * @since 2022-03-02
 */
public interface Cache {
    /**
     * write into cache
     *
     * @param key   cache key, not null
     * @param value cache content
     * @return param value
     */
    String put(String key, String value);

    /**
     * write into cache
     *
     * @param key   cache key, not null
     * @param value cache content
     * @param expireTime valid time, unit seconds
     * @return param value
     */
    String put(String key, String value, int expireTime);

    /**
     * remove cache by key
     *
     * @param key cache key, not null
     */
    void remove(String key);

    /**
     * remove cache by keys
     *
     * @param key cache key list
     */
    void remove(String... key);

    /**
     * read cache
     *
     * @param key cache key, not null
     * @return cache content
     */
    String get(String key);
}
