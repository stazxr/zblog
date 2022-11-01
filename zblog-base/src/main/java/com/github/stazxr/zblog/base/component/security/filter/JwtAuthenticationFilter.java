package com.github.stazxr.zblog.base.component.security.filter;

import com.github.stazxr.zblog.base.component.security.handler.UserCacheHandler;
import com.github.stazxr.zblog.base.component.security.jwt.JwtException;
import com.github.stazxr.zblog.base.component.security.jwt.JwtProperties;
import com.github.stazxr.zblog.base.component.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.base.component.security.jwt.decoder.JwtDecoder;
import com.github.stazxr.zblog.base.domain.entity.Role;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.component.security.exception.CustomAuthenticationEntryPoint;
import com.github.stazxr.zblog.base.component.security.exception.PreJwtCheckAuthenticationException;
import com.github.stazxr.zblog.base.component.security.jwt.TokenError;
import com.github.stazxr.zblog.base.component.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.base.domain.entity.UserTokenStorage;
import com.github.stazxr.zblog.base.service.PermissionService;
import com.github.stazxr.zblog.base.service.RoleService;
import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.base.util.Constants;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.util.CacheUtils;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.github.stazxr.zblog.util.thread.ThreadUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * jwt认证拦截器，用于拦截请求，提取jwt认证
 *
 * @author SunTao
 * @since 2022-01-29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final Map<Long, Boolean> RENEW_LOCK_MAP = new ConcurrentHashMap<>();

    private static final String AUTHENTICATION_PREFIX = Constants.AUTHENTICATION_PREFIX;

    private static final String PASSWORD = "******";

    /**
     * 最大等待续签时间 / 令牌被弃用后的一个最大存活期，单位秒
     */
    private static final int MAX_RENEW_WAIT_SECONDS = 30;

    /**
     * 等待续签的循环刷新间隔，单位毫秒
     */
    private static final int RENEW_WAIT_INTERVAL_MILL_SECONDS = 500;

    private static final String PREVIOUS_TOKEN_KEY_TEMPLATE = "previousTkn:%s";

    private static final String AUTH_ERROR_MESSAGE_TEMPLATE = "An error occurred while authentication token: %s";

    /**
     * 认证失败处理
     */
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private final UserService userService;

    private final RoleService roleService;

    private final PermissionService permissionService;

    private final RouterService routerService;

    private final UserCacheHandler userCacheHandler;

    private final JwtTokenStorage jwtTokenStorage;

    private final JwtTokenGenerator jwtTokenGenerator;

    private final JwtDecoder jwtDecoder;

    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws IOException, ServletException {
        int level = routerService.calculateInterfaceLevel(request.getRequestURI(), request.getMethod());
        if (BaseConst.PermLevel.OPEN == level) {
            // 可直接访问的接口忽略令牌校验
            chain.doFilter(request, response);
            return;
        }

        // 获取 header 解析出 jwt 并进行认证 无token 直接进入下一个过滤器，因为 SecurityContext 的缘故，如果无权限并不会放行
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(header) && header.startsWith(AUTHENTICATION_PREFIX)) {
            String jwtToken = header.replace(AUTHENTICATION_PREFIX, "");
            if (StringUtils.isNotBlank(jwtToken)) {
                try {
                    // 验证 Token 并将用户信息写入上下文
                    authenticationTokenHandle(jwtToken, request, response);
                    chain.doFilter(request, response);
                } catch (AuthenticationException ex) {
                    log.error("authentication token handle exec failed: {} - [{}]", ex.getMessage(), jwtToken);
                    authenticationEntryPoint.commence(request, response, ex);
                }
            } else {
                // 带安全头 没有带token
                authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException(TokenError.TE002.value()));
            }
        } else {
            // 没有带安全头
            authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException(TokenError.TE001.value()));
        }
    }

    /**
     * 具体的认证方法  匿名访问不要携带token
     * 有些逻辑自己补充 这里只做基本功能的实现
     *
     * @param jwtToken jwt token
     * @param request  request
     */
    private void authenticationTokenHandle(String jwtToken, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Date requestTime = new Date();
            JWTClaimsSet claimsSet = jwtDecoder.decode(jwtToken);

            // get user
            User user = getUserFromClaimSet(claimsSet);

            // check loginIp
            String username = user.getUsername();
            checkTokenLoginIp(request, claimsSet, username);

            // check is user previous token
            Long userId = user.getId();
            String previousToken = CacheUtils.get(String.format(PREVIOUS_TOKEN_KEY_TEMPLATE, userId));
            boolean isPrevious = previousToken != null && previousToken.equals(jwtToken);
            if (isPrevious) {
                // 这里Token已经续签完成，不做后续校验
                log.warn("user {} token has been renew finish, but continue use the previous token, request '{}'", username, request.getRequestURL());
                setContextAuthentication(request, user);
                return;
            } else {
                // check token consistency
                checkTokenIsMatch(jwtToken, userId, username);
            }

            // check token is expired
            boolean expired = checkTokenIsExpired(claimsSet, requestTime);
            boolean allowedRenewToken = claimsSet.getBooleanClaim("renewToken");
            if (expired && !allowedRenewToken) {
                log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} token expired without renew"));
                throw new PreJwtCheckAuthenticationException(TokenError.TE008.value());
            }

            // check is need to renew token
            boolean needRenewToken = checkTokenIsNeededRenew(claimsSet, requestTime);
            if (needRenewToken) {
                // check is allowed renew token
                String refreshToken = jwtTokenStorage.getRefreshToken(userId);
                checkTokenIsAllowedRenew(claimsSet, expired, refreshToken, username);

                // renewToken
                Assert.isTrue(checkRequestDoOrWait(userId, username), () -> renewToken(request, response, jwtToken, user, claimsSet), () -> {
                    if (expired) {
                        int count = 0;
                        int maxWaitCount = MAX_RENEW_WAIT_SECONDS * 1000 / RENEW_WAIT_INTERVAL_MILL_SECONDS;
                        do {
                            ThreadUtils.sleepMillisecond(RENEW_WAIT_INTERVAL_MILL_SECONDS);
                            if (count++ > maxWaitCount) {
                                log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} renew token failed, overtime"), username);
                                throw new PreJwtCheckAuthenticationException(TokenError.TE012.value());
                            }
                        } while (RENEW_LOCK_MAP.containsKey(userId));

                        // renew token finish, check result is success
                        if (StringUtils.isBlank(jwtTokenStorage.getAccessToken(userId))) {
                            log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} renew token failed"), username);
                            throw new PreJwtCheckAuthenticationException(TokenError.TE011.value());
                        }
                    }
                });
            }

            // set UsernamePasswordAuthenticationToken
            setContextAuthentication(request, user);
        } catch (PreJwtCheckAuthenticationException ex) {
            throw ex;
        } catch (JwtException ex) {
            log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "decode token error"), ex);
            throw new PreJwtCheckAuthenticationException(TokenError.TE003.value(), ex);
        } catch (Exception ex) {
            // unexpected eor, may parse chaim item error
            log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "catch unexpected error"), ex);
            throw new PreJwtCheckAuthenticationException(TokenError.TE004.value(), ex);
        }
    }

    private void setContextAuthentication(HttpServletRequest request, User user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, PASSWORD, user.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    private void renewToken(HttpServletRequest request, HttpServletResponse response, String jwtToken, User user, JWTClaimsSet claimsSet) {
        Long userId = user.getId();
        String username = user.getUsername();
        try {
            // generate new token
            log.info("续签 - 用户 {}: 开始续签", username);
            int version = claimsSet.getIntegerClaim("version");
            String loginIp = claimsSet.getStringClaim("loginIp");
            int newVersion = version + 1;
            String newToken = jwtTokenGenerator.generateToken(request, user, newVersion, loginIp);

            // cache previous token
            CacheUtils.put(String.format(PREVIOUS_TOKEN_KEY_TEMPLATE, userId), jwtToken, MAX_RENEW_WAIT_SECONDS);

            // set token
            response.addHeader("new-token", Constants.AUTHENTICATION_PREFIX.concat(newToken));
            String remark = "续签: ".concat(String.valueOf(newVersion));
            UserTokenStorage tokenStorage = UserTokenStorage.builder().userId(userId).lastedToken(newToken).version(newVersion).remark(remark).build();
            userService.storageUserToken(tokenStorage, 1);
            log.info("续签 - 用户 {}: 续签成功，版本【{}】", username, newVersion);
        } catch (Exception ex) {
            jwtTokenStorage.expire(userId);
            CacheUtils.remove(String.format(PREVIOUS_TOKEN_KEY_TEMPLATE, userId));
            log.error("续签 - 用户 {}: 续签失败", username);
            log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} renew token catch error"), username, ex);
            throw new PreJwtCheckAuthenticationException(TokenError.TE011.value(), ex);
        } finally {
            log.info("续签 - 用户 {}: 释放锁", username);
            RENEW_LOCK_MAP.remove(userId);
        }
    }

    private synchronized boolean checkRequestDoOrWait(Long userId, String username) {
        if (RENEW_LOCK_MAP.containsKey(userId)) {
            return false;
        } else {
            log.info("续签 - 用户 {}: 上锁", username);
            RENEW_LOCK_MAP.put(userId, true);
            return true;
        }
    }

    private void checkTokenIsAllowedRenew(JWTClaimsSet claimsSet, boolean expired, String refreshToken, String username) throws ParseException {
        if (expired) {
            // accessToken已过期，校验refreshToken是否过期
            if (StringUtils.isBlank(refreshToken)) {
                log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} refresh token expired"), username);
                throw new PreJwtCheckAuthenticationException(TokenError.TE009.value());
            }
        } else {
            // accessToken未过期，校验version是否已经达到了阙值
            int version = claimsSet.getIntegerClaim("version");
            int maxVersion = jwtProperties.getMaxVersion();
            if (version >= maxVersion) {
                log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} renew token over max count[{}]"), username, maxVersion);
                throw new PreJwtCheckAuthenticationException(TokenError.TE010.value());
            }
        }
    }

    private boolean checkTokenIsNeededRenew(JWTClaimsSet claimsSet, Date requestTime) {
        int maxSafeTime = jwtProperties.getAccessTokenDuration() - jwtProperties.getRefreshMinDuration();
        Date minNeedRenewTime = new Date(claimsSet.getIssueTime().getTime() + (maxSafeTime * 1000L));
        return requestTime.after(minNeedRenewTime);
    }

    private boolean checkTokenIsExpired(JWTClaimsSet claimsSet, Date requestTime) {
        Date expirationTime = claimsSet.getExpirationTime();
        return requestTime.after(expirationTime);
    }

    private void checkTokenIsMatch(String jwtToken, Long userId, String username) {
        String accessToken = jwtTokenStorage.getAccessToken(userId);
        if (accessToken == null) {
            // 令牌已经过期，对比持久化的Token
            UserTokenStorage tokenStorage = userService.queryUserStorageToken(userId);
            if (tokenStorage != null && jwtToken.equals(tokenStorage.getLastedToken())) {
                return;
            }

            log.warn(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} check token consistency failed, token may different or expired forever"), username);
        } else {
            // 令牌未过期
            if (jwtToken.equals(accessToken)) {
                return;
            }
        }

        throw new PreJwtCheckAuthenticationException(TokenError.TE007.value());
    }

    private void checkTokenLoginIp(HttpServletRequest request, JWTClaimsSet claimsSet, String username) {
        try {
            String loginIp = claimsSet.getStringClaim("loginIp");
            String requestIp = IpUtils.getIp(request);
            if (loginIp.equals(requestIp)) {
                return;
            }

            log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} check token ip failed, requestIp: {}, loginId: {}"), username, requestIp, loginIp);
        } catch (Exception ex) {
            // loginIp not exist or parse eor
            log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} check token ip catch eor"), username, ex);
        }

        throw new PreJwtCheckAuthenticationException(TokenError.TE006.value());
    }

    protected User getUserFromClaimSet(JWTClaimsSet claimsSet) {
        try {
            List<String> audiences = claimsSet.getAudience();
            if (audiences != null && !audiences.isEmpty()) {
                Long userId = Long.parseLong(audiences.get(0));
                User cacheUser = userCacheHandler.getUserFromCache(userId);
                if (cacheUser == null) {
                    // from db
                    User dbUser = findUserBySearchDb(userId);
                    if (dbUser != null) {
                        userCacheHandler.putUserInCache(userId, dbUser);
                        return dbUser;
                    }
                } else {
                    // from cache
                    return cacheUser;
                }

                log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "aud {} is not inner user"), userId);
            }
        } catch (Exception ex) {
            // parse aud catch eor
            log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "check aud catch eor"), ex);
        }

        throw new PreJwtCheckAuthenticationException(TokenError.TE005.value());
    }

    private User findUserBySearchDb(Long userId) {
        User user = userService.getById(userId);
        if (user != null) {
            List<Role> userRoles = roleService.queryRolesByUserId(user.getId());
            user.setAuthorities(userRoles);

            Set<String> userPerms = permissionService.queryUserPerms(user.getId());
            user.setPerms(userPerms);
        }

        return user;
    }
}
