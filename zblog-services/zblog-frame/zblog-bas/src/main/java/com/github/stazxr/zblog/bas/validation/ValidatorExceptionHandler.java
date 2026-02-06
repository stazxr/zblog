package com.github.stazxr.zblog.bas.validation;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.order.FilterOrder;
import com.github.stazxr.zblog.bas.rest.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;
import java.util.List;

/**
 * 全局参数校验异常处理器
 *
 * <p>
 * “用户能改的 = 业务异常，用户改不了的 = 技术异常”
 *
 * @author SunTao
 * @since 2025-08-01
 */
@RestControllerAdvice
public class ValidatorExceptionHandler implements Ordered {
    private static final Logger log = LoggerFactory.getLogger(ValidatorExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        if (log.isDebugEnabled()) {
            log.debug("参数校验失败: {}", ex.getMessage());
        }

        ErrorCode errorCode = ValidationErrorCode.EVALIA000;
        String errorMessage = errorCode.getI18nKey();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (fieldErrors.size() > 0) {
            FieldError fieldError = fieldErrors.get(0);
            errorMessage = fieldError.getDefaultMessage();
        }

        // return
        Result<Void> result = Result.failure(errorCode.getCode(), errorMessage);
        result.code(HttpStatus.BAD_REQUEST.value());
        return result;
    }

    @ExceptionHandler(AssertException.class)
    public Result<Void> handleAssertException(AssertException ex) {
        if (log.isDebugEnabled()) {
            log.debug("数据校验失败: {}", ex.getMessage());
        }

        // return
        return Result.failure(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(value = {
        ServletRequestBindingException.class,
        TypeMismatchException.class,
        UnexpectedTypeException.class
    })
    public Result<Void> badRequestExceptionHandler(Exception ex) {
        log.error("参数错误", ex);
        ErrorCode errorCode = ValidationErrorCode.EVALIA002;
        return Result.failure(errorCode.getCode(), errorCode.getMessage());
    }

    @Override
    public int getOrder() {
        return FilterOrder.VALIDATION_EXP_ADVICE;
    }
}
