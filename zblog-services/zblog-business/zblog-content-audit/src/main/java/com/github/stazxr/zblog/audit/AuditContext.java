package com.github.stazxr.zblog.audit;

import com.github.stazxr.zblog.audit.model.AuditScene;

/**
 * 审核上下文
 *
 * @author SunTao
 * @since 2026-06-29
 */
public class AuditContext {
    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 审核内容
     */
    private String content;

    /**
     * 审核后的内容
     */
    private String auditContent;

    /**
     * 审核场景
     */
    private AuditScene scene;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    public AuditScene getScene() {
        return scene;
    }

    public void setScene(AuditScene scene) {
        this.scene = scene;
    }
}