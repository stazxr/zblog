package com.github.stazxr.zblog.bas.security.jwt.storage.impl;

import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.bas.security.jwt.storage.TokenPayload;

import java.util.concurrent.TimeUnit;

/**
 * 该类实现了 {@link JwtTokenStorage} 接口，提供了对令牌进行存储、获取、过期处理的功能。
 * 该实现基于缓存 {@link GlobalCache} 存储 JWT。
 *
 * @author SunTao
 * @since 2022-01-19
 */
public class JwtTokenStorageImpl implements JwtTokenStorage {
    /**
     * 令牌信息缓存键模板。
     */
    private static final String RTK_TOKEN_CACHE_KEY = "LOGIN:TOKEN:%s";

    /**
     * 存储令牌。
     *
     * @param uid      用户的唯一标识符（通常是用户 ID）
     * @param token    令牌信息
     * @param duration 令牌的有效时间，单位为秒
     */
    @Override
    public void put(String uid, TokenPayload token, int duration) {
        GlobalCache.put(buildKey(uid), token, duration, TimeUnit.SECONDS);
    }

    /**
     * 获取令牌。
     *
     * @param uid 用户的唯一标识符（通常是用户 ID）
     * @return 令牌信息，若不存在则返回 null
     */
    @Override
    public TokenPayload get(String uid) {
        return GlobalCache.get(buildKey(uid));
    }

    /**
     * 移除令牌。
     *
     * @param uid 用户的唯一标识符（通常是用户 ID）
     */
    @Override
    public void remove(String uid) {
        GlobalCache.remove(buildKey(uid));
    }

    private String buildKey(String uid) {
        return String.format(RTK_TOKEN_CACHE_KEY, uid);
    }
}
