package com.github.stazxr.zblog.audit.provider.source;

import java.util.List;

/**
 * 敏感词数据源
 *
 * @author SunTao
 * @since 2026-07-05
 */
public interface SensitiveWordSource {
    /**
     * 加载敏感词
     */
    List<String> load();
}
