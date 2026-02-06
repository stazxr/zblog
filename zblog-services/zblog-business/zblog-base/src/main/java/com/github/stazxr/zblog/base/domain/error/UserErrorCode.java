package com.github.stazxr.zblog.base.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 用户错误码定义。
 *
 * @author SunTao
 * @since 2026-02-05
 */
public enum UserErrorCode implements ErrorCode {
    /** 用户名已存在 */
    EUSERA000("USER_USERNAME_EXISTS"),
    /** 用户名格式不正确 */
    EUSERA001("USER_USERNAME_PATTERN_INVALID"),
    /** 邮箱已注册 */
    EUSERA002("USER_EMAIL_ALREADY_EXISTS"),
    /** 邮箱格式不正确 */
    EUSERA003("USER_EMAIL_PATTERN_INVALID"),
    /** 昵称已存在 */
    EUSERA004("USER_NICKNAME_ALREADY_EXISTS"),
    /** 临时用户账号有效期不能为空 */
    EUSERA005("USER_EXPIRETIME_REQUIRED_WITH_TEMP"),
    /** 没有数据操作权限 */
    EUSERA006("USER_NO_DATA_OPE_PERMISSION"),
    /** 无法删除自己 */
    EUSERA007("USER_NO_PERMISSION_TO_DELETE_SELF"),
    /** 用户不存在 */
    EUSERA008("USER_NOT_EXISTS"),
    /** 用户名禁止修改 */
    EUSERA009("USER_USERNAME_IMMUTABLE");

    private final String i18nKey;

    UserErrorCode(String i18nKey) {
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
