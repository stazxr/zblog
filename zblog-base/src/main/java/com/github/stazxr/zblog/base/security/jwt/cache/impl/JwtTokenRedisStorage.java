package com.github.stazxr.zblog.base.security.jwt.cache.impl;

import com.github.stazxr.zblog.base.security.jwt.JwtTokenPair;
import com.github.stazxr.zblog.base.security.jwt.cache.JwtTokenStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

/**
 * JwtTokenCacheStorage
 *
 * @author SunTao
 * @since 2022-01-29
 */
@Component(value = "jwtTokenStorage")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "jwt.cache-type", havingValue = "redis")
public class JwtTokenRedisStorage implements JwtTokenStorage {
    /**
     * Put tokenResponse
     *
     * @param tokenResponse OAuth2AccessTokenResponse
     * @param username      username
     * @return JwtTokenPair
     */
    @Override
    public OAuth2AccessTokenResponse put(OAuth2AccessTokenResponse tokenResponse, String username) {
        return null;
    }

    /**
     * Expire.
     *
     * @param username username
     */
    @Override
    public void expire(String username) {

    }

    /**
     * Get tokenResponse
     *
     * @param username username
     * @return OAuth2AccessTokenResponse
     */
    @Override
    public OAuth2AccessTokenResponse get(String username) {
        return null;
    }
}
