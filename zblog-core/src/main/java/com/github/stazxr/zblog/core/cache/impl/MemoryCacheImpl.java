package com.github.stazxr.zblog.core.cache.impl;

import com.github.stazxr.zblog.core.config.properties.CacheConfigProperties;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import com.github.stazxr.zblog.util.thread.ThreadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统内置缓存接口规范的内存实现方式
 *
 * @author SunTao
 * @since 2022-03-02
 */
@Slf4j
@RequiredArgsConstructor
public class MemoryCacheImpl extends BaseCache {
    /**
     * sys cache
     */
    private static final CacheMap<String, String> CACHE_MAP = CacheMap.getInstance();

    private final CacheConfigProperties cacheConfigProperties;

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
        CACHE_MAP.cachePut(key, value, expireTime);
        return get(key);
    }

    /**
     * remove cache by key
     *
     * @param key cache key
     */
    @Override
    public void remove(String key) {
        CACHE_MAP.cacheRemove(key);
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
        return CACHE_MAP.cacheGet(key);
    }

    /**
     * 定义一个带缓存特性的Map，防止缓存越来越大
     *
     * @param <K> key
     * @param <V> value
     */
    private static final class CacheMap<K, V> extends ConcurrentHashMap<K, V> {
        private static CacheMap<String, String> instance;

        // cache the key expired time
        private static CacheMap<String, Date> expiredDateMap;

        private CacheMap() {
        }

        public static CacheMap<String, String> getInstance() {
            if (null != instance) {
                return instance;
            }

            synchronized (CacheMap.class) {
                if (null == instance) {
                    startExpiredCheckThread();
                    instance = new CacheMap<>();
                }
            }
            return instance;
        }

        public void cachePut(String key, String value, int expireTime) {
            Assert.isTrue(StringUtils.isBlank(key), "cache key must not be empty.");
            Assert.notNull(value, "cache value must not be null.");
            Assert.isTrue(expireTime < 0, "cache expire time must not be negative.");

            instance.put(key, value);
            String expireDateRemark = "0";
            if (expireTime != 0) {
                // 计算缓存过期时间
                Date expireDate = calculateExpireDate(expireTime);
                expiredDateMap.put(key, expireDate);
                expireDateRemark = DateUtils.format(expireDate);
            }

            log.info("memory cache put [{}, {}, {}]", key, value, expireDateRemark);
        }

        public void cacheRemove(String key) {
            instance.remove(key);
            expiredDateMap.remove(key);
            log.info("memory cache remove [{}]", key);
        }

        public String cacheGet(String key) {
            Date expireDate = expiredDateMap.get(key);
            if (expireDate != null && new Date().after(expireDate)) {
                // expired
                instance.remove(key);
                expiredDateMap.remove(key);
            }

            return instance.get(key);
        }

        @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
        private static void startExpiredCheckThread() {
            expiredDateMap = new CacheMap<>();

            Thread thread = new Thread(() -> {
                while (true) {
                    // 每10分种遍历一次 expiredDateMap
                    checkExpiredMap();
                    ThreadUtils.sleepMinute(10);
                }
            });

            thread.start();
        }

        private static void checkExpiredMap() {
            if (expiredDateMap.size() > 0) {
                Set<String> expiredKeys = new HashSet<>();
                for (String key : expiredDateMap.keySet()) {
                    Date expireDate = expiredDateMap.get(key);
                    if (new Date().after(expireDate)) {
                        // 缓存超时
                        expiredKeys.add(key);
                    }
                }

                if (expiredKeys.size() > 0) {
                    for (String expiredKey : expiredKeys) {
                        expiredDateMap.remove(expiredKey);
                        instance.remove(expiredKey);
                    }
                }
            }
        }

        private Date calculateExpireDate(int duration) {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.SECOND, duration);
            return instance.getTime();
        }
    }
}
