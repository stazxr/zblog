package com.github.stazxr.zblog.bas.exception;

import com.github.stazxr.zblog.bas.msg.Result;
import com.github.stazxr.zblog.bas.msg.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * BaseException 异常处理器
 *
 * @author SunTao
 * @since 2026-01-06
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {
    /**
     * BaseException 处理器
     *
     * @param e 异常信息
     * @throws IOException write json failed
     */
    @ExceptionHandler(value = BaseException.class)
    public void serviceExceptionHandler(HttpServletResponse response, BaseException e) throws IOException {
        log.error("全局捕获 -> 系统发生业务异常", e);
        ErrorMeta errorMeta = new ErrorMeta(e);
        if (e.getRootCause() != null) {
            // return sys exception info
            Throwable throwable = e.getRootCause();
            errorMeta.setRemark("系统异常为: ".concat(throwable.getClass().getName().concat(" - ").concat(throwable.getMessage())));
        }

        ResponseUtils.responseJsonWriter(response, Result.failure(e.getMessage()).data(errorMeta));
    }
}
