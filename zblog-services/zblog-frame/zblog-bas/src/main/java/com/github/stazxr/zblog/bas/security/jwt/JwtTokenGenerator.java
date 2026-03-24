package com.github.stazxr.zblog.bas.security.jwt;

import com.github.stazxr.zblog.bas.security.jwt.autoconfigure.properties.JwtProperties;
import com.github.stazxr.zblog.bas.security.jwt.encoder.JwtEncoder;
import com.github.stazxr.zblog.bas.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.bas.security.jwt.storage.TokenPayload;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;

import java.util.Date;

/**
 * JwtTokenGenerator 负责生成 JWT（Json Web Token）并将其存储在缓存中。
 * <p>
 * 该类提供生成访问令牌（access token）和刷新令牌（refresh token）的方法，
 * 并将生成的 token 缓存到指定的存储中。JWT 是通过指定的 JWK（Json Web Key）进行编码的，
 * 并且可选地包括有关用户的附加信息（如登录 IP 地址和令牌版本等）。
 * </p>
 *
 * @author SunTao
 * @since 2022-01-19
 */
public class JwtTokenGenerator {
    /**
     * JWT 配置属性。
     */
    private final JwtProperties jwtProperties;

    /**
     * JWT 编码器，用于将 JWT 生成 JWS。
     */
    private final JwtEncoder jwtEncoder;

    /**
     * Token 存储接口，用于将生成的 token 存储在缓存中。
     */
    private final JwtTokenStorage jwtTokenStorage;

    /**
     * 创建 JwtTokenGenerator 实例
     *
     * @param jwtProperties   JWT 配置属性对象
     * @param jwtEncoder      JWT 编码器对象，用于编码 JWT
     * @param jwtTokenStorage JWT Token 存储接口，用于存储生成的 Token
     */
    public JwtTokenGenerator(JwtProperties jwtProperties, JwtEncoder jwtEncoder, JwtTokenStorage jwtTokenStorage) {
        this.jwtProperties = jwtProperties;
        this.jwtEncoder = jwtEncoder;
        this.jwtTokenStorage = jwtTokenStorage;
    }

    /**
     * 令牌生成（登录调用）。
     *
     * @param jwtContext Jwt 参数上下文
     * @return 返回生成的访问令牌（accessToken）
     */
    public String generateToken(JwtContext jwtContext) {
        // 获取当前时间，作为 JWT 的发放时间
        Date issueTime = new Date();

        // 获取签名算法
        JWSAlgorithm algorithm = new JWSAlgorithm(jwtProperties.getAlgorithm());

        // 获取参数信息
        String userId = jwtContext.getUserId();
        String loginIp = jwtContext.getLoginIp();

        // 生成访问令牌的声明集，并通过编码器编码为 access token
        String accessJwtId = "ATK_" + SequenceUtils.getId();
        JWTClaimsSet accessClaims = buildAccessJwtChaimSet(issueTime, accessJwtId, userId, loginIp);
        String accessToken = jwtEncoder.encode(algorithm, accessClaims);

        String refreshToken = null;
        int ttl = jwtProperties.getAccessTokenDuration();
        if (jwtProperties.isAllowedRenewToken()) {
            // 生成刷新令牌的声明集，并通过编码器编码为 refresh token
            String refreshJwtId = "RTK_" + SequenceUtils.getId();
            JWTClaimsSet refreshClaims = buildRefreshJwtChaimSet(issueTime, refreshJwtId, userId);
            refreshToken = jwtEncoder.encode(algorithm, refreshClaims);
            ttl = jwtProperties.getRefreshTokenDuration();
        }

        // 缓存令牌信息
        TokenPayload tokenPayload = new TokenPayload(userId, refreshToken, issueTime, accessJwtId);
        jwtTokenStorage.put(userId, tokenPayload, ttl);

        // 返回访问令牌
        return accessToken;
    }

    /**
     * 令牌刷新（续签调用）。
     *
     * @param jwtContext Jwt 参数上下文
     * @return 返回新生成的访问令牌（accessToken）
     */
    public String refreshToken(JwtContext jwtContext) {
        // 获取当前时间，作为新 JWT 的发放时间
        Date issueTime = new Date();

        // 获取参数信息
        String userId = jwtContext.getUserId();
        String loginIp = jwtContext.getLoginIp();

        // 生成访问令牌的声明集，并通过编码器编码为 access token
        String newAccessJwtId = String.valueOf(SequenceUtils.getId());
        JWTClaimsSet accessClaims = buildAccessJwtChaimSet(issueTime, newAccessJwtId, userId, loginIp);
        String accessToken = jwtEncoder.encode(new JWSAlgorithm(jwtProperties.getAlgorithm()), accessClaims);

        // 缓存新令牌信息
        TokenPayload tokenPayload = jwtTokenStorage.get(userId);
        tokenPayload.setAccessTokenId(newAccessJwtId);
        tokenPayload.setRenewTime(issueTime);
        jwtTokenStorage.put(userId, tokenPayload, jwtProperties.getRefreshTokenDuration());
        return accessToken;
    }

    /**
     * 构建访问令牌的 JWT 声明集（JWT Claims Set）。
     *
     * @param issueTime JWT 的发放时间
     * @param jwtId JWT 唯一标识 ID
     * @param uid 用户唯一标识符
     * @param loginIp 用户的登录 IP 地址
     * @return 返回包含访问令牌声明集的 {@link JWTClaimsSet}
     */
    private JWTClaimsSet buildAccessJwtChaimSet(Date issueTime, String jwtId, String uid, String loginIp) {
        // 获取 JWT 配置中的声明信息
        JwtProperties.Claims claims = jwtProperties.getClaims();
        return new JWTClaimsSet.Builder()
            // JWT ID 用于唯一标识一个 JWT
            .jwtID(jwtId)
            // JWT 的发布者
            .issuer(claims.getIssuer())
            // JWT 的受众
            .audience(claims.getAudience())
            // JWT 的主体
            .subject(uid)
            // JWT 的发放时间
            .issueTime(issueTime)
            // JWT 的生效时间
            .notBeforeTime(issueTime)
            // JWT 的过期时间
            .expirationTime(new Date(issueTime.getTime() + (jwtProperties.getAccessTokenDuration() * 1000L)))
            // 用户登录 IP
            .claim(JwtConstants.LOGIN_IP_KEY, loginIp)
            .build();
    }

    /**
     * 构建刷新令牌的 JWT 声明集（JWT Claims Set）。
     *
     * @param issueTime JWT 的发放时间
     * @param jwtId JWT 唯一标识 ID
     * @param uid 用户唯一标识符
     * @return 返回包含刷新令牌声明集的 {@link JWTClaimsSet}
     */
    private JWTClaimsSet buildRefreshJwtChaimSet(Date issueTime, String jwtId, String uid) {
        // 获取 JWT 配置中的声明信息
        JwtProperties.Claims claims = jwtProperties.getClaims();
        return new JWTClaimsSet.Builder()
            // JWT ID 用于唯一标识一个 JWT
            .jwtID(jwtId)
            // JWT 的发布者
            .issuer(claims.getIssuer())
            // JWT 的受众
            .audience(claims.getAudience())
            // JWT 的主体
            .subject(uid)
            // JWT 的发放时间
            .issueTime(issueTime)
            // JWT 的生效时间
            .notBeforeTime(issueTime)
            // JWT 的过期时间
            .expirationTime(new Date(issueTime.getTime() + (jwtProperties.getRefreshTokenDuration() * 1000L)))
            .build();
    }
}
