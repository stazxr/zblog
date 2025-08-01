package com.github.stazxr.zblog.bas.security.jwt.config;

import com.github.stazxr.zblog.bas.security.jwt.JwtProperties;
import com.github.stazxr.zblog.bas.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.bas.security.jwt.decoder.JwtDecoder;
import com.github.stazxr.zblog.bas.security.jwt.decoder.NimbusJwsDecoder;
import com.github.stazxr.zblog.bas.security.jwt.encoder.JwtEncoder;
import com.github.stazxr.zblog.bas.security.jwt.encoder.NimbusJwsEncoder;
import com.github.stazxr.zblog.bas.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.bas.security.jwt.storage.impl.JwtTokenStorageImpl;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.security.KeyStore;
import java.security.KeyStoreException;

/**
 * JwtConfig 配置类
 * <p>
 * 该配置类负责设置与 JWT 相关的组件，包括 JWT 编码器、解码器、令牌存储和密钥源。
 * 它还处理从 JKS (Java KeyStore) 文件加载 RSA 密钥，用于签名和验证 JWT。
 * </p>
 *
 * <p>
 * 该配置启用了 Spring 的组件扫描，自动将 JwtEncoder、JwtDecoder 和 JwtTokenStorage 等 Bean 注册到应用程序上下文中，
 * 并加载所需的加密密钥用于签名和验证 JWT。
 * </p>
 *
 * @since 2024-05-16
 * @author SunTao
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {
    /**
     * 用于加载 RSA 密钥的 JKS (Java KeyStore) 实例。
     */
    private static final KeyStore JKS_STORE;

    static {
        try {
            // 初始化 JKS keystore 实例
            JKS_STORE = KeyStore.getInstance("jks");
        } catch (KeyStoreException e) {
            throw new RuntimeException("can not obtain jks keystore instance");
        }
    }

    /**
     * 创建并返回 JWKSource Bean，从类路径中的 JKS 文件加载 RSA 密钥，用于 JWT 的签名和验证。
     *
     * RSA 密钥通过 {@link JwtProperties} 配置中的别名和密码加载。
     *
     * @param jwtProperties JwtProperties 实例，包含证书的相关信息
     * @return 返回一个 JWKSource 实例，用于签名 JWT
     * @throws Exception 如果加载 JKS 文件或检索 RSA 密钥时发生错误
     */
    @Bean("jwkSource")
    public JWKSource<SecurityContext> jwkSource(JwtProperties jwtProperties) throws Exception {
        // 获取证书配置
        JwtProperties.CertInfo certInfo = jwtProperties.getCertInfo();

        // 从类路径资源加载 JKS keystore 文件
        ClassPathResource classPathResource = new ClassPathResource(certInfo.getCertLocation());
        char[] pin = certInfo.getKeyPassword().toCharArray();
        JKS_STORE.load(classPathResource.getInputStream(), pin);

        // 从 JKS store 中加载 RSA 密钥，使用别名和密码
        RSAKey rsaKey = RSAKey.load(JKS_STORE, certInfo.getAlias(), pin);

        // 创建包含该 RSA 密钥的 JWK 集
        JWKSet jwkSet = new JWKSet(rsaKey);

        // 返回一个不可变的 JWK 源，封装了 JWK 集
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 创建并返回一个 JwtEncoder Bean，使用提供的 JWKSource 对 JWT 进行编码生成 JWS。
     * <p>
     * 该方法使用 Nimbus JWS 编码器（{@link NimbusJwsEncoder}），
     * 通过 JWK（JSON Web Key）源对 JWT 进行签名，并生成 JWS（JSON Web Signature）。
     * </p>
     *
     * @param jwkSource 用于签名 JWT 的 JWK 源，提供密钥对
     * @return 返回一个 JwtEncoder 实例，该实例用于将 JWT 编码成 JWS
     * @see NimbusJwsEncoder
     */
    @Bean("jwtEncoder")
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwsEncoder(jwkSource);
    }

    /**
     * 创建并返回一个 JwtDecoder Bean，使用提供的 JWKSource 对 JWS 进行解码并验证 JWT。
     * <p>
     * 该方法使用 Nimbus JWS 解码器（{@link NimbusJwsDecoder}）解码 JWT，验证签名，并从中提取 JWT 的声明集（claims）。
     * 通过提供的 JWK（JSON Web Key）源，验证 JWT 是否被合法签名，并解码为 JWT 对象。
     * </p>
     *
     * @param jwkSource 用于解码 JWS 的 JWK 源，提供用于验证 JWT 签名的公钥
     * @return 返回一个 JwtDecoder 实例，用于解码和验证 JWT
     * @see NimbusJwsDecoder
     */
    @Bean("jwtDecoder")
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwsDecoder(jwkSource);
    }

    /**
     * 创建并返回一个 JwtTokenStorage Bean，用于存储和检索 JWT。
     * 该组件负责缓存 JWT，例如访问令牌和刷新令牌，以便在身份验证过程中快速检索。
     *
     * @return 返回一个 JwtTokenStorage 实例，用于存储 JWT 令牌
     */
    @Bean("jwtTokenStorage")
    public JwtTokenStorage jwtTokenStorage() {
        return new JwtTokenStorageImpl();
    }

    /**
     * 创建并配置一个 {@link JwtTokenGenerator} 实例，用于生成 JWT 令牌。
     * <p>
     * 该方法将 {@link JwtProperties}、{@link JwtEncoder} 和 {@link JwtTokenStorage} 作为依赖注入，
     * 并返回一个新的 {@link JwtTokenGenerator} 实例。该实例用于生成访问令牌（access token）和刷新令牌（refresh token），
     * 并将其存储在指定的存储（如缓存）中。
     * </p>
     *
     * @param jwtProperties 配置 JWT 的相关属性，包括访问令牌和刷新令牌的有效期等
     * @param jwtEncoder 用于编码 JWT 生成 JWS 的工具
     * @param jwtTokenStorage 用于存储生成的 JWT 令牌的存储接口
     * @return 返回一个 {@link JwtTokenGenerator} 实例，用于生成和存储 JWT 令牌
     */
    @Bean("jwtTokenGenerator")
    public JwtTokenGenerator jwtTokenGenerator(JwtProperties jwtProperties, JwtEncoder jwtEncoder, JwtTokenStorage jwtTokenStorage) {
        return new JwtTokenGenerator(jwtProperties, jwtEncoder, jwtTokenStorage);
    }
}
