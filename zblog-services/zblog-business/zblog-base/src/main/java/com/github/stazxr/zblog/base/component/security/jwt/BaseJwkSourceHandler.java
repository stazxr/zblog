package com.github.stazxr.zblog.base.component.security.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import java.util.List;

/**
 * JWK Handler
 *
 * @author SunTao
 * @since 2022-08-18
 */
public abstract class BaseJwkSourceHandler {
    private static final String JWK_SOURCE_HANDLER_EOR = "handler jwk source catch an error, %s";

    protected JWK selectJwk(JWSAlgorithm algorithm, JWKSource<SecurityContext> jwkSource) {
        try {
            JWSHeader jwsHeader = new JWSHeader.Builder(algorithm).build();
            JWKSelector jwkSelector = new JWKSelector(JWKMatcher.forJWSHeader(jwsHeader));
            List<JWK> jwkList = jwkSource.get(jwkSelector, null);
            if (jwkList.isEmpty()) {
                throw new JwtException(String.format(JWK_SOURCE_HANDLER_EOR, "Failed to select a JWK signing key"));
            } else if (jwkList.size() > 1) {
                throw new JwtException(String.format(JWK_SOURCE_HANDLER_EOR, "Found multiple JWK signing keys for algorithm '" + algorithm.getName() + "'"));
            } else {
                return jwkList.get(0);
            }
        } catch (JwtException e) {
            throw e;
        } catch (Exception ex) {
            throw new JwtException(String.format(JWK_SOURCE_HANDLER_EOR, "select a JWK signing key error"), ex);
        }
    }
}
