package com.github.stazxr.zblog.bas.validation;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.order.FilterOrder;
import com.github.stazxr.zblog.bas.rest.Result;
import com.github.stazxr.zblog.bas.rest.ResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;

/**
 * 全局参数校验异常处理器
 *
 * @author SunTao
 * @since 2025-08-01
 */
@RestControllerAdvice
public class ValidatorExceptionHandler implements Ordered {
    private static final Logger log = LoggerFactory.getLogger(ValidatorExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorCode errorCode = ValidationErrorCode.EVALIA000;
        String message = ex.getBindingResult().getFieldErrors().stream().findFirst().map(FieldError::getDefaultMessage).orElse(errorCode.getMessage());
        log.debug("参数校验失败: {}", message);
        return failure(errorCode.getCode(), message);
    }

    @ExceptionHandler(AssertException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleAssertException(AssertException ex) {
        log.debug("数据校验失败: {}", ex.getMessage());
        return failure(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(value = {
        BindException.class,
        TypeMismatchException.class,
        UnexpectedTypeException.class,
        ConstraintViolationException.class,
        ServletRequestBindingException.class,
        MissingServletRequestParameterException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> badRequestExceptionHandler(Exception ex) {
        log.warn("请求参数格式错误: {}", ex.getMessage());
        ErrorCode errorCode = ValidationErrorCode.EVALIA002;
        return failure(errorCode.getCode(), errorCode.getMessage());
    }

    private Result<Void> failure(String errorCode, String errorMessage) {
        return Result.<Void>failure(errorCode, errorMessage).type(ResultType.PARAM_FAILED);
    }

    @Override
    public int getOrder() {
        return FilterOrder.VALIDATION_EXP_ADVICE;
    }
}
