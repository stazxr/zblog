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
    /**
     * ACCESS_TOKEN 有效时间，单位秒数
     */
    private int accessTokenDuration = 7200;

    /**
     * REFRESH_TOKEN 有效时间，单位秒数，要求大于 accessTokenDuration，一般配置为 accessTokenDuration 的两倍
     */
    private int refreshTokenDuration = accessTokenDuration * 2;

    /**
     * 是否允许续签，默认关闭
     */
    private boolean allowedRenewToken = false;

    /**
     * token 续期检查时间范围（默认30分钟，单位秒），要求小于 accessTokenDuration
     */
    private int refreshMinDuration = accessTokenDuration / 4;

    /**
     * ACCESS_TOKEN 最大的版本（最多允许续签几次，默认五次）
     */
    private int maxVersion = 5;

    /**
     * 签名类型，com.nimbusds.jose.JWSAlgorithm，默认 RS256
     */
    private String algorithm = "RS256";

    /**
     * 证书信息
     */
    private CertInfo certInfo;

    /**
     * 共有属性
     */
    private Claims claims;

    @Data
    public static class CertInfo {
        /**
         * 证书名称
         */
        private String alias = "zblog";

        /**
         * 证书密钥
         */
        private String keyPassword = "123456";

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
         * 发布者的url地址
         */
        private String issuer;

        /**
         * jwt所面向的用户
         */
        private String subject;
    }

    public void setMaxVersion(int maxVersion) {
        if (maxVersion < 0) {
            throw new IllegalArgumentException("jwt.max-version must not be negative.");
        }
        this.maxVersion = maxVersion;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setAccessTokenDuration(int accessTokenDuration) {
        if (accessTokenDuration < 0) {
            throw new IllegalArgumentException("jwt.access-token-duration must not be negative.");
        }
        this.accessTokenDuration = accessTokenDuration;
    }

    public void setRefreshTokenDuration(int refreshTokenDuration) {
        if (refreshTokenDuration < 0) {
            throw new IllegalArgumentException("jwt.refresh-token-duration must not be negative.");
        }
        this.refreshTokenDuration = refreshTokenDuration;
    }

    public void setRefreshMinDuration(int refreshMinDuration) {
        if (refreshMinDuration < 0) {
            throw new IllegalArgumentException("jwt.refresh-token-duration must not be negative.");
        }

        if (refreshMinDuration >= accessTokenDuration) {
            throw new IllegalArgumentException("jwt.refresh-token-duration must be less then jwt.refresh-token-duration.");
        }
        this.refreshMinDuration = refreshMinDuration;
    }
}
