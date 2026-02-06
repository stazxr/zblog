package com.github.stazxr.zblog.bas.cache.util;

import com.github.stazxr.zblog.bas.cache.Cache;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 全局缓存工具类。
 *
 * @author SunTao
 * @since 2024-08-22
 */
public final class GlobalCache {
    /**
     * 全局缓存实例（必须在应用启动时初始化）
     */
    private static Cache<String, Object> CACHE;

    private GlobalCache() {
        // 工具类禁止实例化
    }

    /**
     * 初始化全局缓存，只能初始化一次
     */
    public static synchronized void init(Cache<String, Object> cache) {
        if (CACHE != null) {
            return; // 已初始化，不覆盖
        }
        if (cache == null) {
            throw new IllegalArgumentException("Cache instance cannot be null");
        }
        CACHE = cache;
    }

    private static void checkInitialized() {
        if (CACHE == null) {
            throw new IllegalStateException("GlobalCache not initialized. Please call GlobalCache.init(...) at startup.");
        }
    }

    public static void put(String key, Object value) {
        checkInitialized();
        CACHE.put(key, value, -1, TimeUnit.MILLISECONDS);
    }

    public static void put(String key, Object value, long timeout, TimeUnit unit) {
        checkInitialized();
        CACHE.put(key, value, timeout, unit);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        checkInitialized();
        return (T) CACHE.get(key);
    }

    public static void remove(String... keys) {
        checkInitialized();
        if (keys != null) {
            for (String key : keys) {
                CACHE.remove(key);
            }
        }
    }

    public static Boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
        checkInitialized();
        return CACHE.setIfAbsent(key, value, timeout, unit);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getOrLoad(String key, Supplier<T> loader, long timeout, TimeUnit unit) {
        checkInitialized();
        return (T) CACHE.getOrLoad(key, (Supplier<Object>) loader, timeout, unit);
    }

    /**
     * TTL 随机抖动，避免雪崩
     */
    public static long jitter(long timeout) {
        long delta = Math.max(1, timeout / 10);
        return timeout + ThreadLocalRandom.current().nextLong(delta);
    }
}
