package com.github.stazxr.zblog.base.security.jwt.cache;

import com.github.stazxr.zblog.base.security.jwt.JwtTokenPair;

/**
 * JwtTokenStorage
 *
 * @author SunTao
 * @since 2022-01-19
 */
public interface JwtTokenStorage {
    /**
     * Put JwtTokenPair
     *
     * @param jwtTokenPair jwtTokenPair
     * @param account      username
     * @return JwtTokenPair
     */
    JwtTokenPair put(JwtTokenPair jwtTokenPair, String account);

    /**
     * Expire.
     *
     * @param account username
     */
    void expire(String account);

    /**
     * Get JwtTokenPair
     *
     * @param account username
     * @return JwtTokenPair
     */
    JwtTokenPair get(String account);
}
