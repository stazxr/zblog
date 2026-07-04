package com.github.stazxr.zblog.audit.enums;

/**
 * 审核决策（Processor / Engine 统一语义）
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
public enum AuditDecision {
    /**
     * 通过（继续执行链）
     */
    PASS,

    /**
     * 修改内容后通过（继续执行链）
     */
    MODIFY,

    /**
     * 拒绝（立即终止链）
     */
    REJECT,

    /**
     * 人工审核（立即终止链）
     */
    MANUAL
}
