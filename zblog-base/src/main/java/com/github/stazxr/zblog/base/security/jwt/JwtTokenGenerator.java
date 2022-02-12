package com.github.stazxr.zblog.base.security.jwt;

import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.security.jwt.cache.JwtTokenStorage;
import com.github.stazxr.zblog.base.security.jwt.encoder.JoseHeader;
import com.github.stazxr.zblog.base.security.jwt.encoder.JwtClaimsSet;
import com.github.stazxr.zblog.base.security.jwt.encoder.JwtEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;
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

    public OAuth2AccessTokenResponse getTokenResponse(User user) {
        JoseHeader joseHeader = JoseHeader.withAlgorithm(SignatureAlgorithm.RS256).type("JWT").build();
        Instant issuedAt = Instant.now();
        Set<String> scopes = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        JwtProperties.Claims claims = jwtProperties.getClaims();
        Integer duration = claims.getDuration();
        String username = user.getUsername();

        JwtClaimsSet sharedClaims = JwtClaimsSet.builder()
                .issuer(claims.getIssuer())
                .subject(claims.getSubject())
                .audience(Collections.singletonList(username))
                .claim("scopes", scopes)
                .expiresAt(issuedAt.plusSeconds(duration))
                .issuedAt(issuedAt)
                .build();

        Jwt accessToken = jwtEncoder.encode(joseHeader, JwtClaimsSet.from(sharedClaims)
                .expiresAt(issuedAt.plusSeconds(duration))
                .build());
        Jwt refreshToken = jwtEncoder.encode(joseHeader, sharedClaims);

        OAuth2AccessTokenResponse tokenResponse = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(duration)
                .scopes(scopes)
                .refreshToken(refreshToken.getTokenValue()).build();
        return jwtTokenStorage.put(tokenResponse, username);
    }
}