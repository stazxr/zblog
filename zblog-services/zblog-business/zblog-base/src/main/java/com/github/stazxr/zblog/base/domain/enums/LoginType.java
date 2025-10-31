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
    GUEST("00"),

    /**
     * 密码
     */
    PASSWORD("01"),

    /**
     * QQ互信
     */
    QQ("02"),

    /**
     * 未知
     */
    UNKNOWN("99");

    private final String type;

    LoginType(String type) {
        this.type = type;
    }
}
