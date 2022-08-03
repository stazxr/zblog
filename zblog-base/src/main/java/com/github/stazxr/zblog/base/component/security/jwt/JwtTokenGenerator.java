package com.github.stazxr.zblog.base.component.security.jwt;

import com.github.stazxr.zblog.base.component.security.jwt.cache.JwtTokenStorage;
import com.github.stazxr.zblog.base.component.security.jwt.encoder.JoseHeader;
import com.github.stazxr.zblog.base.component.security.jwt.encoder.JwtClaimsSet;
import com.github.stazxr.zblog.base.component.security.jwt.encoder.JwtEncoder;
import com.github.stazxr.zblog.base.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Collections;
import java.util.stream.Collectors;

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

    public ZblogToken getTokenResponse(User user) {
        // jwt claims set
        JwtProperties.Claims claims = jwtProperties.getClaims();
        // plusSeconds(TimeUnit.HOURS.toSeconds(8))
        Instant issuedAt = Instant.now();
        Instant expireAt = issuedAt.plusSeconds(claims.getDuration());
        JwtClaimsSet sharedClaims = JwtClaimsSet.builder()
                .issuer(claims.getIssuer())
                .subject(claims.getSubject())
                .audience(Collections.singletonList(user.getUsername()))
                .issuedAt(issuedAt)
                .build();

        // jose header
        JoseHeader joseHeader = JoseHeader.withAlgorithm(SignatureAlgorithm.RS256).type("JWT").build();

        // atk, rtk
        Jwt accessToken = jwtEncoder.encode(joseHeader, JwtClaimsSet.from(sharedClaims).expiresAt(expireAt).build());
        Jwt refreshToken = jwtEncoder.encode(joseHeader, sharedClaims);

        // build ZblogToken
        ZblogToken zblogToken = new ZblogToken()
                .withToken(accessToken.getTokenValue())
                .refreshToken(refreshToken.getTokenValue())
                .additionalParameters(Collections.emptyMap())
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .scopes(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .issuedAt(issuedAt)
                .expiresAt(expireAt);

        // token在缓存中的有效期为半天
        return jwtTokenStorage.put(zblogToken, user.getUsername(), 12 * 3600);
    }
}