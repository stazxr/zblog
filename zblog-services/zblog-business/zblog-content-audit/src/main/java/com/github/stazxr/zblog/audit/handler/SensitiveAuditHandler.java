package com.github.stazxr.zblog.audit.handler;

import com.github.stazxr.zblog.audit.AuditContext;
import com.github.stazxr.zblog.audit.AuditResult;
import com.github.stazxr.zblog.audit.config.AuditProperties;
import com.github.stazxr.zblog.audit.provider.SensitiveProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 敏感词审核处理器（L1规则引擎）。
 *
 * <p>
 * 基于敏感词字典进行快速匹配与替换，属于审核链中的第一层内容语义过滤。
 * </p>
 *
 * @author SunTao
 * @since 2026-07-01
 */
@Component
@RequiredArgsConstructor
public class SensitiveAuditHandler implements AuditHandler {
    /**
     * 敏感词提供器（L1词库）。
     */
    private final SensitiveProvider sensitiveProvider;

    /**
     * 审核配置。
     */
    private final AuditProperties auditProperties;

    /**
     * 顺序：XSS之后执行。
     */
    @Override
    public int getOrder() {
        return 200;
    }

    /**
     * 处理器名称。
     */
    @Override
    public String getName() {
        return "敏感词审核(L1)";
    }

    /**
     * 是否启用该处理器。
     */
    @Override
    public boolean support(AuditContext context) {
        return auditProperties.isEnabled() && auditProperties.getSensitive().isEnabled();
    }

    /**
     * 执行敏感词审核（L1）。
     *
     * @param context 上下文
     * @return 审核结果
     */
    @Override
    public AuditResult handle(AuditContext context) {
        // 获取待审核内容。
        String content = context.getAuditContent();

        // 内容为空直接通过。
        if (content == null || content.isEmpty()) {
            return AuditResult.pass(content);
        }

        // 1️⃣ 命中检测
        if (!sensitiveProvider.contains(content)) {
            return AuditResult.pass(content);
        }

        // 2️⃣ 获取命中词
        List<String> hitWords = sensitiveProvider.findAll(content);

        // 3️⃣ 内容替换（如果开启）
        String resultContent = content;
        if (auditProperties.getSensitive().isReplace()) {
            resultContent = sensitiveProvider.replace(content);
        }

        // 4️⃣ 是否进入人工审核
        if (auditProperties.getSensitive().isPending()) {
            return AuditResult.pending("命中敏感词：" + hitWords);
        }

        // 5️⃣ 返回结果
        return AuditResult.replace(resultContent, hitWords);
    }
}
