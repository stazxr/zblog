package com.github.stazxr.zblog.base.component.security.filter;

import com.github.stazxr.zblog.base.component.security.exception.NumCodeException;
import com.github.stazxr.zblog.base.component.security.handler.CustomAuthenticationFailureHandler;
import com.github.stazxr.zblog.base.component.security.config.CustomWebSecurityConfiguration;
import com.github.stazxr.zblog.core.util.CacheUtils;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码认证过滤器
 *
 * @author SunTao
 * @since 2020-11-14
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateLoginCodeFilter extends OncePerRequestFilter {
    public static final String DEFAULT_CODE_KEY = "code";

    public static final String DEFAULT_CACHE_KEY = "uuid";

    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        // 拦截登录请求，进行验证码验证
        if (CustomWebSecurityConfiguration.LOGIN_PROCESSING_URL.equals(request.getRequestURI())
                && HttpMethod.POST.toString().equalsIgnoreCase(request.getMethod())) {
            // 获取用户输入的验证码
            String uuid = (String) request.getAttribute(DEFAULT_CACHE_KEY);
            if (StringUtils.isEmpty(uuid)) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new NumCodeException("验证码验证失败，缺失参数" + DEFAULT_CACHE_KEY + "，请检查前端代码"));
                return;
            }

            String code = (String) request.getAttribute(DEFAULT_CODE_KEY);
            if (StringUtils.isEmpty(code)) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new NumCodeException("请输入验证码"));
                return;
            }

            // 获取后台绑定的验证码信息，并判断是否为空
            String cacheCode = CacheUtils.getThenRemove(uuid);
            if (cacheCode == null) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new NumCodeException("验证码已过期"));
                return;
            }

            // 判断验证码是否正确
            if (!cacheCode.equalsIgnoreCase(code)) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new NumCodeException("验证码不正确"));
                return;
            }
        }

        // 校验通过
        filterChain.doFilter(request, response);
    }
}
