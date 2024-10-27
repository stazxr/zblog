package com.github.stazxr.zblog.base.component.security.jwt.storage.impl;

import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.base.component.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.util.Assert;

import java.util.Locale;

/**
 * JwtTokenCacheStorage
 *
 * @author SunTao
 * @since 2022-01-19
 */
public class JwtTokenStorageImpl implements JwtTokenStorage {
    /**
     * Cache Label Of accessToken
     */
    public static final String TOKEN_CACHE_1 = Constants.SysCacheKey.usrTkn.cacheKey().concat("-atk:%s");

    /**
     * Cache Label Of refreshToken
     */
    public static final String TOKEN_CACHE_2 = Constants.SysCacheKey.usrTkn.cacheKey().concat("-rtk:%s");

    /**
     * Put accessToken
     *
     * @param accessToken Token
     * @param uid  userId
     * @param duration 有效时间，单位秒
     * @return accessToken
     */
    @Override
    public String putAccessToken(String accessToken, Long uid, int duration) {
        Assert.notNull(uid, "JwtTokenStorage put 'uid' is null");
        Assert.notNull(accessToken, "JwtTokenStorage put 'accessToken' is null");
        String utkCacheKey = String.format(TOKEN_CACHE_1, uid, Locale.ROOT);
        GlobalCache.put(utkCacheKey, accessToken, duration);
        return getAccessToken(uid);
    }

    /**
     * Put refreshToken
     *
     * @param refreshToken Token
     * @param uid  userId
     * @param duration 有效时间，单位秒
     * @return refreshToken
     */
    @Override
    public String putRefreshToken(String refreshToken, Long uid, int duration) {
        Assert.notNull(uid, "JwtTokenStorage put 'uid' is null");
        Assert.notNull(refreshToken, "JwtTokenStorage put 'refreshToken' is null");
        String rtkCacheKey = String.format(TOKEN_CACHE_2, uid, Locale.ROOT);
        GlobalCache.put(rtkCacheKey, refreshToken, duration);
        return getRefreshToken(uid);
    }

    /**
     * Get accessToken
     *
     * @param uid userId
     * @return accessToken
     */
    @Override
    public String getAccessToken(Long uid) {
        String utkCacheKey = String.format(TOKEN_CACHE_1, uid, Locale.ROOT);
        return GlobalCache.get(utkCacheKey);
    }

    /**
     * Get refreshToken
     *
     * @param uid userId
     * @return refreshToken
     */
    @Override
    public String getRefreshToken(Long uid) {
        String rtkCacheKey = String.format(TOKEN_CACHE_2, uid, Locale.ROOT);
        return GlobalCache.get(rtkCacheKey);
    }

    /**
     * Expire.
     *
     * @param uid userId
     */
    @Override
    public void expire(Long uid) {
        String utkCacheKey = String.format(TOKEN_CACHE_1, uid, Locale.ROOT);
        String rtkCacheKey = String.format(TOKEN_CACHE_2, uid, Locale.ROOT);
        GlobalCache.remove(utkCacheKey, rtkCacheKey);
    }
}
