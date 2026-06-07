package com.github.stazxr.zblog.bas.security.authz.handler;

import com.github.stazxr.zblog.bas.i18n.I18nUtils;
import com.github.stazxr.zblog.bas.rest.Result;
import com.github.stazxr.zblog.bas.rest.util.ResponseUtils;
import com.github.stazxr.zblog.bas.security.core.TokenError;
import com.github.stazxr.zblog.bas.security.jwt.exception.JwtAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义认证入口点，实现 Spring Security 提供的 {@link AuthenticationEntryPoint} 接口。
 * 用于处理未认证用户访问受保护资源时的逻辑。
 *
 * @author SunTao
 * @since 2024-11-18
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    /**
     * 处理认证异常。
     *
     * @param request       当前 HTTP 请求
     * @param response      当前 HTTP 响应
     * @param authException 认证异常信息
     * @throws IOException 可能的 IO 异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 记录认证异常日志
        logAuthenticationError(authException);

        // 提取异常信息
        TokenError tokenError = extractTokenError(authException);

        // 返回结果
        String errorMessage = I18nUtils.getMessage(tokenError.getLabel());
        Result<?> result = Result.failure(tokenError.getCode(), errorMessage).type(tokenError.getType().name());
        ResponseUtils.responseJsonWriter(response, result, HttpStatus.UNAUTHORIZED);
    }

    private TokenError extractTokenError(AuthenticationException authException) {
        if (authException instanceof JwtAuthenticationException) {
            return ((JwtAuthenticationException) authException).getTokenError();
        }
        return TokenError.TE099;
    }

    /**
     * 记录认证异常日志。
     *
     * @param authException 认证异常
     */
    private void logAuthenticationError(AuthenticationException authException) {
        log.error("[401] authentication failed", authException);
    }
}
