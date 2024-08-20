package com.github.stazxr.zblog.util.collection;

import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.UuidUtils;
import com.github.stazxr.zblog.util.thread.ThreadUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个带有过期时间功能的映射类。
 *
 * @param <K> 键的类型
 * @param <V> 值的类型
 *
 * @author SunTao
 * @since 2024-08-21
 */
public class TimeMap<K, V> {
    private final ConcurrentHashMap<K, V> dataMap;

    private final ConcurrentHashMap<K, Instant> expiredDateMap;

    /**
     * 创建一个新的 TimeMap 实例，使用默认的线程池名称。
     */
    public TimeMap() {
        this("TimeMapExpiredCheckThread-" + UuidUtils.generateShortUuid());
    }

    /**
     * 创建一个新的 TimeMap 实例，并指定线程的名称。
     *
     * @param name 线程的名称
     */
    public TimeMap(String name) {
        dataMap = new ConcurrentHashMap<>();
        expiredDateMap = new ConcurrentHashMap<>();
        startExpiredCheckThread(name);
    }

    /**
     * 将键值对存储到映射中，并指定过期时间。
     *
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间（秒）
     */
    public void put(K key, V value, int expireTime) {
        Assert.isTrue(expireTime < 0, "expireTime must not be negative");
        dataMap.put(key, value);
        if (expireTime != 0) {
            Instant expireDate = Instant.now().plus(Duration.ofSeconds(expireTime));
            expiredDateMap.put(key, expireDate);
        }
    }

    /**
     * 获取指定键的值。如果该键已经过期，则返回 null。
     *
     * @param key 键
     * @return 值，如果键过期则返回 null
     */
    public V get(K key) {
        Instant expireDate = expiredDateMap.get(key);
        if (expireDate != null && Instant.now().isAfter(expireDate)) {
            // 惰性删除过期的键
            dataMap.remove(key);
            expiredDateMap.remove(key);
        }
        return dataMap.get(key);
    }

    /**
     * 从映射中移除指定的键。
     *
     * @param key 键
     */
    public void remove(K key) {
        dataMap.remove(key);
        expiredDateMap.remove(key);
    }

    /**
     * 启动一个定期检查过期项的任务。
     */
    private void startExpiredCheckThread(String name) {
        ThreadUtils.runThread(name, this::checkExpiredDateMap);
    }

    /**
     * 检查并删除过期的键值对。
     */
    @SuppressWarnings("all")
    private void checkExpiredDateMap() {
        while (true) {
            ThreadUtils.sleepMinute(5);
            if (expiredDateMap.size() > 0) {
                Instant now = Instant.now();
                expiredDateMap.forEach((key, expireDate) -> {
                    if (now.isAfter(expireDate)) {
                        dataMap.remove(key);
                        expiredDateMap.remove(key);
                    }
                });
            }
        }
    }
}
