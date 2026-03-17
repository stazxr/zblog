package com.github.stazxr.zblog.bas.security.cache;

import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
public final class SecurityUserCache {
    private static final Logger log = LoggerFactory.getLogger(SecurityUserCache.class);

    private static final String KEY_PREFIX = "user:";

    /**
     * 默认缓存时间（30分钟）
     */
    private static final long DEFAULT_TIMEOUT = 30;

    /**
     * 空用户默认缓存时间（30分钟）
     */
    private static final long DEFAULT_EMPTY_TIMEOUT = 2;

    private static final TimeUnit DEFAULT_UNIT = TimeUnit.MINUTES;

    /**
     * 空用户对象
     */
    private static final SecurityUser EMPTY_USER = new SecurityUser();

    /**
     * 获取缓存中的用户信息。
     *
     * @param userId 用户标识
     * @return 如果缓存中存在该用户，返回 {@link SecurityUser} 对象，否则返回 null
     */
    public static SecurityUser get(String userId) {
        if (userId == null) {
            return null;
        }
        SecurityUser securityUser = GlobalCache.get(buildKey(userId));
        return securityUser == EMPTY_USER ? null : securityUser;
    }

    /**
     * 获取或加载缓存（推荐方式）。
     *
     * @param userId 用户标识
     * @param loader 用户加载函数
     * @return 如果缓存中存在该用户，返回 {@link SecurityUser} 对象，否则加载用户信息
     */
    public static SecurityUser getOrLoad(String userId, Function<String, SecurityUser> loader) {
        String key = buildKey(userId);
        Object cached = GlobalCache.get(key);
        if (cached != null) {
            return cached == EMPTY_USER ? null : (SecurityUser) cached;
        }

        // load user
        SecurityUser securityUser = loader.apply(userId);
        long ttl = (securityUser != null) ? DEFAULT_TIMEOUT : DEFAULT_EMPTY_TIMEOUT;
        GlobalCache.put(key, securityUser != null ? securityUser : EMPTY_USER, GlobalCache.jitter(ttl), DEFAULT_UNIT);
        return securityUser;
    }

    /**
     * 将用户信息放入缓存。
     *
     * @param userId       用户标识
     * @param securityUser 用户信息
     */
    public static SecurityUser put(String userId, SecurityUser securityUser) {
        if (userId != null && securityUser != null) {
            GlobalCache.put(buildKey(userId), securityUser, GlobalCache.jitter(DEFAULT_TIMEOUT), DEFAULT_UNIT);
            log.debug("add user cache: {}", userId);
        }
        return securityUser;
    }

    /**
     * 删除缓存（踢人/权限变更用）。
     *
     * @param userId 用户标识
     */
    public static void remove(String userId) {
        if (userId != null) {
            GlobalCache.remove(buildKey(userId));
            log.debug("remove user cache: {}", userId);
        }
    }

    private static String buildKey(String userId) {
        return KEY_PREFIX + userId;
    }
}
