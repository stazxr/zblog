package com.github.stazxr.zblog.base.security.filter;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.security.exception.CustomAuthenticationEntryPoint;
import com.github.stazxr.zblog.base.security.jwt.JwtTokenGenerator;
import com.github.stazxr.zblog.base.security.jwt.JwtTokenPair;
import com.github.stazxr.zblog.base.security.jwt.cache.JwtTokenStorage;
import com.github.stazxr.zblog.base.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * 认证失败处理
     */
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private final JwtTokenGenerator jwtTokenGenerator;

    private final JwtTokenStorage jwtTokenStorage;

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 如果已经通过认证
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        // 获取 header 解析出 jwt 并进行认证 无token 直接进入下一个过滤器  因为  SecurityContext 的缘故 如果无权限并不会放行
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(AUTHENTICATION_PREFIX)) {
            String jwtToken = header.replace(AUTHENTICATION_PREFIX, "");
            if (StringUtils.hasText(jwtToken)) {
                try {
                    authenticationTokenHandle(jwtToken, request);
                } catch (AuthenticationException e) {
                    log.warn("authentication token failed [{}]", jwtToken);
                    authenticationEntryPoint.commence(request, response, e);
                }
            } else {
                // 带安全头 没有带token
                authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException("token is not found."));
            }
        }

        // 没有带安全头
        chain.doFilter(request, response);
        // authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException("request header is valid."));
    }

    /**
     * 具体的认证方法  匿名访问不要携带token
     * 有些逻辑自己补充 这里只做基本功能的实现
     *
     * @param jwtToken jwt token
     * @param request  request
     */
    private void authenticationTokenHandle(String jwtToken, HttpServletRequest request) throws AuthenticationException {
        // 根据我的实现 有效token才会被解析出来
        JSONObject jsonObject = jwtTokenGenerator.decodeAndVerify(jwtToken);
        if (Objects.isNull(jsonObject)) {
            throw new BadCredentialsException("token is invalid.");
        }

        // 从缓存获取 token
        String username = jsonObject.getStr("aud");
        JwtTokenPair jwtTokenPair = jwtTokenStorage.get(username);
        if (Objects.isNull(jwtTokenPair)) {
            // 缓存中不存在就算 失败了
            throw new CredentialsExpiredException("token is expired.");
        }

        String accessToken = jwtTokenPair.getAccessToken();
        if (!jwtToken.equals(accessToken)) {
            throw new BadCredentialsException("token is not matched.");
        }

        // 解析权限集合这里
        JSONArray jsonArray = jsonObject.getJSONArray("roles");
        List<String> roles = jsonArray.toList(String.class);
        String[] roleArr = roles.toArray(new String[0]);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roleArr);

        // 构建用户认证token
        User user = userService.queryUserByUsername(username.toUpperCase());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 放入安全上下文中
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
