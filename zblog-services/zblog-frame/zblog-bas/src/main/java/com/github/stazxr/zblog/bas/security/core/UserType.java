package com.github.stazxr.zblog.bas.security.core;

/**
 * 用户类型
 *
 * @author SunTao
 * @since 2024-11-10
 */
public enum UserType {
    /**
     * 系统用户
     */
    SYSTEM_USER(0),

    /**
     * 普通用户
     */
    NORMAL_USER(1),

    /**
     * 管理员用户
     */
    ADMIN_USER(2),

    /**
     * 测试用户
     */
    TEST_USER(3),

    /**
     * 临时用户
     */
    TEMP_USER(4);

    private final Integer type;

    UserType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
