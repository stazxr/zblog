package com.github.stazxr.zblog.base.domain.enums;

import lombok.Getter;

/**
 * 用户微信公众号认证信息
 *
 * @author SunTao
 * @since 2026-10-31
 */
@Getter
public enum UserWechatAccountStatus {
    /**
     * 男
     */
    NONE(0),

    /**
     * 女
     */
    SUCCESS(1),

    /**
     * 隐藏
     */
    FAILED(2);

    private final Integer status;

    UserWechatAccountStatus(Integer status) {
        this.status = status;
    }
}
