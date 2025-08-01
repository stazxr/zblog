package com.github.stazxr.zblog.util.collection;

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
        this("TimeMapExpiredCheckThread-" + UuidUtils.gen8BitUuidStr());
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
     * 将键值对存储到映射中（不会过期）。
     *
     * @param key        键
     * @param value      值
     */
    public V put(K key, V value) {
        return put(key, value, -1);
    }

    /**
     * 将键值对存储到映射中，并指定过期时间。
     *
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间（毫秒）
     */
    public V put(K key, V value, long expireTime) {
        if (key != null) {
            if (value == null) {
                // 删除
                expiredDateMap.remove(key);
                dataMap.remove(key);
            } else {
                // 新增
                if (expireTime >= 0) {
                    Instant expireDate = Instant.now().plus(Duration.ofMillis(expireTime));
                    expiredDateMap.put(key, expireDate);
                }
                dataMap.put(key, value);
                return value;
            }
        }

        return null;
    }

    /**
     * 获取指定键的值。如果该键已经过期，则返回 null。
     *
     * @param key 键
     * @return 值，如果键过期则返回 null
     */
    public V get(K key) {
        if (key != null) {
            Instant expireDate = expiredDateMap.get(key);
            if (expireDate != null && Instant.now().isAfter(expireDate)) {
                // 惰性删除过期的键
                dataMap.remove(key);
                expiredDateMap.remove(key);
            }
            return dataMap.get(key);
        }

        return null;
    }

    /**
     * 判断是否包含键。
     *
     * @param key 键
     * @return boolean，如果键存在则返回 true
     */
    public boolean containsKey(K key) {
        if (key != null) {
            Instant expireDate = expiredDateMap.get(key);
            if (expireDate != null && Instant.now().isAfter(expireDate)) {
                // 惰性删除过期的键
                dataMap.remove(key);
                expiredDateMap.remove(key);
            }
            return dataMap.containsKey(key);
        }

        return false;
    }

    /**
     * 从映射中移除指定的键。
     *
     * @param key 键
     */
    public void remove(K key) {
        if (key != null) {
            dataMap.remove(key);
            expiredDateMap.remove(key);
        }
    }

    /**
     * 清空缓存。
     */
    public void clear() {
        dataMap.clear();
        expiredDateMap.clear();
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
