package com.github.stazxr.zblog.bas.log.advice;

import com.github.stazxr.zblog.bas.log.cache.LogPathCacheManager;
import com.github.stazxr.zblog.bas.log.context.LogTraceContext;
import com.github.stazxr.zblog.bas.log.util.LogUtil;
import com.github.stazxr.zblog.bas.order.FilterOrder;
import com.github.stazxr.zblog.bas.rest.Result;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.util.net.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 响应日志打印.
 *
 * @author SunTao
 * @since 2024-05-16
 */
@RestControllerAdvice
public class ResLogControlAdvice implements ResponseBodyAdvice<Object>, Ordered {
    private final Logger log = LoggerFactory.getLogger("___flowLog___");

    private final HttpServletRequest request;

    private final LogPathCacheManager logManager;

    public ResLogControlAdvice(HttpServletRequest request, LogPathCacheManager logManager) {
        this.request = request;
        this.logManager = logManager;
    }

    @Override
    public boolean supports(MethodParameter parameter, Class converterType) {
        return parameter.hasMethodAnnotation(Router.class);
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType,
            Class selectedConverterType, ServerHttpRequest req, ServerHttpResponse res) {
        try {
            String path = request.getServletPath();
            if (logManager.enabledLog(path)) {
                String traceId = LogTraceContext.getTraceId();
                String apiCode = LogUtil.getApiCode(returnType);
                String apiVersion = LogUtil.getApiVersion(returnType);
                String ipAddr = IpUtils.getIp(request);
                long costTime = System.currentTimeMillis() - LogTraceContext.getStartTime();
                String success = "SUCCESS";
                String errorCode = "";
                if (body instanceof Result) {
                    Result<?> result = (Result<?>) body;
                    success = result.isSuccess() ? "SUCCESS" : "FAIL";
                    errorCode = result.getErrorCode() == null ? "" : result.getErrorCode();
                }

                if ("SUCCESS".equals(success)) {
                    log.info("RES|{}|{}|{}|{}|{}|{}|{}|{}|", traceId, apiCode, apiVersion, "", ipAddr, success, errorCode, costTime);
                } else {
                    log.error("RES|{}|{}|{}|{}|{}|{}|{}|{}|", traceId, apiCode, apiVersion, "", ipAddr, success, errorCode, costTime);
                }
            }
        } catch (Exception e) {
            log.error("响应日志打印失败", e);
        }

        return body;
    }

    @Override
    public int getOrder() {
        return FilterOrder.RES_LOG;
    }
}
