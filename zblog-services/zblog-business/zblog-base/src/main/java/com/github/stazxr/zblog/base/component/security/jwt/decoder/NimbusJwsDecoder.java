package com.github.stazxr.zblog.base.component.security.jwt.decoder;

import com.github.stazxr.zblog.base.component.security.jwt.BaseJwkSourceHandler;
import com.github.stazxr.zblog.base.component.security.jwt.JwtException;
import com.github.stazxr.zblog.util.Assert;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * An implementation of decoding a JWS TO JSON Web Token (JWT)
 *
 * @author SunTao
 * @since 2022-08-17
 */
public class NimbusJwsDecoder extends BaseJwkSourceHandler implements JwtDecoder {
    private static final String DECODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to decode the Jwt: %s";

    private final JWKSource<SecurityContext> jwkSource;

    /**
     * NimbusJwsDecoder
     *
     * @param jwkSource the {@code com.nimbusds.jose.jwk.source.JWKSource}
     */
    public NimbusJwsDecoder(JWKSource<SecurityContext> jwkSource) {
        Assert.notNull(jwkSource, "jwkSource cannot be null");
        this.jwkSource = jwkSource;
    }

    /**
     * Decode the JWS to JWT.
     *
     * @param jws JWS
     * @return JWT
     * @throws JwtException if an error occurs while attempting to decode the JWS
     */
    @Override
    public JWTClaimsSet decode(String jws) throws JwtDecodingException {
        if (jws == null) {
            throw new JwtDecodingException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, "param jws cannot be null"));
        }

        // parse Jwt
        SignedJWT signedJwt;
        JWTClaimsSet jwtClaimsSet;
        try {
            signedJwt = SignedJWT.parse(jws);
            jwtClaimsSet = signedJwt.getJWTClaimsSet();
        } catch (Exception ex) {
            throw new JwtDecodingException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, "Failed to parse a JWS: " + jws), ex);
        }

        // check chaim set and get jwk
        Assert.notNull(jwtClaimsSet, String.format(DECODING_ERROR_MESSAGE_TEMPLATE, "jwt claims set is null"));
        JWSHeader header = signedJwt.getHeader();
        JWK jwk = selectJwk(header.getAlgorithm(), jwkSource);

        // verifier jwt signature
        boolean verify;
        try {
            JWSVerifier verifier = new RSASSAVerifier(jwk.toRSAKey());
            verify = signedJwt.verify(verifier);
        } catch (Exception ex) {
            throw new JwtDecodingException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, "Failed to verify a JWS: " + jws), ex);
        }

        if (!verify) {
            throw new JwtDecodingException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, "The JWS signature is valid: " + jws));
        }

        return jwtClaimsSet;
    }
}