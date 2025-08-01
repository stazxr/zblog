package com.github.stazxr.zblog.bas.validate;

import com.github.stazxr.zblog.bas.msg.Result;
import com.github.stazxr.zblog.bas.msg.ResultCode;
import com.github.stazxr.zblog.bas.msg.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author SunTao
 * @since 2025-08-01
 */
@Slf4j
@RestControllerAdvice
public class ValidatorExceptionHandler {
    private static final String DEFAULT_LOG_PREFIX = "ValidatorExceptionHandler - ";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(HttpServletResponse response, MethodArgumentNotValidException e) throws IOException {
        String firstMessage = ResultCode.VALIDATE_FAILED.message();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            FieldError fieldError = fieldErrors.get(0);
            firstMessage = fieldError.getDefaultMessage();
        }

        log.error("{} 数据校验失败: {}", DEFAULT_LOG_PREFIX, e.getMessage());
        ResponseUtils.responseJsonWriter(response, Result.failure(firstMessage).code(HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, Object> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("message", ex.getMessage());
        return result;
    }
}