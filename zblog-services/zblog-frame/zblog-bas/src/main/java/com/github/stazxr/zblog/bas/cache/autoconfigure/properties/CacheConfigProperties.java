package com.github.stazxr.zblog.bas.cache.autoconfigure.properties;

import com.github.stazxr.zblog.bas.cache.CacheType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;

/**
 * 缓存配置类。
 *
 * @author SunTao
 * @since 2024-08-22
 */
@ConfigurationProperties(prefix = CacheConfigProperties.CONFIG_PREFIX)
public class CacheConfigProperties {
    static final String CONFIG_PREFIX = "zblog.base.cache";

    /**
     * 全局缓存类型
     */
    private CacheType type = CacheType.memory;

    public CacheType getType() {
        return type;
    }

    public void setType(CacheType type) {
        this.type = type;
    }
}
