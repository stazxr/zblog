package com.github.stazxr.zblog.base.domain.enums;

import lombok.Getter;

/**
 * 登录渠道
 *
 * @author SunTao
 * @since 2025-10-17
 */
@Getter
public enum LoginChan {
    /**
     * 移动端
     */
    MOBILE("01"),

    /**
     * PC端
     */
    PC("02");

    private final String chan;

    LoginChan(String chan) {
        this.chan = chan;
    }
}
