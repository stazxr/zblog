package com.github.stazxr.zblog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Jwt配置信息
 *
 * @author SunTao
 * @since 2022-01-01
 */
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * 密钥
     */
    private String secret;

    /**
     * 过期时间，单位毫秒
     */
    private Long expiration;

    /**
     * Token Header
     */
    private String tokenHeader;
}
