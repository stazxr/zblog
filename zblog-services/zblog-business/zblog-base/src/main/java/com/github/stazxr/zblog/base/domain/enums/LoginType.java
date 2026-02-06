package com.github.stazxr.zblog.base.domain.enums;

import lombok.Getter;

/**
 * 登录方式
 *
 * @author SunTao
 * @since 2025-10-17
 */
@Getter
public enum LoginType {
    /**
     * 访客
     */
    GUEST("LT00"),

    /**
     * 密码
     */
    PASSWORD("LT01"),

    /**
     * QQ互信
     */
    QQ("LT02"),

    /**
     * 未知
     */
    UNKNOWN("LT99");

    private final String type;

    LoginType(String type) {
        this.type = type;
    }
}
