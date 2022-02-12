package com.github.stazxr.zblog.base.security.jwt.cache.impl;

import com.github.stazxr.zblog.base.security.jwt.cache.JwtTokenStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

/**
 * JwtTokenCacheStorage
 *
 * @author SunTao
 * @since 2022-01-19
 */
@Component(value = "jwtTokenStorage")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "jwt.cache-type", havingValue = "ehcache")
public class JwtTokenCacheStorage implements JwtTokenStorage {
    /**
     * TIPS: 查看缓存配置文件 ehcache.xml 定义 过期时间与 token 过期一致.
     */
    private static final String TOKEN_CACHE = "usrTkn";

    /**
     * Put tokenResponse
     *
     * @param tokenResponse OAuth2AccessTokenResponse
     * @param username      username
     * @return JwtTokenPair
     */
    @CachePut(value = TOKEN_CACHE, key = "#username")
    @Override
    public OAuth2AccessTokenResponse put(OAuth2AccessTokenResponse tokenResponse, String username) {
        return tokenResponse;
    }

    /**
     * Expire.
     *
     * @param username username
     */
    @CacheEvict(value = TOKEN_CACHE, key = "#username")
    @Override
    public void expire(String username) {

    }

    /**
     * Get tokenResponse
     *
     * @param username username
     * @return OAuth2AccessTokenResponse
     */
    @Cacheable(value = TOKEN_CACHE, key = "#username")
    @Override
    public OAuth2AccessTokenResponse get(String username) {
        return null;
    }
}
