package com.github.stazxr.zblog.bas.context;

import com.github.stazxr.zblog.bas.context.entity.ContextTag;
import com.github.stazxr.zblog.bas.context.autoconfigure.properties.ContextProperties;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 核心上下文管理类（CoreContext）。
 *
 * <p>
 * 每个 CoreContext 实例持有自己的上下文 Map 和缓存 Map，用于存储允许的上下文标签。
 * CoreContext 实例通常通过 {@link CoreContextHolder} 以线程本地 ThreadLocal 的方式管理，
 * 保证线程安全。
 * </p>
 *
 * @author SunTao
 * @since 2024-07-02
 */
public class CoreContext {

    /** 线程上下文标签 Map，存储 key-value 对 */
    private final Map<String, String> contextMap = new ConcurrentHashMap<>();

    /** 标签缓存 Map，用于标记哪些标签是允许的 */
    private final Map<String, Boolean> cacheMap = new ConcurrentHashMap<>();

    /** 配置的上下文属性 */
    private final ContextProperties contextProperties;

    /**
     * 构造函数
     *
     * @param contextProperties 上下文配置属性
     */
    public CoreContext(ContextProperties contextProperties) {
        this.contextProperties = contextProperties;
    }

    /**
     * 获取指定 key 的上下文值。
     *
     * @param key 标签名称
     * @return 标签值，如果不存在返回 null
     */
    public String get(String key) {
        if (key == null) return null;
        return contextMap.get(key.toLowerCase(Locale.ROOT));
    }

    /**
     * 添加单个上下文标签。
     * <p>
     * 如果标签名不在允许列表中，则不会被存储。
     * 标签名在存储时会转换为小写，以保证大小写不敏感。
     * </p>
     *
     * @param contextTag 上下文标签
     */
    public void put(ContextTag contextTag) {
        if (contextTag == null || contextTag.getTagName() == null) return;

        String key = contextTag.getTagName().toLowerCase(Locale.ROOT);
        Boolean allowed = cacheMap.get(key);

        // 缓存不存在时，判断是否允许
        if (allowed == null) {
            allowed = contextProperties.getTagNames()
                    .stream()
                    .anyMatch(tag -> tag.toLowerCase(Locale.ROOT).equals(key));
            cacheMap.put(key, allowed);
        }

        // 允许的标签才存入 contextMap
        if (allowed) {
            contextMap.put(key, contextTag.getTagValue());
        }
    }

    /**
     * 批量添加上下文标签。
     *
     * @param tags 上下文标签列表
     */
    public void putAll(List<ContextTag> tags) {
        if (tags == null || tags.isEmpty()) return;
        tags.forEach(this::put);
    }

    /**
     * 复制上下文。
     */
    public CoreContext copy() {
        CoreContext copy = new CoreContext(contextProperties);
        copy.contextMap.putAll(contextMap);
        copy.cacheMap.putAll(cacheMap);
        return copy;
    }

    /**
     * 获取当前上下文 Map 的不可修改副本。
     * <p>
     * 用于外部只读访问，如 {@link Context#getAll()} 调用。
     * </p>
     *
     * @return 上下文 Map 的只读副本
     */
    public Map<String, String> getContextMap() {
        return Collections.unmodifiableMap(contextMap);
    }

    /**
     * 获取上下文 Map 的字符串表示，方便调试。
     *
     * @return contextMap 的 toString
     */
    public String getContextMapAsString() {
        return contextMap.toString();
    }

    /**
     * 清空上下文，包括标签 Map 和缓存 Map。
     */
    public void clear() {
        contextMap.clear();
        cacheMap.clear();
    }
}
