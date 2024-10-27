package com.github.stazxr.zblog.bas.log.advice;

import com.github.stazxr.zblog.bas.log.cache.LogPathCacheManager;
import com.github.stazxr.zblog.bas.log.context.LogControlSerNoContext;
import com.github.stazxr.zblog.bas.log.properties.LogControlProperties;
import com.github.stazxr.zblog.bas.mask.MaskUtil;
import com.github.stazxr.zblog.util.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;

/**
 * Request body advice for controlling and logging incoming requests.
 * This advice handles masking sensitive data based on configured paths.
 *
 * @author SunTao
 * @since 2024-05-16
 */
@Slf4j
@RestControllerAdvice
public class ReqLogControlAdvice implements RequestBodyAdvice, Ordered {
    private HttpServletRequest httpServletRequest;

    private LogControlProperties logControlProperties;

    private LogPathCacheManager logPathCacheManager;

    /**
     * Checks if the advice supports processing based on configuration.
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        return logControlProperties.isEnabled();
    }

    /**
     * Executes actions before reading the request body, setting a unique serial number context.
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
            Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            LogControlSerNoContext.set(UuidUtils.gen16BitUuidStr());
        } catch (Exception e) {
            log.error("Error set control logging seral no", e);
        }
        return inputMessage;
    }

    /**
     * Logs the masked request body after it has been read.
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
            Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            if (logPathCacheManager.enabledLog(httpServletRequest.getServletPath())) {
                String className = parameter.getContainingClass().getSimpleName();
                String methodName = parameter.getMethod() == null ? "" : parameter.getMethod().getName();
                String serNo = LogControlSerNoContext.exist() ? "[" + LogControlSerNoContext.get() + "]" : "";
                log.info("{}.{}{} request param is: {}", className, methodName, serNo, MaskUtil.toMaskString(body));
            }
        } catch (Exception e) {
            log.error("Error logging request parameters", e);
        }

        return body;
    }

    /**
     * Handles cases where the request body is empty.
     */
    @Override
    public Object handleEmptyBody(@Nullable Object body, HttpInputMessage inputMessage,
            MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * Sets the HttpServletRequest instance via dependency injection.
     */
    @Autowired
    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * Sets the LogControlProperties instance via dependency injection.
     */
    @Autowired
    public void setLogControlProperties(LogControlProperties logControlProperties) {
        this.logControlProperties = logControlProperties;
    }

    /**
     * Sets the LogControlPathCacheManager instance via dependency injection.
     */
    @Autowired
    public void setLogControlPathCacheManager(LogPathCacheManager logPathCacheManager) {
        this.logPathCacheManager = logPathCacheManager;
    }

    /**
     * Specifies the order in which this advice is applied.
     */
    @Override
    public int getOrder() {
        return 999;
    }
}
