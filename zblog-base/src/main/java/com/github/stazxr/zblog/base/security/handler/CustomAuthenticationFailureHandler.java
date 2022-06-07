package com.github.stazxr.zblog.base.security.handler;

import com.github.stazxr.zblog.base.cache.UserRoleCache;
import com.github.stazxr.zblog.base.security.exception.NumCodeException;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.core.util.ResponseUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

/**
 * CustomAuthenticationFailureHandler
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Slf4j
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        // 清除用的缓存信息
        String username = (String) request.getAttribute(SPRING_SECURITY_FORM_USERNAME_KEY);
        UserRoleCache.remove(username);

        // 处理异常
        exceptionHandle(exception, request);

        // 封装返回结果 Result
        Result result = genResult(username, exception);
        ResponseUtils.responseJsonWriter(response, result);
    }

    private Result genResult(String username, AuthenticationException exception) {
        log.warn("user [{}] login failed: [{}]", username, exception.getMessage());
        if (exception instanceof CredentialsExpiredException) {
            return Result.failure(ResultCode.PASSWORD_EXPIRED, errorMsg(exception)).data(username).code(HttpStatus.UNAUTHORIZED);
        }

        return Result.failure(ResultCode.LOGIN_FAILED, errorMsg(exception)).code(HttpStatus.UNAUTHORIZED);
    }

    private void exceptionHandle(AuthenticationException exception, HttpServletRequest request) {
        String username = (String) request.getAttribute(SPRING_SECURITY_FORM_USERNAME_KEY);
        if (exception instanceof BadCredentialsException) {
            // 密码输入错误，记录用户密码输入错误次数，满足条件则锁定用户
            log.warn("user [{}] input wrong password", username);
        }

        if (exception instanceof UsernameNotFoundException) {
            // 用户不存在，记录IP对网站的访问次数，满足条件则拉黑IP
            String ipAddr = IpUtils.getIpAddr(request);
            log.warn("ip [{}] input wrong username [{}]", ipAddr, username);
        }
    }

    private String errorMsg(AuthenticationException e) {
        if (e instanceof NumCodeException) {
            return e.getMessage();
        } else if (e instanceof UsernameNotFoundException) {
            return "用户不存在";
        } else if (e instanceof LockedException) {
            return "用户被锁定";
        }  else if (e instanceof DisabledException) {
            return "用户被禁用";
        }  else if (e instanceof AccountExpiredException) {
            return "用户已过期";
        } else if (e instanceof BadCredentialsException) {
            return "密码错误";
        } else if (e instanceof CredentialsExpiredException) {
            return "密码已过期";
        } else {
            return "登录失败";
        }
    }
}
