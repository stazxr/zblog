package com.github.stazxr.zblog.bas.rest.advice;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.exception.support.MessageSupportLoader;
import com.github.stazxr.zblog.bas.order.FilterOrder;
import com.github.stazxr.zblog.bas.rest.IgnoreResult;
import com.github.stazxr.zblog.bas.rest.Result;
import com.github.stazxr.zblog.bas.rest.autoconfigure.properties.RestProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 全局统一返回体
 *
 * @author SunTao
 * @since 2026-01-25
 */
@RestControllerAdvice
public class ResultAdvice implements ResponseBodyAdvice<Object>, Ordered {
    private static final Logger log = LoggerFactory.getLogger(ResultAdvice.class);

    private final String errorPath;

    /**
     * 不需要进行统一包装的接口列表
     */
    private final List<String> notPackageUriList;

    public ResultAdvice(RestProperties properties) {
        this.errorPath = properties.getErrorPath();
        List<String> notPackageUris = properties.getNotPackageUris();
        this.notPackageUriList = notPackageUris == null ? Collections.emptyList() : notPackageUris;
    }

    @Override
    public boolean supports(MethodParameter returnType, @Nullable Class<? extends HttpMessageConverter<?>> clazz) {
        // 如果方法上有 @IgnoreResult 注解，不包装
        return !returnType.hasMethodAnnotation(IgnoreResult.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object beforeBodyWrite(Object data, @Nullable MethodParameter parameter,
            @Nullable MediaType mediaType, @Nullable Class<? extends HttpMessageConverter<?>> clazz,
            ServerHttpRequest request, @Nullable ServerHttpResponse response) {
        final String urlParamSplit = "\\?";
        String uri = request.getURI().getPath().split(urlParamSplit)[0];
        if (notPackageUriList.contains(uri)) {
            return data;
        }

        if (data == null) {
            return Result.success();
        }

        if (Result.class.isAssignableFrom(data.getClass())) {
            return data;
        }

        if (errorPath.equals(uri)) {
            Integer statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            try {
                LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) data;
                statusCode = (Integer) map.getOrDefault("status", "500");
            } catch (Exception e) {
                log.error("ResultAdvice.beforeBodyWrite catch eor", e);
            }

            // 返回默认错误信息
            String errorCode = ErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
            String errorMessage = MessageSupportLoader.getMessage("error.system.unknown");
            return Result.failure(errorCode, errorMessage).code(statusCode).data(data);
        }

        return Result.success().data(data);
    }

    @Override
    public int getOrder() {
        return FilterOrder.REST_ADVICE;
    }
}
