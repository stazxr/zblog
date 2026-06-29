package com.github.stazxr.zblog.audit.model;

import java.time.LocalDateTime;

public class AuditRecord {
    private Long userId;

    private AuditScene scene;

    private String originalContent;

    private String finalContent;

    private AuditStatus status;

    private String reason;

    private Long cost;

    private LocalDateTime auditTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public LocalDateTime getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }
}