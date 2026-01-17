package com.github.stazxr.zblog.bas.validation;

import com.github.stazxr.zblog.bas.msg.Result;
import com.github.stazxr.zblog.bas.msg.ResultCode;
import com.github.stazxr.zblog.bas.msg.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.UnexpectedTypeException;
import java.io.IOException;
import java.util.*;

/**
 * 全局参数校验异常处理器
 *
 * @author SunTao
 * @since 2025-08-01
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidatorExceptionHandler {
    private static final String DEFAULT_LOG_PREFIX = "ValidatorExceptionHandler - ";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(HttpServletResponse response, MethodArgumentNotValidException ex) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("{} 参数校验失败: {}", DEFAULT_LOG_PREFIX, ex.getMessage());
        }

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        ResultCode validateFailed = ResultCode.PARAM_VALIDATE_FAILED;
        String errorMessage = validateFailed.message();
        if (fieldErrors.size() > 0) {
            FieldError fieldError = fieldErrors.get(0);
            errorMessage = fieldError.getDefaultMessage();
        }

        Result result = Result.failure(validateFailed.code(), errorMessage).code(HttpStatus.BAD_REQUEST);
        ResponseUtils.responseJsonWriter(response, result);
    }

    @ExceptionHandler(AssertException.class)
    public void handleAssertException(HttpServletResponse response, AssertException ex) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("{} 数据校验失败: {}", DEFAULT_LOG_PREFIX, ex.getMessage());
        }

        ResultCode validateFailed = ResultCode.DATA_VALIDATE_FAILED;
        Result result = Result.failure(validateFailed.code(), ex.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseUtils.responseJsonWriter(response, result);
    }

    @ExceptionHandler(value = {
        ServletRequestBindingException.class, TypeMismatchException.class, UnexpectedTypeException.class
    })
    public void badRequestExceptionHandler(HttpServletResponse response, Exception ex) throws IOException {
        log.error("{} 请求参数绑定失败", DEFAULT_LOG_PREFIX, ex);
        ResultCode paramBindFailed = ResultCode.REQUEST_PARAM_BIND_FAILED;
        Result result = Result.failure(paramBindFailed.code(), paramBindFailed.message()).code(HttpStatus.BAD_REQUEST);
        ResponseUtils.responseJsonWriter(response, result);
    }

//    /**
//     * 处理 @Validated 的普通参数校验失败（如 @RequestParam、@PathVariable）
//     */
//    @ExceptionHandler(ConstraintViolationException.class)
//    public void handleConstraintViolationException(HttpServletRequest request,
//                                                   HttpServletResponse response,
//                                                   ConstraintViolationException ex) throws IOException {
//        Locale locale = RequestContextUtils.getLocale(request);
//        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
//
//        List<String> errorMessages = new ArrayList<>();
//        for (ConstraintViolation<?> violation : violations) {
//            String propertyPath = extractLastNode(violation.getPropertyPath().toString());
//            String message = resolveMessage(violation.getMessage(), locale);
//            errorMessages.add(propertyPath + ": " + message);
//        }
//
//        String messageToReturn = errorMessages.isEmpty()
//                ? ResultCode.VALIDATE_FAILED.message()
//                : String.join("; ", errorMessages);
//
//        if (log.isDebugEnabled()) {
//            log.debug("{} 普通参数校验失败: {}", DEFAULT_LOG_PREFIX, ex.getMessage());
//        }
//
//        ResponseUtils.responseJsonWriter(response, Result.failure(messageToReturn).code(HttpStatus.BAD_REQUEST));
//    }
//
//    /**
//     * 获取国际化消息
//     */
//    private String resolveMessage(String rawMessage, Locale locale) {
//        return rawMessage;
//    }
//
//    /**
//     * 提取路径最后一个节点
//     */
//    private String extractLastNode(String propertyPath) {
//        if (propertyPath == null) {
//            return "";
//        }
//        int idx = propertyPath.lastIndexOf(".");
//        return idx != -1 ? propertyPath.substring(idx + 1) : propertyPath;
//    }
}
