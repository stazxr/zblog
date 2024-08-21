package com.github.stazxr.zblog.cache.autoconfigure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
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

        // 设置 key 和 value 的序列化规则，使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = initJacksonSerializer();
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 初始化 Jackson2JsonRedisSerializer 用于 JSON 序列化。
     *
     * @return 配置好的 Jackson2JsonRedisSerializer 实例
     */
    private Jackson2JsonRedisSerializer<Object> initJacksonSerializer() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();

        // 设置对象映射的可见性和默认类型处理
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );

        // 禁用将日期键序列化为时间戳的功能，以支持 Java 8 时间类型
        objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());

        serializer.setObjectMapper(objectMapper);
        return serializer;
    }
}
