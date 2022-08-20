package com.github.stazxr.zblog.base.component.security.jwt.storage.impl;

import com.github.stazxr.zblog.base.component.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.util.CacheUtils;
import com.github.stazxr.zblog.util.Assert;

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
    public static final String TOKEN_CACHE_1 = Constants.CacheKey.usrTkn.cacheKey().concat("-atk:");

    /**
     * Cache Label Of refreshToken
     */
    public static final String TOKEN_CACHE_2 = Constants.CacheKey.usrTkn.cacheKey().concat("-rtk:");

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
        CacheUtils.put(TOKEN_CACHE_1.concat(String.valueOf(uid)), accessToken, duration);
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
        CacheUtils.put(TOKEN_CACHE_2.concat(String.valueOf(uid)), refreshToken, duration);
        return getRefreshToken(uid);
    }

    /**
     * Expire.
     *
     * @param uid userId
     */
    @Override
    public void expire(Long uid) {
        CacheUtils.remove(TOKEN_CACHE_1.concat(String.valueOf(uid)));
        CacheUtils.remove(TOKEN_CACHE_2.concat(String.valueOf(uid)));
    }

    /**
     * Get accessToken
     *
     * @param uid userId
     * @return accessToken
     */
    @Override
    public String getAccessToken(Long uid) {
        return CacheUtils.get(TOKEN_CACHE_1.concat(String.valueOf(uid)));
    }

    /**
     * Get refreshToken
     *
     * @param uid userId
     * @return refreshToken
     */
    @Override
    public String getRefreshToken(Long uid) {
        return CacheUtils.get(TOKEN_CACHE_2.concat(String.valueOf(uid)));
    }
}
