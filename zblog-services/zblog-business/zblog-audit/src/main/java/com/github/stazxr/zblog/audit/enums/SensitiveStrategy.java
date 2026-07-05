package com.github.stazxr.zblog.audit.enums;

/**
 * 敏感词策略类型
 *
 * @author SunTao
 * @since 2026-07-05
 */
public enum SensitiveStrategy {
    /**
     * 替换
     */
    REPLACE,
    /**
     * 直接拒绝
     */
    REJECT,
    /**
     * 人工
     */
    MANUAL
}
