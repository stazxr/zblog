package com.github.stazxr.zblog.audit.model;

import com.github.stazxr.zblog.audit.enums.AuditDecision;

import java.util.List;

/**
 * 审核最终结果
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
public class AuditResult {
    /**
     * 最终决策
     */
    private AuditDecision decision;

    /**
     * 最终内容（可能被修改）
     */
    private String content;

    /**
     * 命中的关键词汇总
     */
    private List<String> hitWords;

    /**
     * 原因
     */
    private String reason;

    /**
     * 执行链路追踪（可观测性）
     */
    private List<ProcessorTrace> traces;

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

    public List<ProcessorTrace> getTraces() {
        return traces;
    }

    public void setTraces(List<ProcessorTrace> traces) {
        this.traces = traces;
    }
}