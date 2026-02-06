package com.github.stazxr.zblog.bas.context.filter;

import com.github.stazxr.zblog.bas.context.Context;
import com.github.stazxr.zblog.bas.context.ContextHelper;
import com.github.stazxr.zblog.bas.context.autoconfigure.properties.ContextProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servlet filter that manages application-specific context.
 *
 * <p>
 * 1. 创建线程上下文 CoreContext 并根据请求 header 填充上下文变量。
 * 2. 请求处理完成后自动清理上下文。
 * 3. OPTIONS 请求直接跳过。
 * </p>
 *
 * @author SunTao
 * @since 2024-07-02
 */
public class ContextFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(ContextFilter.class);

    /** 配置的上下文属性 */
    private final ContextProperties contextProperties;

    /** 允许的 header 名称集合（小写） */
    private final Set<String> allowedHeaderSet = new HashSet<>();

    public ContextFilter(ContextProperties contextProperties) {
        this.contextProperties = contextProperties;
        initAllowedHeaders();
    }

    /**
     * 核心过滤方法。
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param filterChain FilterChain
     * @throws ServletException Servlet 异常
     * @throws IOException IO 异常
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            ContextHelper.createContext(request, allowedHeaderSet);
            Context.print();
            filterChain.doFilter(request, response);
        } finally {
            ContextHelper.clearContext();
        }
    }

    private void initAllowedHeaders() {
        if (contextProperties != null && contextProperties.getTagNames() != null) {
            allowedHeaderSet.addAll(contextProperties.getTagNames()
                    .stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet()));
            log.info("ContextFilter allowed headers initialized: {}", allowedHeaderSet);
        }
    }
}
