package com.github.stazxr.zblog.bas.security.jwt.decoder;

import com.github.stazxr.zblog.bas.security.jwt.BaseJwkSourceHandler;
import com.github.stazxr.zblog.util.Assert;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * 基于 Nimbus 库实现的 JWS 解码器，用于将 JWS（JSON Web Signature）解码为 JWT（JSON Web Token）。
 * <p>
 * 该类负责解析 JWS 字符串、验证签名，并提取 JWT 声明集。它通过提供的 JWK 来源选择合适的 JWK 密钥进行签名验证。
 * </p>
 *
 * @author SunTao
 * @since 2022-08-19
 */
public class NimbusJwsDecoder extends BaseJwkSourceHandler implements JwtDecoder {
    /**
     * 解码错误消息的模板
     */
    private static final String DECODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to decode the Jwt: %s";

    /**
     * JWK 密钥源，用于选择验证 JWS 签名的 JWK
     */
    private final JWKSource<SecurityContext> jwkSource;

    /**
     * 构造方法，初始化 NimbusJwsDecoder 实例。
     *
     * @param jwkSource 提供 JWK 的源，通常是 JWK 集合（不可为 null），see {@link com.nimbusds.jose.jwk.source.JWKSource}
     * @throws IllegalArgumentException 如果 jwkSource 为 null
     */
    public NimbusJwsDecoder(JWKSource<SecurityContext> jwkSource) {
        Assert.notNull(jwkSource, "jwkSource cannot be null");
        this.jwkSource = jwkSource;
    }

    /**
     * 解码 JWS 为 JWT。
     * <p>
     * 该方法将解析传入的 JWS 字符串，提取其中的 JWT 声明集，并验证 JWS 的签名。如果解码失败或签名无效，将抛出 {@link JwtDecodingException}。
     * </p>
     *
     * @param jws 待解码的 JWS 字符串
     * @return 解码后的 JWT 声明集（JWTClaimsSet）
     * @throws JwtDecodingException 如果解码 JWS 过程中发生错误或签名验证失败
     */
    @Override
    public JWTClaimsSet decode(String jws) throws JwtDecodingException {
        if (jws == null) {
            throw new JwtDecodingException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, "jws cannot be null"));
        }

        // 解析 JWS 字符串
        SignedJWT signedJwt;
        JWTClaimsSet jwtClaimsSet;
        try {
            signedJwt = SignedJWT.parse(jws);
            jwtClaimsSet = signedJwt.getJWTClaimsSet();
        } catch (Exception ex) {
            throw new JwtDecodingException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, "Failed to parse the JWS: " + jws), ex);
        }

        // 校验 JWT 声明集是否为空
        Assert.notNull(jwtClaimsSet, String.format(DECODING_ERROR_MESSAGE_TEMPLATE, "jwt claims set is null"));

        // 获取 JWS 头部并从中选择 JWK
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