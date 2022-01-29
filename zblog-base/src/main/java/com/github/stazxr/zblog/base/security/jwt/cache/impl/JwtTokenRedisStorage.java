package com.github.stazxr.zblog.base.security.jwt.cache.impl;

import com.github.stazxr.zblog.base.security.jwt.JwtTokenPair;
import com.github.stazxr.zblog.base.security.jwt.cache.JwtTokenStorage;

/**
 * JwtTokenCacheStorage
 *
 * @author SunTao
 * @since 2022-01-29
 */
public class JwtTokenRedisStorage  implements JwtTokenStorage {
    /**
     * Put JwtTokenPair
     *
     * @param jwtTokenPair jwtTokenPair
     * @param account      username
     * @return JwtTokenPair
     */
    @Override
    public JwtTokenPair put(JwtTokenPair jwtTokenPair, String account) {
        return null;
    }

    /**
     * Expire.
     *
     * @param account username
     */
    @Override
    public void expire(String account) {

    }

    /**
     * Get JwtTokenPair
     *
     * @param account username
     * @return JwtTokenPair
     */
    @Override
    public JwtTokenPair get(String account) {
        return null;
    }
}
