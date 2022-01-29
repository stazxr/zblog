package com.github.stazxr.zblog.base.security.jwt.cache.impl;

import com.github.stazxr.zblog.base.security.jwt.JwtTokenPair;
import com.github.stazxr.zblog.base.security.jwt.cache.JwtTokenStorage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * JwtTokenCacheStorage
 *
 * @author SunTao
 * @since 2022-01-19
 */
public class JwtTokenCacheStorage implements JwtTokenStorage {
    /**
     * 查看缓存配置文件 ehcache.xml 定义 过期时间与 refresh token 过期一致.
     */
    private static final String TOKEN_CACHE = "usrTkn";

    @CachePut(value = TOKEN_CACHE, key = "#userId")
    @Override
    public JwtTokenPair put(JwtTokenPair jwtTokenPair, String userId) {
        return jwtTokenPair;
    }

    @CacheEvict(value = TOKEN_CACHE, key = "#userId")
    @Override
    public void expire(String userId) {
    }

    @Cacheable(value = TOKEN_CACHE, key = "#userId")
    @Override
    public JwtTokenPair get(String userId) {
        return null;
    }
}
