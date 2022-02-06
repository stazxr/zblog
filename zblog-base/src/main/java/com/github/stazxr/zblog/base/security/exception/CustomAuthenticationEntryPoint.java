package com.github.stazxr.zblog.base.security.exception;

import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.core.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CustomAuthenticationEntryPoint
 *
 * @author SunTao
 * @since 2022-01-17
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // 封装返回结果 Result
        Result result = Result.failure(ResultCode.AUTH_FAILED, errorMsg(authException)).code(HttpStatus.UNAUTHORIZED);
        ResponseUtils.responseJsonWriter(response, result);
    }

    private String errorMsg(AuthenticationException authException) {
        return authException.getMessage();
    }
}
