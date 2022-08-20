package com.github.stazxr.zblog.base.component.security.config;

import com.github.stazxr.zblog.base.component.security.jwt.JwtProperties;
import com.github.stazxr.zblog.base.component.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.base.component.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.base.component.security.jwt.decoder.JwtDecoder;
import com.github.stazxr.zblog.base.component.security.jwt.decoder.NimbusJwsDecoder;
import com.github.stazxr.zblog.base.component.security.jwt.encoder.JwtEncoder;
import com.github.stazxr.zblog.base.component.security.jwt.encoder.NimbusJwsEncoder;
import com.github.stazxr.zblog.base.component.security.jwt.storage.impl.JwtTokenStorageImpl;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.security.KeyStore;
import java.security.KeyStoreException;

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
     * JKS 证书库
     */
    private static final KeyStore JKS_STORE;

    private final JwtProperties jwtProperties;

    static {
        try {
            // load jks store
            JKS_STORE = KeyStore.getInstance("jks");
        } catch (KeyStoreException e) {
            throw new RuntimeException("can not obtain jks keystore instance");
        }
    }

    /**
     * JWK
     *
     * @return the jwk set
     * @throws Exception build jwkSource failed
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception {
        // Load rsa key
        JwtProperties.CertInfo certInfo = jwtProperties.getCertInfo();
        ClassPathResource classPathResource = new ClassPathResource(certInfo.getCertLocation());
        char[] pin = certInfo.getKeyPassword().toCharArray();
        JKS_STORE.load(classPathResource.getInputStream(), pin);
        RSAKey rsaKey = RSAKey.load(JKS_STORE, certInfo.getAlias(), pin);

        // Creates a new JWK source backed by an immutable JWK set
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 用 JWK 来编码 JWT 生成 JWS 的工具
     *
     * @param jwkSource jwk
     * @return the jwt encoder
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwsEncoder(jwkSource);
    }

    /**
     * 用 JWK 来解码 JWS 获取 JWT 的工具
     *
     * @param jwkSource jwk
     * @return the jwt decode
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwsDecoder(jwkSource);
    }

    /**
     * Token 缓存器
     *
     * @return JwtTokenStorageImpl
     */
    @Bean
    public JwtTokenStorage jwtTokenStorage() {
        return new JwtTokenStorageImpl();
    }

    /**
     * Token 生成器
     *
     * @param jwtEncoder JWT 编码器
     * @param jwtTokenStorage Token 缓存器
     * @return JwtTokenGenerator
     */
    @Bean
    public JwtTokenGenerator jwtTokenGenerator(JwtEncoder jwtEncoder, JwtTokenStorage jwtTokenStorage) {
        return new JwtTokenGenerator(jwtProperties, jwtEncoder, jwtTokenStorage);
    }
}
