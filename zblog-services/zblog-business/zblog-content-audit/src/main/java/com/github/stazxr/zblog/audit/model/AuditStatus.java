package com.github.stazxr.zblog.audit.model;

/**
 * 审核状态
 *
 * @author SunTao
 * @since 2026-06-29
 */
public enum AuditStatus {
    /**
     * 审核通过
     */
    PASS,

    /**
     * 内容被替换
     */
    REPLACE,

    /**
     * 审核拒绝
     */
    REJECT,

    /**
     * 待人工审核
     */
    PENDING
}