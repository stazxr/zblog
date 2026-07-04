package com.github.stazxr.zblog.audit.model;

import com.github.stazxr.zblog.audit.enums.AuditScene;

/**
 * 审核上下文
 *
 * @author SunTao
 * @since 2026-06-29
 */
public class AuditContext {
    /**
     * 用户标识
     */
    private String uid;

    /**
     * 审核对象标识
     */
    private String oid;

    /**
     * 原始内容（永远不变）
     */
    private final String originalContent;

    /**
     * 当前处理内容（链路中不断变化）
     */
    private String content;

    /**
     * 审核场景
     */
    private AuditScene scene;

    public AuditContext(String originalContent, AuditScene scene) {
        this.content = originalContent;
        this.originalContent = originalContent;
        this.scene = scene;
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

    public String getOriginalContent() {
        return originalContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AuditScene getScene() {
        return scene;
    }

    public void setScene(AuditScene scene) {
        this.scene = scene;
    }
}
