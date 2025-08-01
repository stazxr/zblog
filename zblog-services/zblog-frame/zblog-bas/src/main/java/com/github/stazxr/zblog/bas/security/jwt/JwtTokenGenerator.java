package com.github.stazxr.zblog.bas.security.jwt;

import com.github.stazxr.zblog.bas.security.jwt.encoder.JwtEncoder;
import com.github.stazxr.zblog.bas.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.UuidUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
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
@Slf4j
public class JwtTokenGenerator {
    /**
     * JWT 配置属性，包括访问令牌和刷新令牌的有效时间等。
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
     * 生成访问令牌和刷新令牌，并将它们存储在缓存中。
     *
     * @param request 请求对象
     * @param uid 用户的唯一标识符
     * @param version JWT 版本
     * @param loginIp 用户登录的 IP 地址（可选，如果为空将从请求中获取）
     * @return 返回生成的访问令牌（accessToken）
     */
    public String generateToken(HttpServletRequest request, String uid, int version, String loginIp) {
        // 获取当前时间，作为 JWT 的发放时间
        Date issueTime = new Date();

        // 获取配置中的签名算法（如 RS256）
        JWSAlgorithm algorithm = new JWSAlgorithm(jwtProperties.getAlgorithm());

        // 生成访问令牌的声明集，并通过编码器编码为 access token
        JWTClaimsSet accessTokenClaims = buildAccessJwtChaimSet(issueTime, request, uid, version, loginIp);
        String accessToken = jwtEncoder.encode(algorithm, accessTokenClaims);

        // 生成刷新令牌的声明集，并通过编码器编码为 refresh token
        JWTClaimsSet refreshTokenClaims = buildRefreshJwtChaimSet(issueTime, uid);
        String refreshToken = jwtEncoder.encode(algorithm, refreshTokenClaims);

        // 将生成的 token 存储到缓存中
        accessToken = jwtTokenStorage.putAccessToken(accessToken, uid, jwtProperties.getAccessTokenDuration());
        Assert.notNull(refreshToken, "'accessToken' 缓存失败");
        refreshToken = jwtTokenStorage.putRefreshToken(refreshToken, uid, jwtProperties.getRefreshTokenDuration());
        Assert.notNull(refreshToken, "'refreshToken' 缓存失败");

        // 返回访问令牌
        return accessToken;
    }

    /**
     * 构建访问令牌的 JWT 声明集（JWT Claims Set）。
     *
     * @param issueTime JWT 的发放时间
     * @param request HTTP 请求对象，用于获取客户端的 IP 地址
     * @param uid 用户唯一标识符
     * @param version JWT 版本号
     * @param loginIp 用户的登录 IP 地址（如果为空将从请求中获取）
     * @return 返回包含访问令牌声明集的 {@link JWTClaimsSet}
     */
    private JWTClaimsSet buildAccessJwtChaimSet(Date issueTime, HttpServletRequest request, String uid, int version, String loginIp) {
        // 获取 JWT 配置中的声明信息
        JwtProperties.Claims claims = jwtProperties.getClaims();
        return new JWTClaimsSet.Builder()
                // JWT ID 用于唯一标识一个 JWT
                .jwtID(UuidUtils.genUuidStr())
                // JWT 的发布者
                .issuer(claims.getIssuer())
                // JWT 的发放时间
                .issueTime(issueTime)
                // JWT 的受众（此处为用户 ID）
                .audience(uid)
                // JWT 的主题
                .subject(claims.getSubject())
                // JWT 的生效时间
                .notBeforeTime(issueTime)
                // JWT 的过期时间
                .expirationTime(new Date(issueTime.getTime() + (jwtProperties.getAccessTokenDuration() * 1000L)))
                // 用户登录 IP
                .claim(JwtConstants.LOGIN_IP_KEY, StringUtils.isBlank(loginIp) ? IpUtils.getIp(request) : loginIp)
                // 是否允许续签
                .claim(JwtConstants.RENEW_TOKEN_KEY, jwtProperties.isAllowedRenewToken())
                // JWT 的版本号
                .claim(JwtConstants.JWT_VERSION_KEY, version)
                .build();
    }

    /**
     * 构建刷新令牌的 JWT 声明集（JWT Claims Set）。
     *
     * @param issueTime JWT 的发放时间
     * @param uid 用户唯一标识符
     * @return 返回包含刷新令牌声明集的 {@link JWTClaimsSet}
     */
    private JWTClaimsSet buildRefreshJwtChaimSet(Date issueTime, String uid) {
        // 获取 JWT 配置中的声明信息
        JwtProperties.Claims claims = jwtProperties.getClaims();
        return new JWTClaimsSet.Builder()
                // JWT ID 用于唯一标识一个 JWT
                .jwtID(UuidUtils.genUuidStr())
                // JWT 的发布者
                .issuer(claims.getIssuer())
                // JWT 的发放时间
                .issueTime(issueTime)
                // JWT 的受众（此处为用户 ID）
                .audience(uid)
                // JWT 的主题
                .subject(claims.getSubject())
                // JWT 的生效时间
                .notBeforeTime(issueTime)
                // JWT 的过期时间
                .expirationTime(new Date(issueTime.getTime() + (jwtProperties.getRefreshTokenDuration() * 1000L)))
                .build();
    }
}