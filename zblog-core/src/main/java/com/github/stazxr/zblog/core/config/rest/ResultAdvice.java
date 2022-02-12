package com.github.stazxr.zblog.core.config.rest;

import com.github.stazxr.zblog.core.annotation.IgnoreResult;
import com.github.stazxr.zblog.core.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedHashMap;

/**
 * 统一返回体包装器
 *
 * @author SunTao
 * @since 2022-02-05
 */
@Slf4j
@RestControllerAdvice
public class ResultAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        return !returnType.hasMethodAnnotation(IgnoreResult.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse) {
        if (o == null) {
            return Result.success();
        }

        if (Result.class.isAssignableFrom(o.getClass())) {
            return o;
        }

        String uri = serverHttpRequest.getURI().getPath();
        if ("/error".equals(uri)) {
            try {
                LinkedHashMap<String, Object> val = (LinkedHashMap<String, Object>) o;
                Integer code = (Integer) val.getOrDefault("status", "500");
                return Result.failure("系统错误").data(o).code(HttpStatus.valueOf(code));
            } catch (Exception e) {
                log.error("ResultAdvice.beforeBodyWrite catch eor", e);
                return Result.failure("系统错误");
            }
        }

        return Result.success().data(o);
    }
}
