package com.github.stazxr.zblog.bas.security.hanlder;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.i18n.I18nUtils;
import com.github.stazxr.zblog.bas.rest.Result;
import com.github.stazxr.zblog.bas.rest.util.ResponseUtils;
import com.github.stazxr.zblog.bas.security.exception.resolver.AuthenticationErrorResolver;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 自定义认证失败处理器，实现 Spring Security 提供的 {@link AuthenticationFailureHandler} 接口。
 * 用于处理用户登录失败后的逻辑，包括记录失败原因、响应客户端错误信息等。
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFailureHandlerImpl.class);

    private SecurityUserService securityUserService;

    private AuthenticationErrorResolver authenticationErrorResolver;

    /**
     * 处理认证失败逻辑。
     *
     * @param request   当前 HTTP 请求
     * @param response  当前 HTTP 响应
     * @param exception 认证失败异常
     * @throws IOException 可能的 IO 异常
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException exception) throws IOException {
        // 获取尝试登录的用户名
        String username = (String) request.getAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);

        // 处理异常逻辑
        exceptionHandle(username, exception, request);

        // 生成返回结果
        Result<String> result = genResult(username, exception);

        // 写入响应
        ResponseUtils.responseJsonWriter(response, result, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 根据认证异常生成响应结果。
     *
     * @param username  登录用户名
     * @param exception 认证失败的具体异常
     * @return 封装的失败响应对象
     */
    private Result<String> genResult(String username, AuthenticationException exception) {
        log.warn("用户登录失败 | username={} | reason={}", username, exception.getMessage());
        ErrorCode errorCode = authenticationErrorResolver.resolve(exception);
        return Result.<String>failure(errorCode.getCode(), I18nUtils.getMessage(errorCode.getI18nKey())).data(username);
    }

    /**
     * 处理特定的认证失败逻辑，例如记录错误次数或限制登录。
     *
     * @param username  登录用户名
     * @param exception 认证失败异常
     * @param request   当前 HTTP 请求
     */
    protected void exceptionHandle(String username, AuthenticationException exception, HttpServletRequest request) {
        if (exception instanceof BadCredentialsException) {
            // 密码错误逻辑：记录错误次数，满足规则时锁定用户
            securityUserService.updateUserLoginInfo(username, 2, request);
        }
    }

    @Autowired
    public void setSecurityUserService(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @Autowired
    public void setAuthenticationErrorResolver(AuthenticationErrorResolver authenticationErrorResolver) {
        this.authenticationErrorResolver = authenticationErrorResolver;
    }
}
