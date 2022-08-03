package com.github.stazxr.zblog.core.exception.handler;

import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.model.ErrorMeta;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.core.util.ResponseUtils;
import com.github.stazxr.zblog.util.exception.ValidParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.unit.DataSize;
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
     * 处理自定义的业务异常（返回200响应码）
     *
     * @param e 异常信息
     */
    @ExceptionHandler(value = ServiceException.class)
    public void serviceExceptionHandler(HttpServletResponse response, ServiceException e) throws IOException {
        log.error("全局捕获 -> 系统发生业务异常", e);
        ErrorMeta errorMeta = new ErrorMeta(e);
        if (e.getThrowable() != null) {
            Throwable throwable = e.getThrowable();
            errorMeta.setRemark("系统异常为: ".concat(throwable.getClass().getName().concat(" - ").concat(throwable.getMessage())));
        }

        // 业务异常返回200
        Result result = Result.failure(e.getIdentifier(), e.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR).data(errorMeta);
        ResponseUtils.responseJsonWriter(response, result);
    }

    /**
     * 处理参数校验错误的异常
     *
     * @param e 异常信息
     */
    @ExceptionHandler(value = ValidParamException.class)
    public void validParamExceptionHandler(HttpServletResponse response, ValidParamException e) throws IOException {
        log.error("全局捕获 -> 参数校验错误", e);
        ErrorMeta errorMeta = new ErrorMeta(e);
        Result result = Result.failure(ResultCode.PARAM_VALID, e.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR).data(errorMeta);
        ResponseUtils.responseJsonWriter(response, result);
    }

    /**
     * 请求数据格式不正确（返回400响应码，参数不匹配，请求头不匹配...）
     *
     * @param e 异常信息
     */
    @ExceptionHandler(value = ServletRequestBindingException.class)
    public void badRequestExceptionHandler(HttpServletResponse response, ServletRequestBindingException e) throws IOException {
        log.error("全局捕获 -> 请求数据格式不正确", e);
        ErrorMeta errorMeta = new ErrorMeta(e);
        Result result = Result.failure(ResultCode.BAD_REQUEST).code(HttpStatus.INTERNAL_SERVER_ERROR).data(errorMeta);
        ResponseUtils.responseJsonWriter(response, result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 请求资源不存在（返回404响应码）
     *
     * @param e 异常信息
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public void resourceNotFoundExceptionHandler(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException e) throws IOException {
        log.warn("全局捕获 -> 请求资源不存在: {}", request.getRequestURL());
        ErrorMeta errorMeta = new ErrorMeta(e);
        Result result = Result.failure(ResultCode.NOT_FOUND).code(HttpStatus.NOT_FOUND).data(errorMeta);
        ResponseUtils.responseJsonWriter(response, result, HttpStatus.NOT_FOUND);
    }

    /**
     * 上传文件过大
     *
     * @param e 异常信息
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public void maxUploadSizeExceededExceptionHandler(HttpServletResponse response, MaxUploadSizeExceededException e) throws IOException {
        log.error("全局捕获 -> 文件上传失败", e);
        ErrorMeta errorMeta = new ErrorMeta(e);
        String eorMsg = ResultCode.FILE_SIZE_OVER_LIMIT.message().concat(":").concat(maxFileSize.toString());
        Result result = Result.failure(ResultCode.FILE_SIZE_OVER_LIMIT, eorMsg).code(HttpStatus.INTERNAL_SERVER_ERROR).data(errorMeta);
        ResponseUtils.responseJsonWriter(response, result);
    }

    /**
     * 处理其他异常（返回500响应码，因为发生该异常的原因一般为代码错误导致，如空指针异常，下标越界...）
     *
     * @param e 异常信息
     */
    @ExceptionHandler(value = Throwable.class)
    public void exceptionHandler(HttpServletResponse response, Throwable e) throws IOException {
        log.error("全局捕获 -> 系统发生未知错误", e);
        ErrorMeta errorMeta = new ErrorMeta(e);
        Result result = Result.failure(ResultCode.SERVER_ERROR).code(HttpStatus.INTERNAL_SERVER_ERROR).data(errorMeta);
        ResponseUtils.responseJsonWriter(response, result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
