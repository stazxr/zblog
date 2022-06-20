package com.github.stazxr.zblog.base.security.filter;

import com.github.stazxr.zblog.base.domain.entity.Router;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.security.RouterBlackWhiteListCache;
import com.github.stazxr.zblog.base.security.config.CustomWebSecurityConfiguration;
import com.github.stazxr.zblog.base.security.exception.CustomAuthenticationEntryPoint;
import com.github.stazxr.zblog.base.security.exception.PreJwtCheckAuthenticationException;
import com.github.stazxr.zblog.base.security.jwt.TokenError;
import com.github.stazxr.zblog.base.security.jwt.ZblogToken;
import com.github.stazxr.zblog.base.security.jwt.cache.JwtTokenStorage;
import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.base.service.UserService;
import com.github.stazxr.zblog.core.base.BaseConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private static final String AUTHENTICATION_PREFIX = "Bearer ";

    private static final String URL_SPLIT_LABEL = "?";

    /**
     * 认证失败处理
     */
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private final JwtTokenStorage jwtTokenStorage;

    private final UserService userService;

    private final RouterService routerService;

    private final JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 判断请求是否需要验证token
        String requestUrl = request.getRequestURI();
        if (requestUrl.contains(URL_SPLIT_LABEL)) {
            requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
        }

        if (CustomWebSecurityConfiguration.LOGIN_PROCESSING_URL.equals(requestUrl)
                || RouterBlackWhiteListCache.containsWhite(requestUrl)) {
            // 登录请求放行，白名单放行
            chain.doFilter(request, response);
            return;
        }

        // 获取路由信息
        String requestMethod = request.getMethod();
        Router router = routerService.selectByUrlAndMethod(requestUrl, requestMethod);
        if (router == null || BaseConst.PermLevel.OPEN == router.getLevel()) {
            chain.doFilter(request, response);
            return;
        }

        // 获取 header 解析出 jwt 并进行认证 无token 直接进入下一个过滤器，因为 SecurityContext 的缘故，如果无权限并不会放行
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(AUTHENTICATION_PREFIX)) {
            String jwtToken = header.replace(AUTHENTICATION_PREFIX, "");
            if (StringUtils.hasText(jwtToken)) {
                try {
                    authenticationTokenHandle(jwtToken, request);
                    chain.doFilter(request, response);
                } catch (AuthenticationException e) {
                    log.warn("authentication token failed: {} [{}]", e.getMessage(), jwtToken);
                    authenticationEntryPoint.commence(request, response, e);
                }
            } else {
                // 带安全头 没有带token
                authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException(TokenError.TE007.value()));
            }
        } else {
            // 没有带安全头
            authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException(TokenError.TE008.value()));
        }
    }

    /**
     * 具体的认证方法  匿名访问不要携带token
     * 有些逻辑自己补充 这里只做基本功能的实现
     *
     * @param jwtToken jwt token
     * @param request  request
     */
    private void authenticationTokenHandle(String jwtToken, HttpServletRequest request) throws AuthenticationException {
        try {
            // decode jwt
            Jwt jwt = jwtDecoder.decode(jwtToken);

            // 从缓存获取 token
            List<String> audiences = jwt.getAudience();
            if (audiences == null || audiences.size() < 1) {
                throw new PreJwtCheckAuthenticationException(TokenError.TE003.value());
            }

            String username = audiences.get(0);
            ZblogToken token = jwtTokenStorage.get(username);
            if (Objects.isNull(token)) {
                throw new PreJwtCheckAuthenticationException(TokenError.TE004.value());
            }

            ZblogToken.AccessToken accessToken = token.getAccessToken();
            if (!jwtToken.equals(accessToken.getTokenValue())) {
                throw new PreJwtCheckAuthenticationException(TokenError.TE005.value());
            }

            // 解析权限集合这里
            Set<String> scopes = accessToken.getScopes();
            String[] roleArr = scopes.toArray(new String[0]);
            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roleArr);

            // 构建用户认证token, 放入上下文中
            User user = userService.queryUserByUsername(username.toUpperCase());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException e) {
            log.error("check token failed", e);
            throw new PreJwtCheckAuthenticationException(e);
        } catch (BadJwtException e) {
            String errorMsg = dealBadJwtException(e);
            throw new PreJwtCheckAuthenticationException(errorMsg);
        } catch (Exception e) {
            log.warn("check token catch eor: {} [{}]", e.getMessage(), jwtToken);
            throw new PreJwtCheckAuthenticationException(TokenError.TE006.value());
        }
    }

    private String dealBadJwtException(BadJwtException badJwtException) {
        String errorMsg = badJwtException.getMessage();
        return errorMsg.contains("Expired JWT") ? TokenError.TE001.value() : TokenError.TE002.value();
    }
}
