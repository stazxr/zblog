package com.github.stazxr.zblog.base.component.security.exception;

import com.github.stazxr.zblog.base.component.security.jwt.TokenError;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.core.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

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
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 封装返回结果 Result
        String errorMsg = errorMsg(authException);
        ResultCode resultCode;
        if (errorMsg.contains(TokenError.Const.EXCEPTION)) {
            log.error("authentication catch an error", authException);
            resultCode = ResultCode.TOKEN_FAILED_004;
        } else if (errorMsg.contains(TokenError.Const.BUSY)) {
            resultCode = ResultCode.TOKEN_FAILED_003;
        } else if (errorMsg.contains(TokenError.Const.EXPIRED)) {
            resultCode = ResultCode.TOKEN_FAILED_002;
        } else if (errorMsg.contains(TokenError.Const.NO_LOGIN)) {
            resultCode = ResultCode.TOKEN_FAILED_001;
        } else {
            log.error("authentication catch an exception", authException);
            resultCode = ResultCode.TOKEN_FAILED_001;
        }

        Result result = Result.failure(resultCode).data(errorMsg).code(HttpStatus.UNAUTHORIZED);
        ResponseUtils.responseJsonWriter(response, result);
    }

    private String errorMsg(AuthenticationException authException) {
        return authException.getMessage();
    }
}
