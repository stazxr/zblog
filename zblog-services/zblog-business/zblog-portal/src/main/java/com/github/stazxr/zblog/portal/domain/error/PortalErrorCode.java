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
    EPORTA001("PORTAL_FRIEND_LINK_SWITCH_OFF"),
    /** 评论对象不存在 */
    EPORTA002("PORTAL_COMMENT_OBJECT_NOT_EXIST"),
    /** 回复的评论不存在或已被删除 */
    EPORTA003("PORTAL_COMMENT_PARENT_NOT_EXIST"),
    /** 回复层级错误 */
    EPORTA004("PORTAL_COMMENT_PARENT_LEVEL_INVALID"),
    /** 回复评论无效 */
    EPORTA005("PORTAL_COMMENT_OBJECT_INVALID"),
    /** 身份信息异常 */
    EPORTA006("PORTAL_USER_INVALID"),
    /** 只允许删除自己的评论 */
    EPORTA007("PORTAL_COMMENT_DELETE_PERM_ERROR");

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
