package com.github.stazxr.zblog.bas.cache.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类，配置 RedisTemplate 以及序列化器。
 *
 * @author SunTao
 * @since 2024-08-22
 */
@Configuration
public class RedisConfig {
    /**
     * 配置 RedisTemplate 实例，并设置序列化器。
     *
     * @param redisConnectionFactory LettuceConnectionFactory 实例
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @return 配置好的 RedisTemplate 实例
     */
    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<K, V> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 设置 key 和 value 的序列化规则
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
