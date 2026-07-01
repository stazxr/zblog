package com.github.stazxr.zblog.audit.provider;

import java.util.List;

/**
 * 敏感词服务提供者
 *
 * @author SunTao
 * @since 2026-06-29
 */
public interface SensitiveProvider {
    /**
     * 是否包含敏感词
     *
     * @param content 内容
     * @return boolean
     */
    boolean contains(String content);

    /**
     * 替换敏感词
     *
     * @param content 内容
     * @return 替换结果
     */
    String replace(String content);

    /**
     * 查询命中的敏感词
     *
     * @param content 内容
     * @return 敏感词列表
     */
    List<String> findAll(String content);

    /**
     * 热更新词库
     */
    void reload();
}