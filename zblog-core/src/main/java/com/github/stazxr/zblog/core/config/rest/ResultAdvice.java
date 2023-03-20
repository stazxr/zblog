package com.github.stazxr.zblog.core.config.rest;

import com.github.stazxr.zblog.core.annotation.IgnoreResult;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 统一返回体包装器
 *
 * @author SunTao
 * @since 2022-02-05
 */
@Slf4j
@RestControllerAdvice
public class ResultAdvice implements ResponseBodyAdvice<Object> {
    private static final String ERROR_PATH = "/error";

    /**
     * 不需要进行统一包装的三方接口
     */
    private static final String[] NOT_PACKAGE_URI = {
        "/swagger-resources", "/v3/api-docs", "/v2/api-docs"
    };

    private final List<String> notPackageUriList = Arrays.asList(NOT_PACKAGE_URI);

    @Override
    public boolean supports(MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
        return !returnType.hasMethodAnnotation(IgnoreResult.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object beforeBodyWrite(Object o, @NotNull MethodParameter methodParameter, @NotNull MediaType mediaType,
            @NotNull Class<? extends HttpMessageConverter<?>> aClass, @NotNull ServerHttpRequest serverHttpRequest,
            @NotNull ServerHttpResponse serverHttpResponse) {
        if (o == null) {
            return Result.success();
        }

        if (Result.class.isAssignableFrom(o.getClass())) {
            return o;
        }

        final String urlParamSplit = "\\?";
        String uri = serverHttpRequest.getURI().getPath();
        if (notPackageUriList.contains(uri.split(urlParamSplit)[0])) {
            return o;
        }

        if (ERROR_PATH.equals(uri)) {
            try {
                LinkedHashMap<String, Object> val = (LinkedHashMap<String, Object>) o;
                Integer code = (Integer) val.getOrDefault("status", "500");
                return Result.failure(ResultCode.SERVER_ERROR).data(o).code(HttpStatus.valueOf(code));
            } catch (Exception e) {
                log.error("ResultAdvice.beforeBodyWrite catch eor", e);
                return Result.failure(ResultCode.SERVER_ERROR);
            }
        }

        return Result.success().data(o);
    }
}
