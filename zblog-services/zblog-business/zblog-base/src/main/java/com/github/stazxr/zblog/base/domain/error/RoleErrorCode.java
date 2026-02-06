package com.github.stazxr.zblog.base.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 角色错误码定义。
 *
 * @author SunTao
 * @since 2026-02-06
 */
public enum RoleErrorCode implements ErrorCode {
    /**
     * 角色名称已存在
     */
    EROLEA000("ROLE_ROLENAME_EXISTS"),

    /**
     * 角色编码已存在
     */
    EROLEA001("ROLE_ROLECODE_EXISTS"),

    /**
     * 角色编码格式不正确
     */
    EROLEA002("ROLE_ROLECODE_PATTERN_INVALID"),

    /**
     * 角色编码不可用
     */
    EROLEA003("ROLE_ROLECODE_DISABLED"),

    /**
     * 角色已与用户关联
     */
    EROLEA004("ROLE_DELETE_WITH_USER");

    private final String i18nKey;

    RoleErrorCode(String i18nKey) {
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
