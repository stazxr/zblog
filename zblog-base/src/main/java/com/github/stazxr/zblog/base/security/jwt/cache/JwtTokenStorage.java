package com.github.stazxr.zblog.base.security.jwt.cache;

import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

/**
 * JwtTokenStorage
 *
 * @author SunTao
 * @since 2022-01-19
 */
public interface JwtTokenStorage {
    /**
     * Put tokenResponse
     *
     * @param tokenResponse OAuth2AccessTokenResponse
     * @param username      username
     * @return JwtTokenPair
     */
    OAuth2AccessTokenResponse put(OAuth2AccessTokenResponse tokenResponse, String username);

    /**
     * Expire.
     *
     * @param username username
     */
    void expire(String username);

    /**
     * Get tokenResponse
     *
     * @param username username
     * @return OAuth2AccessTokenResponse
     */
    OAuth2AccessTokenResponse get(String username);
}
