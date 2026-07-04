package com.github.stazxr.zblog.audit.enums;

/**
 * XSS 清洗策略
 *
 * @author SunTao
 * @since 2026-07-05
 */
public enum XssStrategy {
    /**
     * 清洗
     */
    CLEAN,
    /**
     * 直接拒绝
     */
    REJECT,
    /**
     * 人工
     */
    MANUAL
}
