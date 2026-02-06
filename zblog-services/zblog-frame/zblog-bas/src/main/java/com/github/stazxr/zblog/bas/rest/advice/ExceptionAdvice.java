package com.github.stazxr.zblog.bas.rest.advice;

import com.github.stazxr.zblog.bas.exception.BaseException;
import com.github.stazxr.zblog.bas.exception.ServiceException;
import com.github.stazxr.zblog.bas.exception.SystemException;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.exception.support.MessageSupportLoader;
import com.github.stazxr.zblog.bas.order.FilterOrder;
import com.github.stazxr.zblog.bas.rest.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * <p>统一捕获系统中抛出的异常，并返回统一 {@link Result} 格式。</p>
 */
@RestControllerAdvice
public class ExceptionAdvice implements Ordered {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public Result<Void> handleService(ServiceException e) {
        return Result.failure(e.getCode(), e.getMessage());
    }

    /**
     * 技术异常
     */
    @ExceptionHandler(SystemException.class)
    public Result<Void> handleSystem(SystemException e) {
        logger.error("SystemException occurred", e);
        String errorCode = ErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
        String errorMessage = e.getMessage();
        if (e.hasErrorCode()) {
            errorCode = e.getCode();
        }
        return Result.failure(errorCode, errorMessage);
    }

    /**
     * BaseException 及其他子类兜底
     */
    @ExceptionHandler(BaseException.class)
    public Result<Void> handleBase(BaseException e) {
        // 这里处理未明确分类的 BaseException
        if (e instanceof ServiceException) {
            return handleService((ServiceException) e);
        } else if (e instanceof SystemException) {
            return handleSystem((SystemException) e);
        } else {
            logger.error("Unknown BaseException occurred", e);
            String errorCode = ErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
            String errorMessage = e.getMessage();
            if (e.hasErrorCode()) {
                errorCode = e.getCode();
            }
            return Result.failure(errorCode, errorMessage);
        }
    }

    /**
     * 异常兜底
     */
    @ExceptionHandler(Throwable.class)
    public Result<Void> handleThrowable(Throwable e) {
        logger.error("Unhandled exception occurred", e);
        String errorCode = ErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
        String errorMessage = MessageSupportLoader.getMessage("error.system.unknown");
        return Result.failure(errorCode, errorMessage);
    }

    @Override
    public int getOrder() {
        return FilterOrder.GLOBAL_EXP_ADVICE;
    }
}
