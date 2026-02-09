package com.github.stazxr.zblog.bas.ratelimit.core;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.UUID;

/**
 * 限流服务
 *
 * @author SunTao
 * @since 2025-02-09
 */
public class RateLimitService {
    private final StringRedisTemplate redisTemplate;

    private final DefaultRedisScript<Long> script;

    public RateLimitService(StringRedisTemplate redisTemplate, DefaultRedisScript<Long> script) {
        this.redisTemplate = redisTemplate;
        this.script = script;
    }

    /**
     * 尝试获取限流许可
     */
    public boolean tryAcquire(String key, int time, int count) {
        long now = System.currentTimeMillis();

        // 执行 Lua
        Long result = redisTemplate.execute(
                script,
                Collections.singletonList(key),
                String.valueOf(now),
                String.valueOf(time * 1000),
                String.valueOf(count),
                UUID.randomUUID().toString()
        );

        return result != null && result == 1;
    }
}
