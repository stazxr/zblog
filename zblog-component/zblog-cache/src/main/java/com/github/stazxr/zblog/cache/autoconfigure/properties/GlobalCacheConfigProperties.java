package com.github.stazxr.zblog.cache.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Arrays;

/**
 * 全局缓存配置属性类。
 * <p>
 * 通过 {@code @ConfigurationProperties} 注解从配置文件中读取全局缓存的配置信息。
 * 支持的缓存类型包括内存缓存和 Redis 缓存。
 * </p>
 *
 * @author SunTao
 * @since 2024-08-22
 */
@ConfigurationProperties(prefix = GlobalCacheConfigProperties.HEADER_PREFIX)
public class GlobalCacheConfigProperties {
    static final String HEADER_PREFIX = "zblog.global.cache";

    /**
     * 全局缓存类型，默认为内存缓存。<br>
     * 支持的值有 "memory" 和 "redis"。
     */
    private String type = "memory";

    /**
     * 设置缓存类型。
     *
     * @param type 缓存类型，支持 "memory" 和 "redis"
     * @throws IllegalArgumentException 如果类型不在支持的值中
     */
    public void setType(String type) {
        String[] validTypes = {"memory", "redis"};
        if (!Arrays.asList(validTypes).contains(type)) {
            throw new IllegalArgumentException("Properties zblog.global.cache.type value must be ['memory', 'redis']");
        }
        this.type = type;
    }

    /**
     * 获取缓存类型。
     *
     * @return 当前配置的缓存类型
     */
    public String getType() {
        return type;
    }
}

