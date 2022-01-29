package com.github.stazxr.zblog.base.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JwtProperties
 *
 * @author SunTao
 * @since 2022-01-19
 */
@Data
@ConfigurationProperties(prefix= JwtProperties.JWT_PREFIX)
public class JwtProperties {
    static final String JWT_PREFIX= "jwt.config";

    /**
     * jks 路径
     */
    private String keyLocation = "zblog.jks";

    /**
     * key alias
     */
    private String keyAlias = "zblog";

    /**
     * key store pass
     */
    private String keyPass;

    /**
     * jwt签发者
     */
    private String iss = "zblog.space";

    /**
     * jwt所面向的用户
     */
    private String sub = "all";

    /**
     * 缓存类型
     */
    private String cacheType = "memory";

    /**
     * access token 有效天数
     */
    private int accessExpDays = 3;

    /**
     * refresh token 有效天数
     */
    private int refreshExpDays = 7;
}
