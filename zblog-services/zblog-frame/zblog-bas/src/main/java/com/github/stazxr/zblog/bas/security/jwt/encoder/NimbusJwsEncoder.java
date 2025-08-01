package com.github.stazxr.zblog.bas.security.jwt.encoder;

import com.github.stazxr.zblog.bas.security.jwt.BaseJwkSourceHandler;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.factories.DefaultJWSSignerFactory;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.produce.JWSSignerFactory;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于 Nimbus 库实现的 JWS 编码器，用于将 JSON Web Token (JWT) 编码为 JSON Web Signature (JWS)。
 * <p>
 * 该类使用 Nimbus JOSE + JWT 库来创建签名的 JWT（JWS），它从提供的 JWK 源选择合适的 JWK，
 * 并使用该 JWK 对 JWT 进行签名。它支持使用不同的签名算法对 JWT 进行编码。
 * </p>
 *
 * @since 2022-08-17
 * @author SunTao
 */
public class NimbusJwsEncoder extends BaseJwkSourceHandler implements JwtEncoder {
    /**
     * 编码错误消息模板
     */
    private static final String ENCODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to encode the Jwt: %s";

    /**
     * JWS 签名器工厂，用于创建 JWS 签名器
     */
    private static final JWSSignerFactory JWS_SIGNER_FACTORY = new DefaultJWSSignerFactory();

    /**
     * 存储每个 JWK 对应的 JWS 签名器
     */
    private final Map<JWK, JWSSigner> jwsSigners = new ConcurrentHashMap<>();

    /**
     * JWK 源，用于选择合适的 JWK 进行签名
     */
    private final JWKSource<SecurityContext> jwkSource;

    /**
     * 初始化 NimbusJwsEncoder 实例。
     *
     * @param jwkSource 提供 JWK 的源，通常是 JWK 集合（不可为 null）
     * @throws IllegalArgumentException 如果 jwkSource 为 null
     */
    public NimbusJwsEncoder(JWKSource<SecurityContext> jwkSource) {
        Assert.notNull(jwkSource, "jwkSource cannot be null");
        this.jwkSource = jwkSource;
    }

    /**
     * 将 JWT 编码为 JWS（签名的 JWT）。
     * <p>
     * 该方法通过指定的算法对 JWT 进行编码，并返回一个经过签名的 JWS 字符串。生成的 JWS 字符串可以用于安全地传输信息。
     * </p>
     *
     * @param algorithm 用于签名 JWT 的算法（例如：RS256、HS256 等）
     * @param claims JWT 声明集，包含有关用户或客户端的有意义的信息（如身份、权限等）
     * @return 生成的 JWS 字符串，代表经过签名的 JWT
     * @throws JwtEncodingException 如果编码 JWT 时发生错误（例如签名失败或 JWK 不可用等）
     */
    @Override
    public String encode(JWSAlgorithm algorithm, JWTClaimsSet claims) throws JwtEncodingException {
        // 校验算法和声明集参数不能为空
        if (algorithm == null) {
            throw new JwtEncodingException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Algorithm cannot be null"));
        }
        if (claims == null) {
            throw new JwtEncodingException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Claims cannot be null"));
        }

        // 选择合适的 JWK
        JWK jwk = selectJwk(algorithm, jwkSource);

        // 检查 JWK 是否包含有效的 'kid' (key ID)
        if (!StringUtils.hasText(jwk.getKeyID())) {
            throw new JwtEncodingException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "The 'kid' (key ID) from the selected JWK cannot be empty"));
        }

        // 获取或创建 JWS 签名器
        JWSSigner jwsSigner = jwsSigners.computeIfAbsent(jwk, (key) -> {
            try {
                return JWS_SIGNER_FACTORY.createJWSSigner(key);
            } catch (Exception e) {
                throw new JwtEncodingException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Failed to create a JWS Signer"), e);
            }
        });

        try {
            // 创建 JWS 头部
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).keyID(jwk.getKeyID()).build();

            // 创建一个签名的 JWT
            SignedJWT signedJwt = new SignedJWT(jwsHeader, claims);

            // 使用签名器对 JWT 进行签名
            signedJwt.sign(jwsSigner);

            // 返回序列化后的 JWS 字符串
            return signedJwt.serialize();
        } catch (Exception e) {
            throw new JwtEncodingException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, "Failed to sign the JWT"), e);
        }
    }
}