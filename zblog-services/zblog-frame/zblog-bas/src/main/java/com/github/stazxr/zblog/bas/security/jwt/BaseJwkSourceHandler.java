package com.github.stazxr.zblog.bas.security.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import java.util.List;

/**
 * 基础 JWK（JSON Web Key）源处理器，负责从 JWK 来源中选择合适的 JWK。
 * <p>
 * 该类提供了一个方法 `selectJwk` 来从 JWK 来源中选择一个符合给定算法要求的 JWK 密钥。
 * </p>
 * <p>
 * 实现此类的具体子类可以根据不同的需求进一步扩展处理逻辑。
 * </p>
 *
 * @author SunTao
 * @since 2022-08-18
 */
public abstract class BaseJwkSourceHandler {
    /**
     * JWK 源处理器错误消息模板
     */
    private static final String JWK_SOURCE_HANDLER_EOR = "Handler jwk source catch error, %s";

    /**
     * 从给定的 JWK 来源中选择一个符合指定签名算法的 JWK 密钥。
     * <p>
     * 该方法会根据提供的签名算法创建一个 JWS 头，并通过 JWK 来源选择符合该头部的 JWK。
     * 如果选择的 JWK 密钥列表为空或有多个匹配项，将抛出 {@link JwtException} 异常。
     * </p>
     *
     * @param algorithm 用于签名的 JWS 算法（如 {@link JWSAlgorithm#RS256}）
     * @param jwkSource 提供 JWK 密钥的源（如 JWK 集合）
     * @return 选择的 JWK 密钥，符合指定的签名算法
     * @throws JwtException 如果在选择 JWK 时发生错误，或无法找到符合要求的 JWK
     */
    protected JWK selectJwk(JWSAlgorithm algorithm, JWKSource<SecurityContext> jwkSource) {
        try {
            // 构建 JWS 头部
            JWSHeader jwsHeader = new JWSHeader.Builder(algorithm).build();

            // 使用 JWS 头部创建 JWK 选择器
            JWKSelector jwkSelector = new JWKSelector(JWKMatcher.forJWSHeader(jwsHeader));

            // 从 JWK 源中获取匹配的 JWK 列表
            List<JWK> jwkList = jwkSource.get(jwkSelector, null);
            if (jwkList.isEmpty()) {
                // 如果没有找到匹配的 JWK 密钥，抛出异常
                throw new JwtException(String.format(JWK_SOURCE_HANDLER_EOR, "Failed to select a JWK signing key"));
            } else if (jwkList.size() > 1) {
                // 如果找到多个匹配的 JWK 密钥，抛出异常
                throw new JwtException(String.format(JWK_SOURCE_HANDLER_EOR, "Found multiple JWK signing keys for algorithm '" + algorithm.getName() + "'"));
            } else {
                // 返回匹配的 JWK 密钥
                return jwkList.get(0);
            }
        } catch (JwtException e) {
            throw e;
        } catch (Exception ex) {
            throw new JwtException(String.format(JWK_SOURCE_HANDLER_EOR, "select a JWK signing key error"), ex);
        }
    }
}
