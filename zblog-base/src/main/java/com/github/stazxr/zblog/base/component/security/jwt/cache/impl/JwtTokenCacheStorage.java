package com.github.stazxr.zblog.base.component.security.jwt.cache.impl;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.base.component.security.jwt.ZblogToken;
import com.github.stazxr.zblog.base.component.security.jwt.cache.JwtTokenStorage;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.util.CacheUtils;
import com.github.stazxr.zblog.util.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * JwtTokenCacheStorage
 *
 * @author SunTao
 * @since 2022-01-19
 */
@Component(value = "jwtTokenStorage")
@RequiredArgsConstructor
public class JwtTokenCacheStorage implements JwtTokenStorage {
    /**
     * Cache Label
     */
    public static final String TOKEN_CACHE = Constants.CacheKey.usrTkn.cacheKey().concat(":");

    /**
     * Put tokenResponse
     *
     * @param token    ZblogToken
     * @param username username
     * @param duration validTime
     * @return JwtTokenPair
     */
    @Override
    public ZblogToken put(ZblogToken token, String username, int duration) {
        Assert.notNull(username, "JwtTokenStorage cache username must not br null");
        Assert.notNull(token, "JwtTokenStorage cache token must not br null");
        String key = TOKEN_CACHE.concat(username);
        String value = JSON.toJSONString(token);
        CacheUtils.put(key, value, duration);
        return get(username);
    }

    /**
     * Expire.
     *
     * @param username username
     */
    @Override
    public void expire(String username) {
        String key = TOKEN_CACHE.concat(username);
        CacheUtils.remove(key);
    }

    /**
     * Get tokenResponse
     *
     * @param username username
     * @return OAuth2AccessTokenResponse
     */
    @Override
    public ZblogToken get(String username) {
        String key = TOKEN_CACHE.concat(username);
        String cacheValue = CacheUtils.get(key);
        if (cacheValue != null) {
            return JSON.parseObject(cacheValue, ZblogToken.class);
        }
        return null;
    }
}
