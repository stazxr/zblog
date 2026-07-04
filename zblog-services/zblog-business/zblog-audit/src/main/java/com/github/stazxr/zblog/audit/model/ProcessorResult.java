package com.github.stazxr.zblog.audit.model;

import com.github.stazxr.zblog.audit.enums.AuditDecision;

import java.util.List;

/**
 * Processor 执行结果（局部结果）
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
public class ProcessorResult {
    /**
     * 决策
     */
    private AuditDecision decision;

    /**
     * 修改后的内容（仅 MODIFY 时生效）
     */
    private String content;

    /**
     * 命中的关键词
     */
    private List<String> hitWords;

    /**
     * 原因说明
     */
    private String reason;

    public AuditDecision getDecision() {
        return decision;
    }

    public void setDecision(AuditDecision decision) {
        this.decision = decision;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getHitWords() {
        return hitWords;
    }

    public void setHitWords(List<String> hitWords) {
        this.hitWords = hitWords;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
