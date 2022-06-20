package com.github.stazxr.zblog.core.exception.handler;

import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.util.ThrowableUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理器
 *
 * @author SunTao
 * @since 2021-05-19
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理自定义的业务异常
     *
     * @param e 错误信息
     * @return Result
     */
    @ExceptionHandler(value = ServiceException.class)
    public Result serviceExceptionHandler(ServiceException e) {
        log.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(e.getIdentifier(), e.getMessage());
    }

    /**
     * 处理空指针的异常
     *
     * @param e 错误信息
     * @return Result
     */
    @ExceptionHandler(value = NullPointerException.class)
    public Result nullExceptionHandler(NullPointerException e) {
        log.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(ResultCode.NULL_POINT);
    }

    /**
     * 处理参数校验错误的异常
     *
     * @param e 错误信息
     * @return Result
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.error(ThrowableUtils.getStackTrace(e));
        return Result.failure(ResultCode.PARAM_VALID);
    }

    /**
     * 请求路径不存在
     *
     * @param e 错误信息
     * @return Result
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public Result resourceNotFoundExceptionHandler(HttpServletRequest request, NoHandlerFoundException e) {
        log.error("请求路径不存在，{}", e.getMessage());
        String eorMsg = String.format("请求资源[%s]不存在", request.getRequestURL());
        return Result.failure(ResultCode.NOT_FOUND, eorMsg).code(HttpStatus.NOT_FOUND);
    }
}
