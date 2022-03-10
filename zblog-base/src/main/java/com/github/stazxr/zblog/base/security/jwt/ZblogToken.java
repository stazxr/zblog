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

    private AccessToken accessToken;

    private RefreshToken refreshToken;

    private Map<String, Object> additional;

    public ZblogToken() {
        accessToken = new AccessToken();
        refreshToken = new RefreshToken();
    }

    /**
     * 构建 ZblogToken
     *
     * @param tokenResponse OAuth2AccessTokenResponse
     */
    public ZblogToken build(OAuth2AccessTokenResponse tokenResponse) {
        // accessToken
        OAuth2AccessToken oAuth2AccessToken = tokenResponse.getAccessToken();
        accessToken.issuedAt = oAuth2AccessToken.getIssuedAt();
        accessToken.expiresAt = oAuth2AccessToken.getExpiresAt();
        accessToken.tokenType = oAuth2AccessToken.getTokenType().getValue();
        accessToken.tokenValue = oAuth2AccessToken.getTokenValue();
        accessToken.scopes = oAuth2AccessToken.getScopes();

        // refreshToken
        OAuth2RefreshToken oAuth2RefreshToken = tokenResponse.getRefreshToken();
        if (oAuth2RefreshToken == null) {
            refreshToken = null;
        } else {
            refreshToken.issuedAt = oAuth2RefreshToken.getIssuedAt();
            refreshToken.tokenValue = oAuth2RefreshToken.getTokenValue();
        }

        // additional
        additional = tokenResponse.getAdditionalParameters();

        return this;
    }

    @Data
    public static class AccessToken {
        private Instant issuedAt;

        private Instant expiresAt;

        private String tokenType;

        private String tokenValue;

        Set<String> scopes;
    }

    @Data
    public static class RefreshToken {
        private Instant issuedAt;

        private String tokenValue;
    }
}
