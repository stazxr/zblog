package com.github.stazxr.zblog.base.service.impl;

import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.security.core.UserType;
import com.github.stazxr.zblog.bas.security.jwt.JwtConstants;
import com.github.stazxr.zblog.bas.security.jwt.autoconfigure.properties.JwtCookieProperties;
import com.github.stazxr.zblog.bas.security.jwt.autoconfigure.properties.JwtProperties;
import com.github.stazxr.zblog.bas.security.jwt.decoder.JwtDecoder;
import com.github.stazxr.zblog.bas.security.jwt.encoder.JwtEncoder;
import com.github.stazxr.zblog.bas.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.bas.security.jwt.storage.TokenPayload;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.domain.bo.LoginUser;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.vo.MenuVo;
import com.github.stazxr.zblog.base.mapper.UserMapper;
import com.github.stazxr.zblog.base.service.AuthService;
import com.github.stazxr.zblog.base.service.MenuService;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 认证管理业务实现层
 *
 * @author SunTao
 * @since 2022-07-24
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final MenuService menuService;

    private final UserMapper userMapper;

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    private final JwtTokenStorage jwtTokenStorage;

    private final JwtProperties jwtProperties;

    private final JwtCookieProperties jwtCookieProperties;

    /**
     * 获取当前登录用户信息
     *
     * @return LoginUser
     */
    @Override
    public LoginUser currentUserDetail() {
        LoginUser loginUser = new LoginUser();

        // 获取用户信息
        User user = SecurityUtils.getLoginUser();
        loginUser.setUser(user);

        // 查询用户权限列表
        if (UserType.ADMIN_USER.getType().equals(user.getUserType())) {
            loginUser.setPerms(userMapper.selectAllMd5PermCodes());
        } else {
            loginUser.setPerms(userMapper.selectMd5PermCodesByUserId(user.getId()));
        }

        // 查询用户菜单列表
        List<MenuVo> menus = menuService.queryUserMenuTree();
        loginUser.setMenus(menus);
        return loginUser;
    }

    /**
     * 刷新令牌信息
     *
     * @param request  请求
     * @param response 响应
     * @return boolean 续签是否成功
     */
    @Override
    public boolean refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String lockKey = null;
        try {
            // 1、获取刷新令牌
            String refreshToken = getRefreshToken(request);
            if (StringUtils.isBlank(refreshToken)) {
                return false;
            }

            // 2、解码刷新令牌
            JWTClaimsSet claimsSet = jwtDecoder.decode(refreshToken);
            String userId = claimsSet.getSubject();
            if (StringUtils.isBlank(userId)) {
                return false;
            }

            // 3、并发刷新控制
            lockKey = "refresh_lock:" + userId;
            boolean locked = GlobalCache.setIfAbsent(lockKey, "1", 5, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("[AUTH_REFRESH] concurrent refresh blocked, userId={}", userId);
                return false;
            }

            // 4、校验刷新令牌是否过期
            Date renewTime = new Date();
            Date expirationTime = claimsSet.getExpirationTime();
            if (renewTime.after(expirationTime)) {
                log.warn("[AUTH_REFRESH] refreshToken expired, userId={}", userId);
                return false;
            }

            // 4、获取刷新令牌缓存信息
            TokenPayload tokenPayload = jwtTokenStorage.get(userId);
            if (tokenPayload == null) {
                log.warn("[AUTH_REFRESH] tokenPayload missing, userId={}", userId);
                return false;
            }

            // 5、越权校验
            String refreshTokenId = claimsSet.getJWTID();
            if (!refreshTokenId.equals(tokenPayload.getRefreshTokenId())) {
                log.warn("[AUTH_REFRESH] refreshToken mismatch, userId={}", userId);
                return false;
            }

            // 6、生成新的访问令牌
            String accessJwtId = "ATK_" + SequenceUtils.getId();
            String loginIp = claimsSet.getStringClaim(JwtConstants.LOGIN_IP_KEY);
            JWTClaimsSet accessClaims = buildAccessJwtChaimSet(renewTime, accessJwtId, userId, loginIp);
            String accessToken = jwtEncoder.encode(new JWSAlgorithm(jwtProperties.getAlgorithm()), accessClaims);

            // 7、刷新缓存
            tokenPayload.setAccessTokenId(accessJwtId);
            tokenPayload.setRenewTime(renewTime);
            jwtTokenStorage.update(userId, tokenPayload);

            // 8、写 Cookie
            writeTokenCookie(response, accessToken);

            log.info("[AUTH_REFRESH] success, userId={}, accessJwtId={}", userId, accessJwtId);
            return true;
        } catch (Exception e) {
            log.error("[AUTH_REFRESH] unexpected error, ip={}", IpUtils.getIp(request), e);
            return false;
        } finally {
            if (lockKey != null) {
                GlobalCache.remove(lockKey);
            }
        }
    }

    private String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if (JwtConstants.REFRESH_TOKEN.equals(name) && StringUtils.isNotBlank(value)) {
                    return value;
                }
            }
        }
        return null;
    }

    private void writeTokenCookie(HttpServletResponse response, String accessToken) {
        ResponseCookie accessCookie = ResponseCookie.from(JwtConstants.ACCESS_TOKEN, accessToken)
                .httpOnly(jwtCookieProperties.isHttpOnly())
                .secure(jwtCookieProperties.getSecure())
                .domain(jwtCookieProperties.getDomain())
                .path(jwtCookieProperties.getPath())
                .sameSite(jwtCookieProperties.getSameSite())
                .maxAge(Duration.ofSeconds(jwtProperties.getAccessTokenTtl()))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    }

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
            .expirationTime(new Date(issueTime.getTime() + (jwtProperties.getAccessTokenTtl() * 1000L)))
            // 用户登录 IP
            .claim(JwtConstants.LOGIN_IP_KEY, loginIp)
            .build();
    }
}
