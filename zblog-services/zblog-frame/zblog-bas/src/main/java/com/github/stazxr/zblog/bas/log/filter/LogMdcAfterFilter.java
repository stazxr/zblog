package com.github.stazxr.zblog.bas.log.filter;

import com.github.stazxr.zblog.bas.log.mdc.LogMdcUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to clear MDC (Mapped Diagnostic Context) for logging after processing each request.
 * This filter ensures that MDC is properly cleared after request processing to avoid
 * potential memory leaks or context pollution.
 *
 * @author SunTao
 * @since 2024-05-20
 */
@Component
public class LogMdcAfterFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            // Continue with the filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Clear MDC after processing the request
            LogMdcUtil.clearMdc();
        }
    }
}
