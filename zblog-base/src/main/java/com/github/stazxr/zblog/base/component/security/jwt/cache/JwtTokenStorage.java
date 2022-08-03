package com.github.stazxr.zblog.base.component.security.jwt.cache;

import com.github.stazxr.zblog.base.component.security.jwt.ZblogToken;

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
     * @param token     ZblogToken
     * @param username  username
     * @param duration  valid time
     * @return JwtTokenPair
     */
    ZblogToken put(ZblogToken token, String username, int duration);

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
    ZblogToken get(String username);
}
