package com.github.stazxr.zblog.base.security.jwt;

import lombok.Data;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

/**
 * 系统自定义令牌
 *
 * @author SunTao
 * @since 2022-03-09
 */
@Data
public class ZblogToken implements Serializable {
    /**
     * serialId
     */
    private static final long serialVersionUID = 8442431512167536320L;

    /**
     * ATK
     */
    private AccessToken accessToken;

    /**
     * RTK
     */
    private RefreshToken refreshToken;

    /**
     * Additional
     */
    private Map<String, Object> additional;

    public ZblogToken() {
        accessToken = new AccessToken();
        refreshToken = new RefreshToken();
    }

    public ZblogToken withToken(String tokenValue) {
        this.accessToken.tokenValue = tokenValue;
        return this;
    }

    public ZblogToken refreshToken(String tokenValue) {
        this.refreshToken.tokenValue = tokenValue;
        return this;
    }

    public ZblogToken tokenType(OAuth2AccessToken.TokenType tokenType) {
        this.accessToken.tokenType = tokenType.getValue();
        return this;
    }

    public ZblogToken scopes(Set<String> scopes) {
        this.accessToken.scopes = scopes;
        return this;
    }

    public ZblogToken issuedAt(Instant issuedAt) {
        this.accessToken.issuedAt = issuedAt;
        this.refreshToken.issuedAt = issuedAt;
        return this;
    }

    public ZblogToken expiresAt(Instant expiresAt) {
        this.accessToken.expiresAt = expiresAt;
        return this;
    }

    public ZblogToken additionalParameters(Map<String, Object> additionalParameters) {
        this.additional = additionalParameters;
        return this;
    }

    @Data
    public static class AccessToken {
        private String tokenType;

        private String tokenValue;

        private Instant issuedAt;

        private Instant expiresAt;

        private Set<String> scopes;
    }

    @Data
    public static class RefreshToken {
        private String tokenValue;

        private Instant issuedAt;
    }
}
