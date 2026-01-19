package com.github.stazxr.zblog.bas.security.hanlder;

import com.github.stazxr.zblog.bas.msg.Result;
import com.github.stazxr.zblog.bas.msg.util.ResponseUtils;
import com.github.stazxr.zblog.bas.security.exception.LoginNumCodeException;
import com.github.stazxr.zblog.bas.security.service.SecurityUserService;
import com.github.stazxr.zblog.util.net.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
@Slf4j
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    private SecurityUserService securityUserService;

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
        Result result = genResult(username, exception);

        // 写入响应
        ResponseUtils.responseJsonWriter(response, result);
    }

    /**
     * 根据认证异常生成响应结果。
     *
     * @param username  登录用户名
     * @param exception 认证失败的具体异常
     * @return 封装的失败响应对象
     */
    private Result genResult(String username, AuthenticationException exception) {
        log.error("用户 [{}] 登录失败，异常信息：{}", username, exception.getMessage(), exception);

        if (exception instanceof CredentialsExpiredException) {
            return Result.failure(10002, errorMsg(exception)).data(username).code(HttpStatus.UNAUTHORIZED);
        }

        return Result.failure(10001, errorMsg(exception)).code(HttpStatus.UNAUTHORIZED);
    }

    /**
     * 处理特定的认证失败逻辑，例如记录错误次数或限制登录。
     *
     * @param username  登录用户名
     * @param exception 认证失败异常
     * @param request   当前 HTTP 请求
     */
    private void exceptionHandle(String username, AuthenticationException exception, HttpServletRequest request) {
        String ipAddress = IpUtils.getIp(request);
        if (exception instanceof BadCredentialsException) {
            // 密码错误逻辑：记录错误次数，满足规则时锁定用户
            log.warn("用户 [{}] 输入错误的密码", username);
            securityUserService.updateUserLoginInfo(username, ipAddress, 2, request);
        }

        if (exception instanceof UsernameNotFoundException) {
            // 用户不存在逻辑：记录IP访问次数，满足规则时拉黑IP
            log.warn("IP [{}] 使用不存在的用户名 [{}] 尝试登录", ipAddress, username);
            securityUserService.updateUserLoginInfo(username, ipAddress, 3, request);
        }
    }

    /**
     * 根据异常类型返回对应的错误提示信息。
     *
     * @param exception 认证失败异常
     * @return 错误提示信息
     */
    private String errorMsg(AuthenticationException exception) {
        if (exception instanceof LoginNumCodeException) {
            return exception.getMessage();
        } else if (exception instanceof UsernameNotFoundException) {
            return "用户不存在";
        } else if (exception instanceof LockedException) {
            return "用户已锁定";
        } else if (exception instanceof DisabledException) {
            return "用户已禁用";
        } else if (exception instanceof AccountExpiredException) {
            return "用户已过期";
        } else if (exception instanceof BadCredentialsException) {
            return "密码错误";
        } else if (exception instanceof CredentialsExpiredException) {
            return "密码已过期，请修改密码";
        } else {
            return exception.getMessage();
        }
    }

    @Autowired
    public void setSecurityUserService(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }
}

