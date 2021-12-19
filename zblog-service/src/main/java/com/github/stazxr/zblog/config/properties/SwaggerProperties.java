package com.github.stazxr.zblog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger配置信息
 *
 * @author SunTao
 * @since 2021-12-11
 */
@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    /**
     * Whether to enable swagger
     */
    private String enable;
}
