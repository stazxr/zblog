package com.github.stazxr.zblog.base.component.security.exception;

import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.core.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义 AccessDeniedHandler，处理 AccessDeniedException 异常
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 封装返回结果 Result
        Result result = Result.failure(ResultCode.NO_PERMISSION, accessDeniedException.getMessage()).code(HttpStatus.FORBIDDEN);
        ResponseUtils.responseJsonWriter(response, result);
    }
}
