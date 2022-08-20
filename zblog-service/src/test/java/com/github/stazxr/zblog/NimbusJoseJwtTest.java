package com.github.stazxr.zblog;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.crypto.factories.DefaultJWSSignerFactory;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.produce.JWSSignerFactory;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.security.KeyStore;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 测试 nimbus-jose-jwt
 *
 * @author Thomas Sun
 * @since 2022-08-17
 */
public class NimbusJoseJwtTest {
    private static final String ENCODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to encode the Jwt: %s";

    private final Map<JWK, JWSSigner> jwsSigners = new ConcurrentHashMap<>();

    private static final JWSSignerFactory JWS_SIGNER_FACTORY = new DefaultJWSSignerFactory();

    @Test
    @Ignore
    public void testJwkSet() throws Exception {
        ImmutableJWKSet<SecurityContext> jwkSet = getJwkSet();
        System.out.println(jwkSet);
    }

    @Test
    @Ignore
    public void testJwkSetDouble() throws Exception {
        ImmutableJWKSet<SecurityContext> jwkSet = getJwkSetDouble();
        System.out.println(jwkSet);
    }

    @Test
    @Ignore
    public void testEncode() throws Exception {
        ImmutableJWKSet<SecurityContext> jwkSet = getJwkSet();
        JWK jwk = selectJwk(JWSAlgorithm.RS256, jwkSet);
        if (!StringUtils.hasText(jwk.getKeyID())) {
            throw new RuntimeException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "The 'kid' (key ID) from the selected JWK cannot be empty"));
        }

        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).keyID(jwk.getKeyID()).build();

        Date now = new Date();
        System.out.println(now);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("https://www.suntaoblog.com")
                .subject("all")
                .audience("https://www.suntaoblog.com")
                .expirationTime(new Date(now.getTime() + 2 * 60 * 1000))
                .notBeforeTime(now)
                .issueTime(now)
                .jwtID(UUID.randomUUID().toString())
                .build();

        JWSSigner jwsSigner = jwsSigners.computeIfAbsent(jwk, (key) -> {
            try {
                return JWS_SIGNER_FACTORY.createJWSSigner(key);
            } catch (Exception ex) {
                throw new RuntimeException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Failed to create a JWS Signer"), ex);
            }
        });

        SignedJWT signedJwt = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            signedJwt.sign(jwsSigner);
        } catch (Exception ex) {
            throw new RuntimeException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Failed to sign the JWT"), ex);
        }

        String jws = signedJwt.serialize();
        System.out.println("jws: " + jws);
    }

    @Test
    @Ignore
    public void testDecode() throws Exception {
        String token = "eyJraWQiOiJ6YmxvZyIsInR5cCI6IkpXVCIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJhbGwiLCJhdWQiOiJodHRwczpcL1wvd3d3LnN1bnRhb2Jsb2cuY29tIiwibmJmIjoxNjYwNzE3OTgyLCJpc3MiOiJodHRwczpcL1wvd3d3LnN1bnRhb2Jsb2cuY29tIiwiZXhwIjoxNjYwNzE4MTAyLCJpYXQiOjE2NjA3MTc5ODIsImp0aSI6ImNhNWNkZmJjLTVjOTUtNDYzNS05ODA0LTAyYTczMjg2MmQxYSJ9.Rln8noskDmc0MFCZLzXmpUE46A1TdGvyRJCl2IlBlx5ZhoLrYugqDJ-Xm3Jl6ngcquk-vcE6RSQla3xGF838Kp7dJpCdH1uBCqSJCPR71X8bqySVr9eCvcIdIGDfAsaEhCOwbthmeTxF7B8jGcAv8nVfB8_XQeC5Ksu1099S3u_W0nEQfoSsO0HZ7ovga7MtNz8y6IdecbB6xT2M_DeMK1DYuExb77LNW0HMF-jGCxlwWqkzkKG5pkXMdKna1amdtu5InTMCW3IgkXzwWz56DZXE49lqZYHGjpierbW5GNsPlNsjGyCoMmCCVxk17pZoLaTG-2u_yGB2aU-EBRtISw";
        SignedJWT signedJwt = SignedJWT.parse(token);
        JWTClaimsSet claims = signedJwt.getJWTClaimsSet();
        System.out.println(claims);

        // verify signature
        ImmutableJWKSet<SecurityContext> jwkSet = getJwkSet();
        JWK jwk = selectJwk(JWSAlgorithm.RS256, jwkSet);
        JWSVerifier verifier = new RSASSAVerifier(jwk.toRSAKey());
        boolean verify = signedJwt.verify(verifier);
        System.out.println(verify);

        // verify other

    }

    private ImmutableJWKSet<SecurityContext> getJwkSet() throws Exception {
        // load rsa key
        KeyStore keyStore = KeyStore.getInstance("jks");
        ClassPathResource classPathResource = new ClassPathResource("zblog.jks");
        char[] pin = "123456".toCharArray();
        keyStore.load(classPathResource.getInputStream(), pin);
        RSAKey rsaKey = RSAKey.load(keyStore, "zblog", pin);

        // generate jwk set
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private ImmutableJWKSet<SecurityContext> getJwkSetDouble() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("jks");
        ClassPathResource classPathResource = new ClassPathResource("zblog.jks");
        char[] pin = "123456".toCharArray();
        keyStore.load(classPathResource.getInputStream(), pin);
        RSAKey rsaKey = RSAKey.load(keyStore, "zblog", pin);
        System.out.println(rsaKey);

        KeyStore keyStore2 = KeyStore.getInstance("jks");
        ClassPathResource classPathResource2 = new ClassPathResource("zblog.jks");
        char[] pin2 = "123456".toCharArray();
        keyStore2.load(classPathResource2.getInputStream(), pin2);
        RSAKey rsaKey2 = RSAKey.load(keyStore2, "zblog", pin2);
        System.out.println(rsaKey2);

        List<JWK> jwkList = new ArrayList<>();
        jwkList.add(rsaKey);
        jwkList.add(rsaKey2);
        JWKSet jwkSet = new JWKSet(jwkList);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private JWK selectJwk(final JWSAlgorithm algorithm, ImmutableJWKSet<SecurityContext> jwkSet) {
        JWSHeader jwsHeader = new JWSHeader.Builder(algorithm).build();
        JWKSelector jwkSelector = new JWKSelector(JWKMatcher.forJWSHeader(jwsHeader));
        List<JWK> jwkList = jwkSet.get(jwkSelector, null);
        if (jwkList.isEmpty()) {
            throw new RuntimeException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Failed to select a JWK signing key"));
        }

        if (jwkList.size() > 1) {
            throw new RuntimeException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Found multiple JWK signing keys for algorithm '" + algorithm.getName() + "'"));
        }

        return jwkList.get(0);
    }
}
