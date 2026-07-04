package com.github.stazxr.zblog.audit.processor;

import com.github.stazxr.zblog.audit.model.AuditContext;
import com.github.stazxr.zblog.audit.model.ProcessorResult;

/**
 * 审核处理器
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
public interface AuditProcessor {
    /**
     * 处理内容
     *
     * @param context 审核上下文（只读使用）
     * @return 局部处理结果（仅表示建议）
     */
    ProcessorResult process(AuditContext context);

    /**
     * 唯一标识（用于注册 & 配置匹配）
     */
    String name();

    /**
     * 是否支持当前场景（可选扩展）
     */
    default boolean support(AuditContext context) {
        return true;
    }
}
