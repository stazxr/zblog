package com.github.stazxr.zblog.bas.security.jwt.storage.impl;

import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.util.Assert;

/**
 * 该类实现了 {@link JwtTokenStorage} 接口，提供了对访问令牌（accessToken）和刷新令牌（refreshToken）进行存储、获取、过期处理的功能。
 * 该实现基于缓存 {@link GlobalCache} 存储 JWT。
 *
 * @author SunTao
 * @since 2022-01-19
 */
public class JwtTokenStorageImpl implements JwtTokenStorage {
    /**
     * 存储访问令牌（accessToken）的缓存键模板。
     */
    private static final String ATK_TOKEN_CACHE_KEY = "jwt-atk:%s";

    /**
     * 存储刷新令牌（refreshToken）的缓存键模板。
     */
    private static final String RTK_TOKEN_CACHE_KEY = "jwt-rtk:%s";

    /**
     * 存储访问令牌（accessToken）。
     * <p>
     * 该方法将用户的访问令牌存储在缓存中，并为其设置有效时间。
     * </p>
     *
     * @param accessToken 需要存储的访问令牌
     * @param uid 用户的唯一标识符（通常是用户 ID）
     * @param duration 令牌的有效时间，单位为秒
     * @return 存储后的访问令牌
     * @throws IllegalArgumentException 如果传入的 uid 或 accessToken 为 null
     */
    @Override
    public String putAccessToken(String accessToken, String uid, int duration) {
        Assert.notNull(uid, "JwtTokenStorage put 'uid' is null");
        Assert.notNull(accessToken, "JwtTokenStorage put 'accessToken' is null");
        String utkCacheKey = String.format(ATK_TOKEN_CACHE_KEY, uid);
        GlobalCache.put(utkCacheKey, accessToken, duration);
        return getAccessToken(uid);
    }

    /**
     * 存储刷新令牌（refreshToken）。
     * <p>
     * 该方法将用户的刷新令牌存储在缓存中，并为其设置有效时间。
     * </p>
     *
     * @param refreshToken 需要存储的刷新令牌
     * @param uid 用户的唯一标识符（通常是用户 ID）
     * @param duration 令牌的有效时间，单位为秒
     * @return 存储后的刷新令牌
     * @throws IllegalArgumentException 如果传入的 uid 或 refreshToken 为 null
     */
    @Override
    public String putRefreshToken(String refreshToken, String uid, int duration) {
        Assert.notNull(uid, "JwtTokenStorage put 'uid' is null");
        Assert.notNull(refreshToken, "JwtTokenStorage put 'refreshToken' is null");
        String rtkCacheKey = String.format(RTK_TOKEN_CACHE_KEY, uid);
        GlobalCache.put(rtkCacheKey, refreshToken, duration);
        return getRefreshToken(uid);
    }

    /**
     * 获取访问令牌（accessToken）。
     * <p>
     * 该方法根据用户 ID 从缓存中获取访问令牌。如果令牌不存在或已过期，则返回 null。
     * </p>
     *
     * @param uid 用户的唯一标识符（通常是用户 ID）
     * @return 用户对应的访问令牌，若不存在则返回 null
     */
    @Override
    public String getAccessToken(String uid) {
        String utkCacheKey = String.format(ATK_TOKEN_CACHE_KEY, uid);
        return GlobalCache.get(utkCacheKey);
    }

    /**
     * 获取刷新令牌（refreshToken）。
     * <p>
     * 该方法根据用户 ID 从缓存中获取刷新令牌。如果令牌不存在或已过期，则返回 null。
     * </p>
     *
     * @param uid 用户的唯一标识符（通常是用户 ID）
     * @return 用户对应的刷新令牌，若不存在则返回 null
     */
    @Override
    public String getRefreshToken(String uid) {
        String rtkCacheKey = String.format(RTK_TOKEN_CACHE_KEY, uid);
        return GlobalCache.get(rtkCacheKey);
    }

    /**
     * 使指定用户的令牌过期。
     * <p>
     * 该方法根据用户 ID 删除该用户的访问令牌和刷新令牌，使其立即过期。
     * </p>
     *
     * @param uid 用户的唯一标识符（通常是用户 ID）
     */
    @Override
    public void expire(String uid) {
        String utkCacheKey = String.format(ATK_TOKEN_CACHE_KEY, uid);
        String rtkCacheKey = String.format(RTK_TOKEN_CACHE_KEY, uid);
        GlobalCache.remove(utkCacheKey, rtkCacheKey);
    }
}
