package com.github.stazxr.zblog.base.component.security.jwt;

import com.github.stazxr.zblog.base.component.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.base.component.security.jwt.encoder.JwtEncoder;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.UuidUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * JwtTokenGenerator
 *
 * @author SunTao
 * @since 2022-01-19
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenGenerator {
    private final JwtProperties jwtProperties;

    private final JwtEncoder jwtEncoder;

    private final JwtTokenStorage jwtTokenStorage;

    public String generateToken(HttpServletRequest request, User user, int version, String loginIp) {
        // jwt properties claims set
        Date issueTime = new Date();
        JWSAlgorithm algorithm = new JWSAlgorithm(jwtProperties.getAlgorithm());

        // access_token
        JWTClaimsSet accessTokenClaims = buildAccessJwtChaimSet(issueTime, request, user, version, loginIp);
        String accessToken = jwtEncoder.encode(algorithm, accessTokenClaims);

        // refresh_token
        JWTClaimsSet refreshTokenClaims = buildRefreshJwtChaimSet(issueTime, user);
        String refreshToken = jwtEncoder.encode(algorithm, refreshTokenClaims);
        refreshToken = jwtTokenStorage.putRefreshToken(refreshToken, user.getId(), jwtProperties.getRefreshTokenDuration());
        Assert.notNull(refreshToken, "'refreshToken' 缓存失败");

        // cache token
        accessToken = jwtTokenStorage.putAccessToken(accessToken, user.getId(), jwtProperties.getAccessTokenDuration());
        Assert.notNull(refreshToken, "'accessToken' 缓存失败");
        return accessToken;
    }

    private JWTClaimsSet buildAccessJwtChaimSet(Date issueTime, HttpServletRequest request, User user, int version, String loginIp) {
        JwtProperties.Claims claims = jwtProperties.getClaims();
        return new JWTClaimsSet.Builder()
                .jwtID(UuidUtils.uuid())
                .issuer(claims.getIssuer())
                .issueTime(issueTime)
                .audience(String.valueOf(user.getId()))
                .subject(claims.getSubject())
                .notBeforeTime(issueTime)
                .expirationTime(new Date(issueTime.getTime() + (jwtProperties.getAccessTokenDuration() * 1000L)))
                .claim("loginIp", StringUtils.isBlank(loginIp) ? IpUtils.getIp(request) : loginIp)
                .claim("renewToken", jwtProperties.isAllowedRenewToken())
                .claim("version", version)
                .build();
    }

    private JWTClaimsSet buildRefreshJwtChaimSet(Date issueTime, User user) {
        JwtProperties.Claims claims = jwtProperties.getClaims();
        return new JWTClaimsSet.Builder()
                .jwtID(UuidUtils.uuid())
                .issuer(claims.getIssuer())
                .issueTime(issueTime)
                .audience(String.valueOf(user.getId()))
                .subject(claims.getSubject())
                .notBeforeTime(issueTime)
                .expirationTime(new Date(issueTime.getTime() + (jwtProperties.getRefreshTokenDuration() * 1000L)))
                .build();
    }
}