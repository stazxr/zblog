package com.github.stazxr.zblog.bas.security.core;

/**
 * 用户状态
 *
 * @author SunTao
 * @since 2024-11-10
 */
public enum UserStatus {
    /**
     * 正常
     */
    NORMAL(0),

    /**
     * 禁用
     */
    FORBID(1),

    /**
     * 锁定
     */
    LOCKED(2);

    private final Integer status;

    UserStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
