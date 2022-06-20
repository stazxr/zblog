package com.github.stazxr.zblog.base.security.exception;

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
    /**
     * ATK 过期，可续签
     */
    private static final String TOKEN_LABEL_1 = "TE001";

    /**
     * RTK 过期，不可续签
     */
    private static final String TOKEN_LABEL_4 = "TE004";

    /**
     * 令牌校验失败
     */
    private static final String TOKEN_LABEL_6 = "TE006";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        // 封装返回结果 Result
        String errorMsg = errorMsg(authException);
        ResultCode resultCode;
        if (errorMsg.contains(TOKEN_LABEL_1)) {
            resultCode = ResultCode.TOKEN_FAILED_001;
        } else if (errorMsg.contains(TOKEN_LABEL_4)) {
            resultCode = ResultCode.TOKEN_FAILED_004;
        } else if (errorMsg.contains(TOKEN_LABEL_6)) {
            resultCode = ResultCode.TOKEN_FAILED_006;
        } else {
            // no login
            resultCode = ResultCode.TOKEN_FAILED_XXX;
        }
        Result result = Result.failure(resultCode).code(HttpStatus.UNAUTHORIZED);
        ResponseUtils.responseJsonWriter(response, result);
    }

    private String errorMsg(AuthenticationException authException) {
        return authException.getMessage();
    }
}
