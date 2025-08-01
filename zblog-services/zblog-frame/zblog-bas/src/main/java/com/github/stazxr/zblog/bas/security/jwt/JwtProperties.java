package com.github.stazxr.zblog.bas.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Jwt 配置信息
 * <p>
 * 配置 JWT 相关的属性，主要包括访问令牌（accessToken）和刷新令牌（refreshToken）的有效时间、签名算法、证书信息等。
 * </p>
 *
 * @author SunTao
 * @since 2022-01-19
 */
@ConfigurationProperties(prefix= JwtProperties.JWT_PREFIX)
public class JwtProperties {
    static final String JWT_PREFIX = "zblog.base.security.jwt";

    /**
     * 访问令牌有效时间，单位为秒，默认值 30 分钟
     */
    private int accessTokenDuration = 30 * 60;

    /**
     * 刷新令牌有效时间，单位为秒，通常为访问令牌有效时间的两倍
     */
    private int refreshTokenDuration = accessTokenDuration * 2;

    /**
     * 是否允许续签，默认不允许
     */
    private boolean allowedRenewToken = false;

    /**
     * 续签判定阙值，单位为秒，当访问令牌剩余时间小于该值时，可进行续签操作
     */
    private int refreshMinDuration = accessTokenDuration / 6;

    /**
     * 最大续签次数，默认 5 次
     */
    private int maxVersion = 5;

    /**
     * JWT 签名算法 {@link com.nimbusds.jose.JWSAlgorithm}，默认为 RS256
     */
    private String algorithm = "RS256";

    /**
     * 证书信息
     */
    private CertInfo certInfo;

    /**
     * 声明信息
     */
    private Claims claims;

    public static class CertInfo {
        /**
         * 证书名称
         */
        private String alias;

        /**
         * 证书密钥
         */
        private String keyPassword;

        /**
         * 证书路径
         */
        private String certLocation;

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getKeyPassword() {
            return keyPassword;
        }

        public void setKeyPassword(String keyPassword) {
            this.keyPassword = keyPassword;
        }

        public String getCertLocation() {
            return certLocation;
        }

        public void setCertLocation(String certLocation) {
            this.certLocation = certLocation;
        }
    }

    public static class Claims {
        /**
         * JWT 发布者的 URL 地址
         */
        private String issuer;

        /**
         * JWT 面向的用户
         */
        private String subject;

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }

    /**
     * 设置访问令牌有效时间，单位为秒
     *
     * @param accessTokenDuration 访问令牌有效时间，不能为负数
     */
    public void setAccessTokenDuration(int accessTokenDuration) {
        if (accessTokenDuration < 0) {
            throw new IllegalArgumentException(JWT_PREFIX + ".access-token-duration must not be negative.");
        }
        this.accessTokenDuration = accessTokenDuration;
    }

    public int getAccessTokenDuration() {
        return accessTokenDuration;
    }

    /**
     * 设置刷新令牌有效时间，单位为秒，必须大于等于 accessTokenDuration
     *
     * @param refreshTokenDuration 刷新令牌有效时间，不能为负数且大于访问令牌有效时间
     */
    public void setRefreshTokenDuration(int refreshTokenDuration) {
        if (refreshTokenDuration < 0) {
            throw new IllegalArgumentException(JWT_PREFIX + ".refresh-token-duration must not be negative.");
        }
        this.refreshTokenDuration = refreshTokenDuration;
    }

    public int getRefreshTokenDuration() {
        return refreshTokenDuration;
    }

    /**
     * 设置是否允许续签令牌
     *
     * @param allowedRenewToken 是否允许续签
     */
    public void setAllowedRenewToken(boolean allowedRenewToken) {
        this.allowedRenewToken = allowedRenewToken;
    }

    public boolean isAllowedRenewToken() {
        return allowedRenewToken;
    }

    /**
     * 设置最小续签时间，单位为秒，必须小于 accessTokenDuration
     *
     * @param refreshMinDuration 最小续签时间，不能为负数且小于访问令牌有效时间
     */
    public void setRefreshMinDuration(int refreshMinDuration) {
        if (refreshMinDuration < 0) {
            throw new IllegalArgumentException(JWT_PREFIX + ".refresh-token-duration must not be negative.");
        }

        if (refreshMinDuration >= accessTokenDuration) {
            throw new IllegalArgumentException(JWT_PREFIX + ".refresh-token-duration must be less then jwt.refresh-token-duration.");
        }
        this.refreshMinDuration = refreshMinDuration;
    }

    public int getRefreshMinDuration() {
        return refreshMinDuration;
    }

    /**
     * 设置最大版本数
     *
     * @param maxVersion 最大版本数，不能为负数
     */
    public void setMaxVersion(int maxVersion) {
        if (maxVersion < 0) {
            throw new IllegalArgumentException(JWT_PREFIX + ".max-version must not be negative.");
        }
        this.maxVersion = maxVersion;
    }

    public int getMaxVersion() {
        return maxVersion;
    }

    /**
     * 设置 JWT 签名算法
     *
     * @param algorithm 签名算法，如 RS256、HS256 等
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * 设置证书信息
     *
     * @param certInfo 证书信息对象
     */
    public void setCertInfo(CertInfo certInfo) {
        this.certInfo = certInfo;
    }

    public CertInfo getCertInfo() {
        return certInfo;
    }

    /**
     * 设置 JWT 声明信息
     *
     * @param claims JWT 声明信息对象
     */
    public void setClaims(Claims claims) {
        this.claims = claims;
    }

    public Claims getClaims() {
        return claims;
    }
}
