package com.github.stazxr.zblog.bas.log.advice;

import com.github.stazxr.zblog.bas.log.cache.LogPathCacheManager;
import com.github.stazxr.zblog.bas.log.context.LogTraceContext;
import com.github.stazxr.zblog.bas.order.FilterOrder;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.util.net.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求日志打印.
 *
 * @author SunTao
 * @since 2026-02-03
 */
@Component
public class ReqLogControlAdvice implements HandlerInterceptor, Ordered {
    private static final Logger log = LoggerFactory.getLogger("___flowLog___");

    private final LogPathCacheManager logManager;

    public ReqLogControlAdvice(LogPathCacheManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String path = request.getServletPath();
            if (logManager.enabledLog(path)) {
                if (!(handler instanceof HandlerMethod)) {
                    return true;
                }

                HandlerMethod method = (HandlerMethod) handler;
                Router router = method.getMethodAnnotation(Router.class);
                if (router == null) {
                    return true;
                }

                ApiVersion version = method.getMethodAnnotation(ApiVersion.class);
                String traceId = LogTraceContext.getTraceId();
                String apiCode = router.code();
                String apiVersion = version == null ? "" : version.value();
                String ipAddr = IpUtils.getIp(request);
                log.info("REQ|{}|{}|{}|{}|{}|", traceId, apiCode, apiVersion, "", ipAddr);
            }
        } catch (Exception e) {
            log.error("请求日志打印失败", e);
        }

        return true;
    }

    @Override
    public int getOrder() {
        return FilterOrder.REQ_LOG;
    }
}
