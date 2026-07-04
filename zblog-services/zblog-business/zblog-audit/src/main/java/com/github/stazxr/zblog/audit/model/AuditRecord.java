package com.github.stazxr.zblog.audit.model;

import com.github.stazxr.zblog.audit.enums.AuditDecision;
import com.github.stazxr.zblog.audit.enums.AuditScene;

import java.util.Date;
import java.util.List;

/**
 * 审核记录（审计事实模型）
 *
 * <p>
 * 用于记录一次完整审核生命周期，包括：
 * <ul>
 *     <li>输入信息（原始内容）</li>
 *     <li>输出结果（最终内容）</li>
 *     <li>决策信息（PASS/REJECT/MANUAL）</li>
 *     <li>执行过程（Processor Trace）</li>
 *     <li>风险信息（扩展能力）</li>
 * </ul>
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
public class AuditRecord {
    /**
     * 主键
     */
    private String id;

    /**
     * 用户标识
     */
    private String uid;

    /**
     * 业务对象ID
     */
    private String oid;

    /**
     * 审核场景
     */
    private AuditScene scene;

    /**
     * 原始内容（不可变）
     */
    private String originalContent;

    /**
     * 最终内容（可能被修改）
     */
    private String finalContent;

    /**
     * 审核决策结果
     */
    private AuditDecision decision;

    /**
     * 命中敏感词（用于分析）
     */
    private List<String> hitWords;

    /**
     * 审核原因（多processor拼接）
     */
    private String reason;

    /**
     * Processor执行轨迹（可观测）
     */
    private List<ProcessorTrace> traces;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 执行耗时（毫秒）
     */
    private Long costMs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public AuditScene getScene() {
        return scene;
    }

    public void setScene(AuditScene scene) {
        this.scene = scene;
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }

    public String getFinalContent() {
        return finalContent;
    }

    public void setFinalContent(String finalContent) {
        this.finalContent = finalContent;
    }

    public AuditDecision getDecision() {
        return decision;
    }

    public void setDecision(AuditDecision decision) {
        this.decision = decision;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCostMs() {
        return costMs;
    }

    public void setCostMs(Long costMs) {
        this.costMs = costMs;
    }
}