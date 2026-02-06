package com.github.stazxr.zblog.bas.log.filter;

import com.github.stazxr.zblog.bas.context.Context;
import com.github.stazxr.zblog.bas.log.context.LogTraceContext;
import com.github.stazxr.zblog.bas.log.mdc.LogMdcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 日志上下文过滤器
 *
 * @author SunTao
 * @since 2026-02-04
 */
public class LogContextFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(LogContextFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            initContext();
            filterChain.doFilter(request, response);
        } finally {
            clearContext();
        }
    }

    /**
     * 初始化日志上下文
     */
    private void initContext() {
        try {
            // 初始化MDC
            LogMdcUtil.initMdc();

            // traceId
            String traceId = Context.getTraceId();
            LogTraceContext.setTraceId(traceId);
            LogTraceContext.setStartTime(System.currentTimeMillis());
            MDC.put("traceId", traceId);
        } catch (Exception e) {
            log.error("初始化日志上下文失败", e);
        }
    }

    /**
     * 清理上下文
     */
    private void clearContext() {
        try {
            MDC.clear();
            LogTraceContext.clear();
        } catch (Exception e) {
            log.error("清理日志上下文失败", e);
        }
    }
}
