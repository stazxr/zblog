package com.github.stazxr.zblog.base.component.security.jwt.encoder;

import com.github.stazxr.zblog.base.component.security.jwt.BaseJwkSourceHandler;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.factories.DefaultJWSSignerFactory;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.produce.JWSSignerFactory;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of encoding a JSON Web Token (JWT) TO JWS
 *
 * @author SunTao
 * @since 2022-08-17
 */
public class NimbusJwsEncoder extends BaseJwkSourceHandler implements JwtEncoder {
    private static final String ENCODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to encode the Jwt: %s";

    private static final JWSSignerFactory JWS_SIGNER_FACTORY = new DefaultJWSSignerFactory();

    private final Map<JWK, JWSSigner> jwsSigners = new ConcurrentHashMap<>();

    private final JWKSource<SecurityContext> jwkSource;

    /**
     * NimbusJwsEncoder
     *
     * @param jwkSource the {@code com.nimbusds.jose.jwk.source.JWKSource}
     */
    public NimbusJwsEncoder(JWKSource<SecurityContext> jwkSource) {
        Assert.notNull(jwkSource, "jwkSource cannot be null");
        this.jwkSource = jwkSource;
    }

    /**
     * Encode the JWT to JWS.
     *
     * @param algorithm the algorithm
     * @param claims the JWT Claims Set
     * @return jws
     * @throws JwtEncodingException if an error occurs while attempting to encode the JWT
     */
    @Override
    public String encode(JWSAlgorithm algorithm, JWTClaimsSet claims) throws JwtEncodingException {
        Assert.notNull(algorithm, "algorithm cannot be null");
        Assert.notNull(claims, "claims cannot be null");

        JWK jwk = selectJwk(algorithm, jwkSource);
        if (!StringUtils.hasText(jwk.getKeyID())) {
            throw new JwtEncodingException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "The 'kid' (key ID) from the selected JWK cannot be empty"));
        }

        // get jws signer
        JWSSigner jwsSigner = jwsSigners.computeIfAbsent(jwk, (key) -> {
            try {
                return JWS_SIGNER_FACTORY.createJWSSigner(key);
            } catch (Exception ex) {
                throw new JwtEncodingException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Failed to create a JWS Signer"), ex);
            }
        });

        try {
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).keyID(jwk.getKeyID()).build();
            SignedJWT signedJwt = new SignedJWT(jwsHeader, claims);
            signedJwt.sign(jwsSigner);
            return signedJwt.serialize();
        } catch (Exception ex) {
            throw new JwtEncodingException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Failed to sign the JWT"), ex);
        }
    }
}