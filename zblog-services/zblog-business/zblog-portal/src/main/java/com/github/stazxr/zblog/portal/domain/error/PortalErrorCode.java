package com.github.stazxr.zblog.portal.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 门户错误码定义。
 *
 * @author SunTao
 * @since 2026-08-30
 */
public enum PortalErrorCode implements ErrorCode {
    /** 友链申请开关已关闭 */
    EPORTA001("PORTAL_FRIEND_LINK_SWITCH_OFF");

    private final String i18nKey;

    PortalErrorCode(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * 错误码，唯一且符合规范
     *
     * @return 错误码字符串
     */
    @Override
    public String getCode() {
        return name();
    }

    /**
     * 国际化 key，用于获取对应消息
     *
     * @return i18n key
     */
    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}
