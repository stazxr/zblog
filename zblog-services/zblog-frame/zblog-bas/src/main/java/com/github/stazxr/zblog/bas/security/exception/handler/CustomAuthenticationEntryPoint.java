package com.github.stazxr.zblog.bas.security.exception.handler;

import com.github.stazxr.zblog.bas.msg.Result;
import com.github.stazxr.zblog.bas.msg.util.ResponseUtils;
import com.github.stazxr.zblog.bas.security.core.TokenError;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
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
        // 提取异常信息
        String errorMsg = extractErrorMessage(authException);
        String noticeMsg = generateNoticeMessage(errorMsg);

        // 记录认证异常日志
        logAuthenticationError(errorMsg, authException);

        // 返回结果
        Result result = Result.failure(noticeMsg).data(errorMsg).code(HttpStatus.UNAUTHORIZED);
        ResponseUtils.responseJsonWriter(response, result);
    }

    /**
     * 提取异常信息，获取详细错误消息。
     *
     * @param authException 认证异常
     * @return 错误消息字符串
     */
    private String extractErrorMessage(AuthenticationException authException) {
        return authException.getMessage() != null ? authException.getMessage() : "未知认证错误";
    }

    /**
     * 根据异常信息生成用户友好的提示消息。
     *
     * @param errorMsg      错误消息字符串
     * @return 友好提示消息
     */
    private String generateNoticeMessage(String errorMsg) {
        if (errorMsg.contains(TokenError.Const.EXCEPTION)) {
            return "权限校验异常，请联系网站管理员";
        } else if (errorMsg.contains(TokenError.Const.BUSY)) {
            return "系统繁忙，请稍后再试";
        } else if (errorMsg.contains(TokenError.Const.EXPIRED)) {
            return "当前登录状态已过期，请重新登录";
        } else if (errorMsg.contains(TokenError.Const.NO_LOGIN)) {
            return "用户未登录";
        } else {
            return "用户未认证，请检查登录状态";
        }
    }

    /**
     * 记录认证异常日志。
     *
     * @param errorMsg      错误消息字符串
     * @param authException 认证异常
     */
    private void logAuthenticationError(String errorMsg, AuthenticationException authException) {
        if (errorMsg.contains(TokenError.Const.EXCEPTION)) {
            log.error("权限校验异常：{}", errorMsg, authException);
        } else {
            log.warn("认证失败：{}", errorMsg, authException);
        }
    }
}
