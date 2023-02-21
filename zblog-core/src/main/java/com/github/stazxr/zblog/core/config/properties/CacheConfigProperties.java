package com.github.stazxr.zblog.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * CacheProperties
 *
 * @author SunTao
 * @since 2022-03-03
 */
@Data
@Component
@ConfigurationProperties(prefix= "cache")
public class CacheConfigProperties {
    /**
     * 永久缓存
     */
    public static final int FOREVER_CACHE = 0;

    /**
     * 系统缓存类型
     */
    private String type = "memory";

    /**
     * 默认有效时间, 单位秒, 0 代表永久有效.
     */
    private int defaultDuration = 300;

    public void setDefaultDuration(int defaultDuration) {
        if (defaultDuration < 0) {
            throw new IllegalArgumentException("sys cache duration must not be negative.");
        }
        this.defaultDuration = defaultDuration;
    }

    public void setType(String type) {
        String[] validType = new String[]{"memory", "redis"};
        if (!Arrays.asList(validType).contains(type)) {
            throw new IllegalArgumentException("sys cache type must be ['memory', 'redis'].");
        }
        this.type = type;
    }
}
