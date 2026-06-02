package com.github.stazxr.zblog.bas.security.hanlder;

import com.github.stazxr.zblog.bas.rest.Result;
import com.github.stazxr.zblog.bas.rest.ResultType;
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
        TokenError tokenError = extractErrorMessage(authException);
        String noticeMsg = generateNoticeMessage(tokenError);

        // 返回结果 401
        Result<?> result = Result.failure(noticeMsg).type(ResultType.TOKEN_EXPIRED);
        ResponseUtils.responseJsonWriter(response, result, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 提取异常信息，获取详细错误消息。
     *
     * @param authException 认证异常
     * @return 错误消息字符串
     */
    private TokenError extractErrorMessage(AuthenticationException authException) {
        if (authException instanceof JwtAuthenticationException) {
            return ((JwtAuthenticationException) authException).getTokenError();
        }
        return null;
    }

    /**
     * 根据异常信息生成用户友好的提示消息。
     *
     * @param errorMsg      错误消息字符串
     * @return 友好提示消息
     */
    private String generateNoticeMessage(TokenError tokenError) {
        if (tokenError == null) {
            return "用户未认证，请检查登录状态";
        }
        if (TokenError.Const.NO_LOGIN.equals(tokenError.getLabel())) {
            return "用户未登录";
        }
        if (TokenError.Const.EXPIRED.equals(tokenError.getLabel())) {
            return "当前登录状态已过期，请重新登录";
        }
        if (TokenError.Const.BUSY.equals(tokenError.getLabel())) {
            return "系统繁忙，请稍后再试";
        }
        if (TokenError.Const.EXCEPTION.equals(tokenError.getLabel())) {
            return "用户登录信息校验异常，请联系网站管理员";
        }

        return "用户未登录";
    }

    /**
     * 记录认证异常日志。
     *
     * @param errorMsg      错误消息字符串
     * @param authException 认证异常
     */
    private void logAuthenticationError(AuthenticationException authException) {
        log.error("authentication failed", authException);
    }
}
