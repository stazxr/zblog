package com.github.stazxr.zblog.content.ext.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 友链错误码定义。
 *
 * @author SunTao
 * @since 2026-04-15
 */
public enum FriendLinkErrorCode implements ErrorCode {
    /** 友链已存在 */
    ELINKA001("FRIEND_LINK_EXISTS"),
    /** 网站地址解析错误 */
    ELINKA002("FRIEND_LINK_URL_INVALID");

    private final String i18nKey;

    FriendLinkErrorCode(String i18nKey) {
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
