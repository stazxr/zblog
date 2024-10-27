package com.github.stazxr.zblog.bas.log.filter;

import com.github.stazxr.zblog.bas.log.cache.LogPathCacheManager;
import com.github.stazxr.zblog.bas.log.contants.LogConstants;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to manage MDC (Mapped Diagnostic Context) for logging purposes based on the servlet path.
 *
 * <p>This filter is executed once per request and sets a specific MDC key ({@link LogConstants#LOG_MDC_PATH_KEY})
 * with the servlet path of the incoming request if logging is enabled for that path according to
 * {@link LogPathCacheManager}. It ensures proper MDC management throughout the request lifecycle.</p>
 *
 * <p>The MDC value is removed from MDC context after processing the request.</p>
 *
 * @author SunTao
 * @since 2024-07-07
 */
@Order(0)
@Component
public class LogMdcPrintFilter extends OncePerRequestFilter {
    private LogPathCacheManager logPathCacheManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Initialize MDC with an empty string for safety
            MDC.put(LogConstants.LOG_MDC_PATH_KEY, "");

            String servletPath = request.getServletPath();
            if (logPathCacheManager.enabledLog(servletPath)) {
                MDC.put(LogConstants.LOG_MDC_PATH_KEY, servletPath);
            }

            // Proceed with the filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Clean up: remove the MDC value after processing the request
            MDC.remove(LogConstants.LOG_MDC_PATH_KEY);
        }
    }

    /**
     * Sets the LogPathCacheManager instance via dependency injection.
     *
     * @param logPathCacheManager The LogPathCacheManager instance to be injected.
     */
    @Autowired
    public void setLogControlPathCacheManager(LogPathCacheManager logPathCacheManager) {
        this.logPathCacheManager = logPathCacheManager;
    }
}
