package com.github.stazxr.zblog.base.component.security.config;

import com.github.stazxr.zblog.base.component.security.jwt.JwtProperties;
import com.github.stazxr.zblog.base.component.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.base.component.security.jwt.cache.JwtTokenStorage;
import com.github.stazxr.zblog.base.component.security.jwt.encoder.JwtEncoder;
import com.github.stazxr.zblog.base.component.security.jwt.encoder.NimbusJwsEncoder;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.Collection;

/**
 * JwtConfiguration
 *
 * @author SunTao
 * @since 2022-01-20
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {
    /**
     * 加载 JKS 证书库
     */
    private static final KeyStore JKS_STORE;

    private final JwtProperties jwtProperties;

    static {
        try {
            JKS_STORE = KeyStore.getInstance("jks");
        } catch (KeyStoreException e) {
            throw new RuntimeException("can not obtain jks keystore instance");
        }
    }

    /**
     * 获取JWK (JSON Web Key)  包含了JOSE(可以认为是JWT的超集) 加密解密 签名验签的Key
     *
     * @return the jwk set
     * @throws KeyStoreException        the key store exception
     * @throws IOException              the io exception
     * @throws JOSEException            the jose exception
     * @throws CertificateException     the certificate exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    @Bean
    @ConditionalOnMissingBean
    public JWKSource<SecurityContext> jwkSource() throws IOException, CertificateException,
            NoSuchAlgorithmException, KeyStoreException, JOSEException {
        // Load key store.
        JwtProperties.CertInfo certInfo = jwtProperties.getCertInfo();
        ClassPathResource classPathResource = new ClassPathResource(certInfo.getCertLocation());
        char[] pin = certInfo.getKeyPassword().toCharArray();
        JKS_STORE.load(classPathResource.getInputStream(), pin);

        // Loads a public / private RSA JWK from the specified JCA key store.
        RSAKey rsaKey = RSAKey.load(JKS_STORE, certInfo.getAlias(), pin);

        // Creates a new JSON Web Key (JWK) set with a single key.
        JWKSet jwkSet = new JWKSet(rsaKey);

        // Creates a new JWK source backed by an immutable JWK set.
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 用 JWK 来生成 JWT 的工具，底层使用了 Nimbus 库，这个库是 Spring Security OAuth2 Client 默认引用的库
     *
     * @param jwkSource the jwk source
     * @return the jwt encoder
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwsEncoder(jwkSource);
    }

    /**
     * generate JWT.
     *
     * @param jwtEncoder the jwt encoder
     * @param jwtTokenStorage the jwt cache
     * @return the jwt token generator
     */
    @Bean
    public JwtTokenGenerator jwtTokenGenerator(JwtEncoder jwtEncoder, JwtTokenStorage jwtTokenStorage) {
        return new JwtTokenGenerator(jwtProperties, jwtEncoder, jwtTokenStorage);
    }

    /**
     * 校验 JWT issuer
     *
     * @return the jwt issuer validator
     * @see DelegatingOAuth2TokenValidator
     */
    @Bean
    JwtIssuerValidator jwtIssuerValidator() {
        return new JwtIssuerValidator(jwtProperties.getClaims().getIssuer());
    }

    /**
     * 校验 JWT 是否过期
     *
     * @return the jwt timestamp validator
     * @see DelegatingOAuth2TokenValidator
     */
    @Bean
    JwtTimestampValidator jwtTimestampValidator() {
        return new JwtTimestampValidator(Duration.ofSeconds(jwtProperties.getClaims().getDuration()));
    }

    /**
     * JWT 委托校验器，用来执行多个JWT校验策略，如果有其它校验需要可自行实现{@link OAuth2TokenValidator}
     *
     * @param tokenValidators the token validators
     * @return the delegating o auth 2 token validator
     */
    @Primary
    @Bean("delegatingTokenValidator")
    public DelegatingOAuth2TokenValidator<Jwt> delegatingTokenValidator(Collection<OAuth2TokenValidator<Jwt>> tokenValidators) {
        return new DelegatingOAuth2TokenValidator<>(tokenValidators);
    }

    /**
     * Jwt decoder.
     *
     * @param jwkSource the jwk source
     * @return the jwt decoder
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource, @Qualifier("delegatingTokenValidator") DelegatingOAuth2TokenValidator<Jwt> validator) {
        // Creates a new JWS verification key selector.
        JWSVerificationKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);

        // Default processor of JSON Web Tokens (JWTs).
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(keySelector);

        // generate nimbusJwtDecoder
        NimbusJwtDecoder nimbusJwtDecoder = new NimbusJwtDecoder(jwtProcessor);
        nimbusJwtDecoder.setJwtValidator(validator);

        return nimbusJwtDecoder;
    }
}
