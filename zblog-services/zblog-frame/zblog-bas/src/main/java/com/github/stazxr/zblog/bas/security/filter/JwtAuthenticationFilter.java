package com.github.stazxr.zblog.bas.security.filter;

import com.github.stazxr.zblog.bas.cache.util.GlobalCache;
import com.github.stazxr.zblog.bas.context.Context;
import com.github.stazxr.zblog.bas.context.constant.TagConstants;
import com.github.stazxr.zblog.bas.context.entity.ContextTag;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.security.SecurityConstant;
import com.github.stazxr.zblog.bas.security.SecurityExtProperties;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.security.authz.metadata.SecurityResourceService;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.core.TokenError;
import com.github.stazxr.zblog.bas.security.core.SecurityToken;
import com.github.stazxr.zblog.bas.security.exception.PreJwtCheckAuthenticationException;
import com.github.stazxr.zblog.bas.security.jwt.JwtConstants;
import com.github.stazxr.zblog.bas.security.jwt.JwtException;
import com.github.stazxr.zblog.bas.security.jwt.JwtProperties;
import com.github.stazxr.zblog.bas.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.bas.security.jwt.decoder.JwtDecoder;
import com.github.stazxr.zblog.bas.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.bas.security.service.SecurityLogoutService;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import com.github.stazxr.zblog.bas.security.service.SecurityTokenService;
import com.github.stazxr.zblog.bas.security.sso.SsoToken;
import com.github.stazxr.zblog.bas.security.sso.SsoTokenCache;
import com.github.stazxr.zblog.bas.validation.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.github.stazxr.zblog.util.thread.ThreadUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * jwt认证拦截器，用于拦截请求，提取jwt认证
 *
 * @author SunTao
 * @since 2022-01-29
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final Map<String, Boolean> RENEW_LOCK_MAP = new ConcurrentHashMap<>();

    private static final String AUTHENTICATION_PREFIX = SecurityConstant.AUTHENTICATION_PREFIX;

    /**
     * 最大等待续签时间 / 令牌被弃用后的一个最大存活期，单位秒
     */
    private static final int MAX_RENEW_WAIT_SECONDS = 30;

    /**
     * 等待续签的循环刷新间隔，单位毫秒
     */
    private static final long RENEW_WAIT_INTERVAL_MILL_SECONDS = 500;

    private static final String LOGIN_USER_ID_REQ_KEY = "LOGIN_USER_ID";

    private static final String AUTH_ERROR_MESSAGE_TEMPLATE = "An error occurred while authentication token: %s";

    /**
     *
     */
    private AuthenticationEntryPoint authenticationEntryPoint;

    private SecurityResourceService securityResourceService;

    private SecurityExtProperties securityExtProperties;

    private SecurityUserService securityUserService;

    private SecurityTokenService userTokenService;

    private SecurityLogoutService securityLogoutService;

    private JwtDecoder jwtDecoder;

    private JwtTokenStorage jwtTokenStorage;

    private JwtTokenGenerator jwtTokenGenerator;

    private JwtProperties jwtProperties;

    @Value("${error.path:/error}")
    private String errorPath = "/error";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        if (securityExtProperties.getLoginUrl().equals(requestURI) || errorPath.equals(requestURI)) {
            // 登录接口或 ERROR 接口直接访问
            chain.doFilter(request, response);
            return;
        }

        if (!requestURI.startsWith("/api/")) {
            // 非系统请求不鉴权
            chain.doFilter(request, response);
            return;
        }

        // 获取 Authorization
        boolean checkSuccess = false;
        boolean hasAuthorization = false;
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(AUTHENTICATION_PREFIX)) {
            String jwtToken = authorizationHeader.replace(AUTHENTICATION_PREFIX, "");
            if (StringUtils.isNotBlank(jwtToken)) {
                try {
                    // 验证 Token 并将用户信息写入上下文
                    hasAuthorization = true;
                    authenticationTokenHandle(jwtToken, request, response);
                    checkSuccess = true;
                } catch (AuthenticationException ex) {
                    authenticationWithError(request, response, jwtToken, ex);
                    return;
                }
            }
        }

        if (checkSuccess) {
            // 令牌校验成功
            chain.doFilter(request, response);
        }

        if (!hasAuthorization) {
            // 未携带 Authorization 请求头，视为用户未登录
            authenticationWithoutLogin(chain, request, response);
        }
    }

    private void authenticationTokenHandle(String jwtToken, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Date requestTime = new Date();
            JWTClaimsSet claimsSet = jwtDecoder.decode(jwtToken);

            // 获取用户信息
            SecurityUser loginUser = getUserFromClaimSet(claimsSet);
            String userId = String.valueOf(loginUser.getId());
            String username = loginUser.getUsername();
            request.setAttribute(LOGIN_USER_ID_REQ_KEY, userId);

            // 检查网络是否发生变化 TODO 只踢出当前客户端
            checkTokenLoginIp(request, claimsSet, username);

            // 检查前一个令牌
            String preTknCacheKey = String.format(Locale.ROOT, JwtConstants.PTK_TOKEN_CACHE_KEY, userId);
            String previousToken = GlobalCache.get(preTknCacheKey);
            boolean allowedRenewToken = claimsSet.getBooleanClaim(JwtConstants.RENEW_TOKEN_KEY);
            if (previousToken != null) {
                // 存在前置令牌
                if (previousToken.equals(jwtToken)) {
                    // 这里已经续签完成，通知前端刷新令牌，不做后续校验
                    log.warn("user {} token has been renew finish, but continue use the previous token", username);
                    String newToken = jwtTokenStorage.getAccessToken(userId);
                    if (newToken != null) {
                        response.addHeader(JwtConstants.NEW_TOKEN_HEADER, AUTHENTICATION_PREFIX.concat(newToken));
                        setContextAuthentication(request, loginUser);
                        return;
                    }
                }

                // 用户登录已过期
                throw new PreJwtCheckAuthenticationException(TokenError.TE002.value());
            } else {
                // 不存在前置令牌
                checkTokenConsistency(jwtToken, userId, username, allowedRenewToken);
            }

            // 检查令牌是否过期
            boolean expired = checkTokenIsExpired(claimsSet, requestTime);
            if (expired && !allowedRenewToken) {
                log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} token expired without renew"));
                throw new PreJwtCheckAuthenticationException(TokenError.TE008.value());
            }

            // 检查是否需要续签
            if (allowedRenewToken && checkTokenIsNeededRenew(claimsSet, requestTime)) {
                // 检查是否允许续签
                String refreshToken = jwtTokenStorage.getRefreshToken(userId);
                checkTokenIsAllowedRenew(claimsSet, expired, refreshToken, username);

                // 检查当前用户是进行续签操作还是等待操作
                Assert.doIfElse(checkRequestDoOrWait(userId, username), () -> {
                    // 续签操作
                    renewToken(request, response, jwtToken, userId, username, claimsSet);
                }, () -> {
                    // 续签中，等待续签操作结束
                    if (expired) {
                        int count = 0;
                        long maxWaitCount = MAX_RENEW_WAIT_SECONDS * 1000 / RENEW_WAIT_INTERVAL_MILL_SECONDS;
                        do {
                            ThreadUtils.sleepMillisecond(RENEW_WAIT_INTERVAL_MILL_SECONDS);
                            if (count++ > maxWaitCount) {
                                log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} renew token failed, overtime"), username);
                                throw new PreJwtCheckAuthenticationException(TokenError.TE012.value());
                            }
                        } while (RENEW_LOCK_MAP.containsKey(userId));

                        // 续签完成
                        String newAtk = jwtTokenStorage.getAccessToken(userId);
                        if (StringUtils.isBlank(newAtk)) {
                            log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} renew token failed"), username);
                            throw new PreJwtCheckAuthenticationException(TokenError.TE011.value());
                        } else {
                            // 通知前端刷新令牌
                            response.addHeader(JwtConstants.NEW_TOKEN_HEADER, AUTHENTICATION_PREFIX.concat(newAtk));
                        }
                    }
                });
            }

            // set UsernamePasswordAuthenticationToken
            setContextAuthentication(request, loginUser);
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

    private void authenticationWithoutLogin(FilterChain chain, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int resourceLevel = securityResourceService.getResourceLevel(request.getRequestURI(), request.getMethod());
        if (RouterLevel.OPEN == resourceLevel) {
            // 可直接访问的接口忽略令牌校验
            chain.doFilter(request, response);
            return;
        }

        // 抛出异常
        log.error("check resource XXX {}:{}:{}", request.getRequestURI(), request.getMethod(), resourceLevel);
        authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException(TokenError.TE001.value()));
    }

    private void authenticationWithError(HttpServletRequest request, HttpServletResponse response, String jwtToken, AuthenticationException ex) throws ServletException, IOException {
        if (request.getAttribute(LOGIN_USER_ID_REQ_KEY) != null) {
            securityLogoutService.clearUserLoginInfo(String.valueOf(request.getAttribute(LOGIN_USER_ID_REQ_KEY)));
        }
        log.error("authentication token handle execute failed: {}", jwtToken);
        authenticationEntryPoint.commence(request, response, ex);
    }

    private void setContextAuthentication(HttpServletRequest request, SecurityUser user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 登录用户id
        String loginId = String.valueOf(SecurityUtils.getLoginId());
        Context.put(new ContextTag(TagConstants.LOGIN_ID_TAG, loginId));
    }

    private void renewToken(HttpServletRequest request, HttpServletResponse response, String jwtToken, String userId, String username, JWTClaimsSet claimsSet) {
        try {
            // generate new token
            log.info("续签 - 用户 {}: 开始续签", username);
            int version = claimsSet.getIntegerClaim(JwtConstants.JWT_VERSION_KEY);
            String loginIp = claimsSet.getStringClaim(JwtConstants.LOGIN_IP_KEY);
            int newVersion = version + 1;
            String newToken = jwtTokenGenerator.generateToken(request, userId, newVersion, loginIp);

            // cache previous token
            int preTokenExpiredTime = getPreTokenFreeTime(jwtToken);
            String preTknCacheKey = String.format(Locale.ROOT, JwtConstants.PTK_TOKEN_CACHE_KEY, userId);
            GlobalCache.put(preTknCacheKey, jwtToken, preTokenExpiredTime, TimeUnit.SECONDS);

            // set token
            response.addHeader(JwtConstants.NEW_TOKEN_HEADER, AUTHENTICATION_PREFIX.concat(newToken));
            SecurityToken userToken = new SecurityToken(userId, newToken);
            userToken.setRemark("续签: ".concat(String.valueOf(newVersion)));
            userToken.updateModifyTime();
            userTokenService.saveToken(userToken);
            log.info("续签 - 用户 {}: 续签成功，版本【{}】", username, newVersion);

            // set sso token
            SsoToken ssoToken = new SsoToken(userId, newToken, IpUtils.getIp(request));
            SsoTokenCache.set(ssoToken);
        } catch (Exception ex) {
            log.error("续签 - 用户 {}: 续签失败", username);
            log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} renew token failed"), username, ex);
            throw new PreJwtCheckAuthenticationException(TokenError.TE011.value(), ex);
        } finally {
            log.info("续签 - 用户 {}: 释放锁", username);
            RENEW_LOCK_MAP.remove(userId);
        }
    }

    protected int getPreTokenFreeTime(String jwtToken) {
        try {
            // 此处设置不过期
            return 0;
        } catch (Exception e) {
            log.warn(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "get pre token free time catch unexpected error, use 'MAX_RENEW_WAIT_SECONDS * 2', jwt is: " + jwtToken));
            return MAX_RENEW_WAIT_SECONDS * 2;
        }
    }

    private synchronized boolean checkRequestDoOrWait(String userId, String username) {
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
            // accessToken 已过期，校验 refreshToken 是否过期
            if (StringUtils.isBlank(refreshToken)) {
                log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} refresh token expired"), username);
                throw new PreJwtCheckAuthenticationException(TokenError.TE009.value());
            }
        } else {
            // accessToken 未过期，校验 version 是否已经达到了阙值
            int version = claimsSet.getIntegerClaim("version");
            int maxVersion = jwtProperties.getMaxVersion();
            if (version >= maxVersion) {
                log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} renew token over max version: {}"), username, maxVersion);
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

    private void checkTokenConsistency(String jwtToken, String userId, String username, boolean allowedRenewToken) {
        String accessToken = jwtTokenStorage.getAccessToken(userId);
        if (accessToken == null) {
            // 令牌已经过期
            if (allowedRenewToken) {
                // 续签功能开启的情况下，需要对比持久化的 Token
                SecurityToken userToken = userTokenService.getToken(userId);
                if (userToken != null && jwtToken.equals(userToken.getToken())) {
                    return;
                }

                log.warn(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} check token consistency failed, token may different or expired forever"), username);
            }
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
            String loginIp = claimsSet.getStringClaim(JwtConstants.LOGIN_IP_KEY);
            String requestIp = IpUtils.getIp(request);
            if (loginIp.equals(requestIp)) {
                return;
            }

            log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} check ip failed, requestIp: {}, loginId: {}"), username, requestIp, loginIp);
        } catch (Exception ex) {
            // loginIp not exist or parse eor
            log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "user {} check ip failed"), username, ex);
        }

        throw new PreJwtCheckAuthenticationException(TokenError.TE006.value());
    }

    protected SecurityUser getUserFromClaimSet(JWTClaimsSet claimsSet) {
        try {
            List<String> audiences = claimsSet.getAudience();
            if (audiences != null && !audiences.isEmpty()) {
                String userId = audiences.get(0);
                SecurityUser cacheUser = SecurityUserCache.get(userId);
                if (cacheUser != null) {
                    // find user from cache
                    return cacheUser;
                }

                // find user from service
                SecurityUser dbUser = securityUserService.findUserById(userId);
                if (dbUser != null) {
                    return SecurityUserCache.put(userId, dbUser);
                }

                log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "audience {} is not inner user"), userId);
            }
        } catch (Exception ex) {
            // parse aud catch eor
            log.error(String.format(AUTH_ERROR_MESSAGE_TEMPLATE, "check audience failed"), ex);
        }

        throw new PreJwtCheckAuthenticationException(TokenError.TE005.value());
    }

    @Autowired
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Autowired
    public void setSecurityResourceService(SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
    }

    @Autowired
    public void setSecurityExtProperties(SecurityExtProperties securityExtProperties) {
        this.securityExtProperties = securityExtProperties;
    }

    @Autowired
    public void setSecurityUserService(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @Autowired
    public void setClearUserLoginManager(SecurityLogoutService clearUserLoginManager) {
        this.securityLogoutService = clearUserLoginManager;
    }

    @Autowired
    public void setJwtDecoder(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Autowired
    public void setJwtTokenStorage(JwtTokenStorage jwtTokenStorage) {
        this.jwtTokenStorage = jwtTokenStorage;
    }

    @Autowired
    public void setUserTokenService(SecurityTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @Autowired
    public void setJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Autowired
    public void setJwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }
}
