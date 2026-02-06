package com.github.stazxr.zblog.bas.context;

import com.github.stazxr.zblog.bas.context.constant.TagConstants;
import com.github.stazxr.zblog.bas.context.entity.ContextTag;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for managing CoreContext based on HTTP request headers.
 *
 * <p>
 * 根据请求头构建 CoreContext，并支持清理上下文。
 * 使用缓存和允许标签集合，提高请求处理性能。
 * </p>
 *
 * @author SunTao
 * @since 2024-07-02
 */
public class ContextHelper implements AutoCloseable {

    /** 缓存 header 是否允许，key: header name 小写, value: 是否允许 */
    private static final Map<String, Boolean> HEADER_CACHE = new ConcurrentHashMap<>();

    /**
     * 根据请求头创建 CoreContext。
     *
     * @param request HTTP 请求对象
     * @param allowedHeaders 小写允许 header 集合，由 Filter 初始化
     */
    public static void createContext(HttpServletRequest request, Set<String> allowedHeaders) {
        CoreContextHolder.init();
        List<ContextTag> tags = new ArrayList<>();
        if (request != null && !allowedHeaders.isEmpty()) {
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    String keyLower = headerName.toLowerCase(Locale.ROOT);

                    // 缓存判断 header 是否允许
                    Boolean allowed = HEADER_CACHE.computeIfAbsent(keyLower, allowedHeaders::contains);
                    if (allowed) {
                        String value = request.getHeader(headerName);
                        if (value != null) {
                            tags.add(new ContextTag(headerName, value));
                        }
                    }
                }
            }
        }

        // 生成 traceId
        tags.add(new ContextTag(TagConstants.TRACE_ID_TAG, String.valueOf(SequenceUtils.getId())));

        // 写入 CoreContext
        Context.putAll(tags);
    }

    /**
     * 清空当前线程的上下文。
     */
    public static void clearContext() {
        CoreContextHolder.clear();
    }

    /**
     * AutoCloseable 接口实现，清理上下文。
     */
    @Override
    public void close() {
        clearContext();
    }
}
