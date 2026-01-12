package com.github.stazxr.zblog.bas.file.path;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件路径构建上下文对象
 *
 * <p>
 * 用于在文件上传过程中向 {@link FilePathStrategy} 提供构建存储路径所需的上下文信息。
 * </p>
 *
 * <p>
 * 该对象只描述“路径生成所需的数据”，不关心文件最终存储在哪里（本地 / 云存储  / FTP ...）。
 * </p>
 *
 * <p>
 * 设计为可扩展结构，后续可无侵入支持：
 * <ul>
 *     <li>按用户 ID 分目录</li>
 *     <li>按业务模块分目录</li>
 *     <li>多租户（tenantId）</li>
 *     <li>临时文件 / 永久文件区分</li>
 * </ul>
 * </p>
 *
 * @author SunTao
 * @since 2026-01-10
 */
@Getter
@Setter
public class FilePathContext {
    /**
     * 文件名
     */
    private String filename;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 业务模块标识
     */
    private String module;

    /**
     * 是否临时文件
     */
    private boolean temporary;

    /**
     * 扩展属性（兜底字段），可用于区分临时上传与正式文件存储路径
     */
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * 获取扩展属性值。
     *
     * <p>
     * 用于从路径上下文的扩展属性中获取指定 key 对应的值。
     * 当路径规则需要额外上下文信息（如自定义业务参数、动态目录标识等）
     * 且该信息不适合作为固定字段存在时，可通过该方法获取。
     * </p>
     *
     * <p>
     * 该方法在 key 不存在时返回 {@code null}。
     * </p>
     *
     * @param key 扩展属性名称
     * @return 对应的属性值，不存在时返回 {@code null}
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * 获取扩展属性值，支持默认值。
     *
     * <p>
     * 当指定的 key 在扩展属性中不存在时，
     * 返回提供的默认值，避免调用方进行空值判断。
     * </p>
     *
     * <p>
     * 该方法适用于路径生成策略中存在可选参数的场景，
     * 例如：自定义目录层级、业务标识开关等。
     * </p>
     *
     * @param key 扩展属性名称
     * @param defaultValue 默认值（当 key 不存在时返回）
     * @return 扩展属性值或默认值
     */
    public Object getAttribute(String key, Object defaultValue) {
        return attributes.getOrDefault(key, defaultValue);
    }

    /**
     * 创建一个空的 FilePathContext
     */
    public static FilePathContext empty() {
        return new FilePathContext();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
