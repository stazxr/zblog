package com.github.stazxr.zblog.bas.security.jwt.filter;

import com.github.stazxr.zblog.bas.context.Context;
import com.github.stazxr.zblog.bas.context.constant.TagConstants;
import com.github.stazxr.zblog.bas.context.entity.ContextTag;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.bas.security.authz.metadata.SecurityResourceService;
import com.github.stazxr.zblog.bas.security.cache.SecurityUserCache;
import com.github.stazxr.zblog.bas.security.core.SecurityUser;
import com.github.stazxr.zblog.bas.security.core.TokenError;
import com.github.stazxr.zblog.bas.security.core.UserStatus;
import com.github.stazxr.zblog.bas.security.jwt.JwtConstants;
import com.github.stazxr.zblog.bas.security.jwt.autoconfigure.properties.JwtProperties;
import com.github.stazxr.zblog.bas.security.jwt.decoder.JwtDecoder;
import com.github.stazxr.zblog.bas.security.jwt.decoder.JwtDecodingException;
import com.github.stazxr.zblog.bas.security.jwt.exception.JwtAuthenticationException;
import com.github.stazxr.zblog.bas.security.jwt.storage.JwtTokenStorage;
import com.github.stazxr.zblog.bas.security.jwt.storage.TokenPayload;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * jwt认证拦截器
 *
 * @author SunTao
 * @since 2022-01-29
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final String ERROR_PREFIX = "Token 认证异常: ";

    private JwtDecoder jwtDecoder;

    private JwtTokenStorage jwtTokenStorage;

    private JwtProperties jwtProperties;

    private SecurityUserService securityUserService;

    private SecurityResourceService securityResourceService;

    private AuthenticationEntryPoint authenticationEntryPoint;

    private String errorPath;

    private String loginPath;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws IOException, ServletException {
        // 白名单
        String requestURI = request.getRequestURI();
        if (requestURI.equals(loginPath) || requestURI.equals(errorPath) || !requestURI.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        // 令牌解析
        String jwtToken = getTokenFromCookie(request);
        if (StringUtils.isNotBlank(jwtToken)) {
            try {
                authenticationTokenHandle(jwtToken, request);
                chain.doFilter(request, response);
            } catch (AuthenticationException ex) {
                boolean isOpenApi;
                try {
                    isOpenApi = isOpenResource(request);
                } catch (Exception ex1) {
                    authenticationEntryPoint.commence(request, response, new JwtAuthenticationException(TokenError.TE099, ex1));
                    return;
                }
                if (isOpenApi) {
                    if (ex instanceof JwtAuthenticationException) {
                        JwtAuthenticationException jex = (JwtAuthenticationException) ex;
                        TokenError tokenError = jex.getTokenError();
                        response.setHeader(JwtConstants.X_TOKEN_STATUS, tokenError.getLabel());
                    }
                    request.setAttribute(JwtConstants.__TOKEN_ERROR, ex);
                    chain.doFilter(request, response);
                } else {
                    authenticationEntryPoint.commence(request, response, ex);
                }
            } finally {
                SecurityContextHolder.clearContext();
            }
        } else {
            boolean isOpenApi;
            try {
                isOpenApi = isOpenResource(request);
            } catch (Exception ex) {
                authenticationEntryPoint.commence(request, response, new JwtAuthenticationException(TokenError.TE099, ex));
                return;
            }
            if (isOpenApi) {
                chain.doFilter(request, response);
            } else {
                authenticationEntryPoint.commence(request, response, new JwtAuthenticationException(TokenError.TE001));
            }
        }
    }

    /** 从 Cookie 中获取 jwt token */
    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if (JwtConstants.ACCESS_TOKEN.equals(name) && StringUtils.isNotBlank(value)) {
                    return value;
                }
            }
        }
        return null;
    }

    /** 判断接口是否是开放接口 */
    private boolean isOpenResource(HttpServletRequest request) {
        int resourceLevel = securityResourceService.getResourceLevel(request.getRequestURI(), request.getMethod());
        request.setAttribute(JwtConstants.__RESOURCE_LEVEL, resourceLevel);
        return RouterLevel.OPEN == resourceLevel;
    }

    private void authenticationTokenHandle(String accessToken, HttpServletRequest request) throws AuthenticationException {
        try {
            // 解码 token 获取 JWTClaimsSet 对象
            Date requestTime = new Date();
            JWTClaimsSet claimsSet = jwtDecoder.decode(accessToken);

            // 检查 token 缓存信息
            String userId = claimsSet.getSubject();
            request.setAttribute(JwtConstants.__WEAK_USER_ID, userId);
            TokenPayload tokenPayload = jwtTokenStorage.get(userId);
            if (tokenPayload == null) {
                throw new JwtAuthenticationException(TokenError.TE009);
            }

            // 检查 access token 信息
            checkAccessToken(request, tokenPayload, claimsSet, requestTime);

            // 设置 token 上次访问时间
            updateLastAccessTime(userId, tokenPayload, requestTime);

            // 获取用户信息
            SecurityUser loginUser = loadUserFromClaimSet(userId);

            // 设置 Authentication 上下文
            setContextAuthentication(request, loginUser);
        } catch (JwtAuthenticationException ex) {
            throw ex;
        } catch (JwtDecodingException ex) {
            throw new JwtAuthenticationException(TokenError.TE002, ex);
        } catch (Exception ex) {
            throw new JwtAuthenticationException(TokenError.TE099, ex);
        }
    }

    protected SecurityUser loadUserFromClaimSet(String userId) {
        try {
            if (StringUtils.isNotBlank(userId)) {
                SecurityUser user = SecurityUserCache.getOrLoad(userId, securityUserService::loadUserInfo);
                if (user != null) {
                    if (UserStatus.FORBID.getStatus().equals(user.getUserStatus())) {
                        throw new JwtAuthenticationException(TokenError.TE003);
                    }
                    if (UserStatus.LOCKED.getStatus().equals(user.getUserStatus())) {
                        throw new JwtAuthenticationException(TokenError.TE004);
                    }
                    return user;
                }
            }
        } catch (JwtAuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("{} 用户信息查询失败: {}", ERROR_PREFIX, userId, ex);
        }

        throw new JwtAuthenticationException(TokenError.TE005);
    }

    private void checkAccessToken(HttpServletRequest request, TokenPayload tokenPayload, JWTClaimsSet claimsSet, Date requestTime) {
        // 校验过期
        if (requestTime.after(claimsSet.getExpirationTime())) {
            throw new JwtAuthenticationException(TokenError.TE010);
        }
        // 校验越权
        if (!claimsSet.getJWTID().equals(tokenPayload.getAccessTokenId())) {
            throw new JwtAuthenticationException(TokenError.TE006);
        }
        // 校验踢人
        if (Boolean.TRUE.equals(tokenPayload.isKickOut())) {
            throw new JwtAuthenticationException(TokenError.TE007);
        }
        // 校验访问IP
        checkLoginIp(request, claimsSet);
    }

    private void checkLoginIp(HttpServletRequest request, JWTClaimsSet claimsSet) {
        if (jwtProperties.isCheckIpChange()) {
            try {
                String loginIp = claimsSet.getStringClaim(JwtConstants.LOGIN_IP_KEY);
                String requestIp = IpUtils.getIp(request);
                if (requestIp.equals(loginIp)) {
                    return;
                }
                log.error("{} 用户 [{}] IP发生变化 {} >>> {}", ERROR_PREFIX, claimsSet.getSubject(), loginIp, requestIp);
            } catch (Exception ex) {
                log.error("{} 用户 [{}] IP检查异常", claimsSet.getSubject(), ERROR_PREFIX, ex);
            }

            throw new JwtAuthenticationException(TokenError.TE008);
        }
    }

    private void updateLastAccessTime(String userId, TokenPayload tokenPayload, Date requestTime) {
        tokenPayload.setLastAccessTime(requestTime);
        jwtTokenStorage.update(userId, tokenPayload);
    }

    private void setContextAuthentication(HttpServletRequest request, SecurityUser user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        Context.put(new ContextTag(TagConstants.LOGIN_ID_TAG, String.valueOf(user.getId())));
    }

    public void setJwtDecoder(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public void setJwtTokenStorage(JwtTokenStorage jwtTokenStorage) {
        this.jwtTokenStorage = jwtTokenStorage;
    }

    public void setJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public void setSecurityUserService(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    public void setSecurityResourceService(SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
    }

    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    public void setErrorPath(String errorPath) {
        this.errorPath = errorPath;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }
}
