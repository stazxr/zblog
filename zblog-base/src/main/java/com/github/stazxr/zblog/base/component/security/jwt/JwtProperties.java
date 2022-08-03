package com.github.stazxr.zblog.base.component.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JwtProperties
 *
 * @author SunTao
 * @since 2022-01-19
 */
@Data
@Component
@ConfigurationProperties(prefix= "jwt")
public class JwtProperties {
    private CertInfo certInfo;

    private Claims claims;

    /**
     * The cert info.
     */
    @Data
    public static class CertInfo {
        /**
         * key alias
         */
        private String alias = "zblog";

        /**
         * key store pass
         */
        private String keyPassword;

        /**
         * 证书路径
         */
        private String certLocation = "zblog.jks";
    }

    /**
     * The jwt claims.
     */
    @Data
    public static class Claims {
        /**
         * jwt签发者
         */
        private String issuer = "https://www.suntaoblog.com";

        /**
         * jwt所面向的用户
         */
        private String subject = "all";

        /**
         * jwt有效时间，单位秒数
         */
        private int duration;
    }
}
