package com.github.stazxr.zblog.audit.processor;

import com.github.stazxr.zblog.audit.enums.AuditDecision;
import com.github.stazxr.zblog.audit.model.ProcessorResult;

import java.util.List;

/**
 * 审核处理器公共实现
 *
 * @author Sun Tao
 * @since 2026-07-05
 */
public abstract class AbstractAuditProcessor implements AuditProcessor {
    /**
     * PASS结果构建
     */
    protected ProcessorResult pass() {
        ProcessorResult r = new ProcessorResult();
        r.setDecision(AuditDecision.PASS);
        return r;
    }

    /**
     * MODIFY结果构建
     */
    protected ProcessorResult modify(String content, List<String> hitWords) {
        ProcessorResult r = new ProcessorResult();
        r.setDecision(AuditDecision.MODIFY);
        r.setContent(content);
        r.setHitWords(hitWords);
        return r;
    }

    /**
     * REJECT结果构建
     */
    protected ProcessorResult reject(String reason, List<String> hitWords) {
        ProcessorResult r = new ProcessorResult();
        r.setDecision(AuditDecision.REJECT);
        r.setHitWords(hitWords);
        r.setReason(reason);
        return r;
    }

    /**
     * MANUAL结果构建
     */
    protected ProcessorResult manual(String reason, List<String> hitWords) {
        ProcessorResult r = new ProcessorResult();
        r.setDecision(AuditDecision.MANUAL);
        r.setHitWords(hitWords);
        r.setReason(reason);
        return r;
    }
}
