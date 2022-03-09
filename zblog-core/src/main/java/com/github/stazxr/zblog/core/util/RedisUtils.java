package com.github.stazxr.zblog.core.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存管理
 *
 * @author SunTao
 * @since 2020-12-10
 */
@Slf4j
@Component
@AllArgsConstructor
public class RedisUtils {
    private final StringRedisTemplate redisTemplate;

    /**
     * 写入缓存
     *
     * @param key   Key
     * @param value Value
     * @return boolean
     */
    public boolean set(String key, String value) {
        boolean result = false;
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            log.error("RedisManager.set failed", e);
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key   Key
     * @param value Value
     * @param expireTime 超时时间，单位秒
     * @return boolean
     */
    public boolean set(String key, String value, int expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("RedisManager.set failed", e);
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key   Key
     * @param value Value
     * @param expireTime 超时时间，单位秒
     * @param timeUnit   超时单位
     * @return boolean
     */
    public boolean set(String key, String value, int expireTime, TimeUnit timeUnit) {
        boolean result = false;
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            log.error("RedisManager.set failed", e);
        }
        return result;
    }

    /**
     * 批量删除
     *
     * @param keys Keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern 格式
     */
    public void removePattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key Key
     */
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 读取缓存
     *
     * @param key Key
     * @return String
     */
    public String get(String key) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 哈希 添加
     *
     * @param key     Key
     * @param hashKey hashKey
     * @param value Value
     */
    public void hashSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希获取数据
     *
     * @param key     Key
     * @param hashKey hashKey
     * @return Object
     */
    public Object hashGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 列表添加
     *
     * @param key key
     * @param value value
     */
    public void listPush(String key, String value) {
        ListOperations<String, String> list = redisTemplate.opsForList();
        list.rightPush(key, value);
    }

    /**
     * 列表获取
     *
     * @param k  key
     * @param start  开始
     * @param end 结束
     * @return Objects
     */
    public List<String> listRange(String k, long start, long end) {
        ListOperations<String, String> list = redisTemplate.opsForList();
        return list.range(k, start, end);
    }

    /**
     * 集合添加
     *
     * @param key   key
     * @param value value
     */
    public void add(String key, String value) {
        SetOperations<String, String> set = redisTemplate.opsForSet();
        set.add(key, value);
    }

    /**
     * 集合获取
     *
     * @param key key
     * @return Set<Object>
     */
    public Set<String> setMembers(String key) {
        SetOperations<String, String> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     *
     * @param key    key
     * @param value  value
     * @param score  score
     */
    public void zAdd(String key, String value, double score) {
        ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();
        zSet.add(key, value, score);
    }

    /**
     * 有序集合获取
     *
     * @param key    key
     * @param score  score
     * @param score1 score1
     * @return Object
     */
    public Set<String> rangeByScore(String key, double score, double score1) {
        ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();
        return zSet.rangeByScore(key, score, score1);
    }
}
