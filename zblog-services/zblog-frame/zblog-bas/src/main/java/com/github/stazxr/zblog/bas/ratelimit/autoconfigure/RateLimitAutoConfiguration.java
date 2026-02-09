package com.github.stazxr.zblog.bas.ratelimit.autoconfigure;

import com.github.stazxr.zblog.bas.ratelimit.core.RateLimitService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * 限流自动配置
 *
 * @author SunTao
 * @since 2025-02-08
 */
@Configuration
public class RateLimitAutoConfiguration {
    @Bean
    public DefaultRedisScript<Long> rateLimitScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/rate_limit.lua"));
        script.setResultType(Long.class);
        return script;
    }

    @Bean
    public RateLimitService rateLimitService(StringRedisTemplate redisTemplate, DefaultRedisScript<Long> script) {
        return new RateLimitService(redisTemplate, script);
    }
}
