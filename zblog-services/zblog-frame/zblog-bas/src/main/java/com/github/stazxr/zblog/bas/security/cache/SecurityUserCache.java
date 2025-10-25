package com.github.stazxr.zblog.bas.security.cache;

import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户信息缓存处理类。
 * <p>
 * 该类用于缓存和管理用户的详细信息（{@link SecurityUser}），以便在不需要频繁访问数据库或其他慢速存储的情况下，
 * 快速获取用户的相关数据。缓存采用线程安全的 {@link ConcurrentHashMap} 来存储用户信息。
 * </p>
 *
 * <strong>注意：</strong>缓存中的用户信息在应用程序生命周期内有效，在清空缓存或重启应用时会丢失。
 *
 * @author SunTao
 * @since 2024-11-14
 */
@Slf4j
public class SecurityUserCache {
    /**
     * 用户信息缓存
     */
    private static final Map<String, SecurityUser> CACHE = new ConcurrentHashMap<>();

    /**
     * 获取缓存中的用户信息。
     *
     * @param userId 用户标识
     * @return 如果缓存中存在该用户，返回 {@link SecurityUser} 对象，否则返回 null
     */
    public static SecurityUser get(String userId) {
        // 从缓存中获取用户信息
        return CACHE.get(userId);
    }

    /**
     * 将用户信息放入缓存。
     *
     * @param userId       用户标识
     * @param securityUser 用户信息
     */
    public static SecurityUser put(String userId, SecurityUser securityUser) {
        if (userId != null && securityUser != null) {
            CACHE.put(userId, securityUser);
            if (log.isDebugEnabled()) {
                log.debug("add user cache: {}", userId);
            }
            return securityUser;
        }
        return null;
    }

    /**
     * 从缓存中移除指定的用户信息。
     *
     * @param userId 用户标识
     */
    public static void remove(String userId) {
        if (userId != null) {
            CACHE.remove(userId);
            if (log.isDebugEnabled()) {
                log.debug("remove user cache: {}", userId);
            }
        }
    }

    /**
     * 清空缓存中的所有用户信息。
     */
    public static void clean() {
        // 清空整个缓存
        CACHE.clear();
        if (log.isDebugEnabled()) {
            log.debug("clear user cache");
        }
    }
}
