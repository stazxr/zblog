package com.github.stazxr.zblog.core.exception.handler;

import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.DataValidatedException;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.model.ErrorMeta;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.core.util.ResponseUtils;
import com.github.stazxr.zblog.util.exception.AssertionViolatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.util.unit.DataSize;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 异常处理器
 *  业务异常：DataValidatedException => ServiceException
 *  规则校验失败：AssertionViolatedException
 *  上传文件过大：MaxUploadSizeExceededException
 *  请求错误异常：HttpRequestMethodNotSupportedException => ServletRequestBindingException(500)
 *  资源不存在：NoHandlerFoundException(404)
 *  其他异常：Throwable(500)
 *
 * @author SunTao
 * @since 2021-05-19
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @Value("${spring.servlet.multipart.max-file-size:1MB}")
    private DataSize maxFileSize;

    /**
     * 实体校验异常
     *
     * @param e 异常信息
     * @throws IOException write json failed
     */
    @ExceptionHandler(value = DataValidatedException.class)
    public void dataValidatedExceptionHandler(HttpServletResponse response, DataValidatedException e) throws IOException {
        log.warn("全局捕获 -> 数据校验失败: {}", e.getMessage());
        ResponseUtils.responseJsonWriter(response, Result.failure(e.getIdentifier(), e.getMessage()));
    }

    /**
     * 业务异常
     *
     * @param e 异常信息
     * @throws IOException write json failed
     */
    @ExceptionHandler(value = ServiceException.class)
    public void serviceExceptionHandler(HttpServletResponse response, ServiceException e) throws IOException {
        log.error("全局捕获 -> 系统发生业务异常", e);
        ErrorMeta errorMeta = new ErrorMeta(e);
        if (e.getThrowable() != null) {
            // return sys exception info
            Throwable throwable = e.getThrowable();
            errorMeta.setRemark("系统异常为: ".concat(throwable.getClass().getName().concat(" - ").concat(throwable.getMessage())));
        }

        ResponseUtils.responseJsonWriter(response, Result.failure(e.getIdentifier(), e.getMessage()).data(errorMeta));
    }

    /**
     * 规则校验不通过异常
     *
     * @param e 异常信息
     * @throws IOException write json failed
     */
    @ExceptionHandler(value = AssertionViolatedException.class)
    public void assertionViolatedExceptionHandler(HttpServletResponse response, AssertionViolatedException e) throws IOException {
        log.error("全局捕获 -> 规则校验失败", e);
        ResponseUtils.responseJsonWriter(response, Result.failure(e.getMessage()));
    }

    /**
     * 上传文件过大
     *
     * @param e 异常信息
     * @throws IOException write json failed
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public void maxUploadSizeExceededExceptionHandler(HttpServletResponse response, MaxUploadSizeExceededException e) throws IOException {
        log.error("全局捕获 -> 文件上传失败", e);
        String eorMsg = ResultCode.FILE_SIZE_OVER_LIMIT.message().concat(":").concat(maxFileSize.toString());
        ResponseUtils.responseJsonWriter(response, Result.failure(ResultCode.FILE_SIZE_OVER_LIMIT, eorMsg));
    }

    /**
     * 请求方式错误
     *
     * @param e 异常信息
     * @throws IOException write json failed
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public void methodNotSupportedExceptionHandler(HttpServletResponse response, HttpRequestMethodNotSupportedException e) throws IOException {
        log.error("全局捕获 -> 请求方式错误", e);
        ErrorMeta errorMeta = new ErrorMeta(e);
        Result result = Result.failure(ResultCode.REQUEST_METHOD_NOT_SUPPORT, "请求方式不正确：".concat(e.getMessage())).data(errorMeta);
        ResponseUtils.responseJsonWriter(response, result);
    }

    /**
     * 请求数据格式不正确
     *
     * @param e 异常信息
     * @throws IOException write json failed
     */
    @ExceptionHandler(value = ServletRequestBindingException.class)
    public void badRequestExceptionHandler(HttpServletResponse response, ServletRequestBindingException e) throws IOException {
        log.error("全局捕获 -> 请求数据格式不正确", e);
        ErrorMeta errorMeta = new ErrorMeta(e);
        Result result = Result.failure(ResultCode.BAD_REQUEST).code(HttpStatus.INTERNAL_SERVER_ERROR).data(errorMeta);
        ResponseUtils.responseJsonWriter(response, result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 请求资源不存在
     *
     * @param e 异常信息
     * @throws IOException write json failed
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public void resourceNotFoundExceptionHandler(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException e) throws IOException {
        log.warn("全局捕获 -> 请求资源不存在: {}", request.getRequestURL());
        ErrorMeta errorMeta = new ErrorMeta(e);
        Result result = Result.failure(ResultCode.NOT_FOUND).code(HttpStatus.NOT_FOUND).data(errorMeta);
        ResponseUtils.responseJsonWriter(response, result, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理其他异常（返回500响应码，因为发生该异常的原因一般为代码错误导致，如空指针异常，下标越界...）
     *
     * @param e 异常信息
     */
    @ExceptionHandler(value = Throwable.class)
    public void exceptionHandler(HttpServletResponse response, Throwable e) throws IOException {
        ErrorMeta errorMeta = new ErrorMeta(e);
        ResultCode resultCode = ResultCode.SERVER_ERROR;
        if (e instanceof DuplicateKeyException) {
            resultCode = ResultCode.DATA_EXIST;
            log.error("全局捕获 -> 系统发生未知错误[数据已存在]", e);
        } else {
            log.error("全局捕获 -> 系统发生未知错误", e);
        }

        Result result = Result.failure(resultCode).code(HttpStatus.INTERNAL_SERVER_ERROR).data(errorMeta);
        ResponseUtils.responseJsonWriter(response, result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
