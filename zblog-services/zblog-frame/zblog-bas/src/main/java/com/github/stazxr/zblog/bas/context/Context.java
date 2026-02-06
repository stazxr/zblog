package com.github.stazxr.zblog.bas.context;

import com.github.stazxr.zblog.bas.context.constant.TagConstants;
import com.github.stazxr.zblog.bas.context.entity.ContextTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Context 工具类，用于对外暴露 CoreContext 操作接口。
 *
 * <p>
 * 提供静态方法获取、设置、打印上下文信息。
 * 核心上下文 CoreContext 基于线程本地 ThreadLocal 保存，保证线程安全。
 * </p>
 *
 * @author SunTao
 * @since 2024-07-02
 */
public class Context {
    private static final Logger log = LoggerFactory.getLogger(Context.class);

    /**
     * 获取指定 key 的上下文值。
     *
     * @param key 上下文 key
     * @return 上下文值，如果不存在返回 null
     */
    public static String get(String key) {
        return getContext().get(key);
    }

    /**
     * 获取 loginId
     *
     * @return loginId, 如果不存在返回 null
     */
    public static String getLoginId() {
        return getContext().get(TagConstants.LOGIN_ID_TAG);
    }

    /**
     * 获取 traceId
     *
     * @return traceId, 如果不存在返回 null
     */
    public static String getTraceId() {
        return getContext().get(TagConstants.TRACE_ID_TAG);
    }

    /**
     * 添加单个上下文标签。
     *
     * @param tag 上下文标签
     */
    public static void put(ContextTag tag) {
        CoreContext context = getContext();
        if (tag != null) {
            context.put(tag);
        }
    }

    /**
     * 添加多个上下文标签。
     *
     * @param tags 上下文标签列表
     */
    public static void putAll(List<ContextTag> tags) {
        CoreContext context = getContext();
        if (tags != null && !tags.isEmpty()) {
            context.putAll(tags);
        }
    }

    /**
     * 获取当前线程的 CoreContext 实例。
     *
     * @return CoreContext 实例
     */
    private static CoreContext getContext() {
        return CoreContextHolder.get();
    }

    /**
     * 打印当前上下文内容到日志（仅在 debug 模式）。
     */
    public static void print() {
        if (log.isDebugEnabled()) {
            String contextMap = getContext().getContextMapAsString();
            log.debug("Current thread CoreContext: {}", contextMap);
        }
    }

    /**
     * 获取完整上下文 Map（不可修改副本）。
     *
     * @return 上下文 Map 的只读副本
     */
    public static Map<String, String> getAll() {
        return getContext().getContextMap();
    }
}