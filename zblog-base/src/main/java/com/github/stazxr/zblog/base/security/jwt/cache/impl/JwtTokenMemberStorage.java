package com.github.stazxr.zblog.base.security.jwt.cache.impl;


import com.github.stazxr.zblog.base.security.jwt.JwtProperties;
import com.github.stazxr.zblog.base.security.jwt.JwtTokenPair;
import com.github.stazxr.zblog.base.security.jwt.cache.JwtTokenStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JwtTokenCacheStorage
 *
 * @author SunTao
 * @since 2022-01-29
 */
@Component(value = "jwtTokenStorage")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "jwt.cache-type", havingValue = "memory")
public class JwtTokenMemberStorage implements JwtTokenStorage {
    private final Map<String, JwtTokenPair> tokenMap = new ConcurrentHashMap<>();

    private final Map<String, LocalDateTime> expTimeMap1 = new ConcurrentHashMap<>();

    // private final Map<String, LocalDateTime> expTimeMap2 = new ConcurrentHashMap<>();

    private final JwtProperties jwtProperties;

    /**
     * Put JwtTokenPair
     *
     * @param jwtTokenPair jwtTokenPair
     * @param account      username
     * @return JwtTokenPair
     */
    @Override
    public JwtTokenPair put(JwtTokenPair jwtTokenPair, String account) {
        tokenMap.put(account, jwtTokenPair);
        expTimeMap1.put(account, LocalDateTime.now().plusSeconds(jwtProperties.getClaims().getDuration()));
        // expTimeMap2.put(account, LocalDateTime.now().plusDays(jwtProperties.getRefreshExpDays()));
        return jwtTokenPair;
    }

    /**
     * Expire.
     *
     * @param account username
     */
    @Override
    public void expire(String account) {
        tokenMap.remove(account);
        expTimeMap1.remove(account);
        // expTimeMap2.remove(account);
    }

    /**
     * Get JwtTokenPair
     *
     * @param account username
     * @return JwtTokenPair
     */
    @Override
    public JwtTokenPair get(String account) {
        if (expTimeMap1.containsKey(account)) {
            LocalDateTime accessExpTime = expTimeMap1.get(account);
            if (LocalDateTime.now().isAfter(accessExpTime)) {
                expire(account);
            }
            return tokenMap.get(account);
        }

        return null;
    }
}
