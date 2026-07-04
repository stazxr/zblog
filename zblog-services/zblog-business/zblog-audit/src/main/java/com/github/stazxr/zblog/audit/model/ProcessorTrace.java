package com.github.stazxr.zblog.audit.model;

import java.util.List;

/**
 * Processor 执行轨迹
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
public class ProcessorTrace {
    /**
     * Processor 名称
     */
    private String processor;

    /**
     * 执行结果
     */
    private String status;

    /**
     * 命中的敏感词
     */
    private List<String> hitWords;

    /**
     * 处理耗时（ms）
     */
    private long costMs;

    /**
     * 备注/原因
     */
    private String reason;

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getHitWords() {
        return hitWords;
    }

    public void setHitWords(List<String> hitWords) {
        this.hitWords = hitWords;
    }

    public long getCostMs() {
        return costMs;
    }

    public void setCostMs(long costMs) {
        this.costMs = costMs;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}