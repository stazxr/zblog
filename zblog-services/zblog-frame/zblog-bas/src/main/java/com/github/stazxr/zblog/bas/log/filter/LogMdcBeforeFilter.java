package com.github.stazxr.zblog.bas.log.filter;

import com.github.stazxr.zblog.bas.log.mdc.LogMdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to initialize MDC (Mapped Diagnostic Context) for logging before processing each request.
 * This filter ensures that MDC is properly initialized for logging contextually relevant information.
 *
 * @author SunTao
 * @since 2024-05-20
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogMdcBeforeFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            LogMdcUtil.initMdc();
        } catch (Exception e) {
            log.error("Error initializing MDC", e);
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
