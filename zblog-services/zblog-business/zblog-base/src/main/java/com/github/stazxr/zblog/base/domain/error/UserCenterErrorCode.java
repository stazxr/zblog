package com.github.stazxr.zblog.base.domain.error;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 用户中心错误码定义。
 *
 * @author SunTao
 * @since 2026-02-06
 */
public enum UserCenterErrorCode implements ErrorCode {
    /** 原密码不能为空 */
    EUSERB000("USERCENTER_OLD_PASSWORD_REQUIRED"),
    /** 新密码不能为空 */
    EUSERB001("USERCENTER_NEW_PASSWORD_REQUIRED"),
    /** 确认密码不能为空 */
    EUSERB002("USERCENTER_CONFIRM_PASSWORD_REQUIRED"),
    /** 两次输入的新密码不一致 */
    EUSERB003("USERCENTER_NEW_PASSWORD_MISMATCH"),
    /** 用户信息异常，请刷新页面后重试 */
    EUSERB004("USERCENTER_PASSWORD_WITH_USER_MISS"),
    /** 原密码错误 */
    EUSERB005("USERCENTER_OLD_PASSWORD_MISMATCH"),
    /** 新密码与原密码不能相同 */
    EUSERB006("USERCENTER_NEW_PASSWORD_SAMEWITHOLD"),
    /** 密码不能包含用户名 */
    EUSERB007("USERCENTER_NEW_PASSWORD_CONTAIN_USER"),
    /** 密码复杂度过低 */
    EUSERB008("USERCENTER_NEW_PASSWORD_LOW_COMPLEXITY"),
    /** 新密码不能与历史密码重复 */
    EUSERB009("USERCENTER_NEW_PASSWORD_SAMEWITHHIS"),

    /** 头像上传失败 */
    SUSERB005("USERCENTER_IMAGE_FILE_MISS"),

    /** 邮箱验证码已过期 */
    EUSERB010("USERCENTER_EMAIL_CODE_EXPIRED"),
    /** 邮箱验证码不正确 */
    EUSERB011("USERCENTER_EMAIL_CODE_INVALID"),
    /** 新邮箱不能与旧邮箱相同 */
    EUSERB012("USERCENTER_EMAIL_SAMEWITHOLD"),
    /** 邮箱格式不正确 */
    EUSERB013("USERCENTER_EMAIL_PATTERN_INVALID"),
    /** 邮箱已被其他账号绑定 */
    EUSERB014("USERCENTER_EMAIL_EXISTS"),

    /** 昵称已存在 */
    EUSERB015("USERCENTER_PROFILE_NICKNAME_EXISTS"),
    /** 用户性别不合法 */
    EUSERB016("USERCENTER_PROFILE_GENDER_INVALID"),

    /** 密码修改失败 */
    SUSERB001("USERCENTER_PASSWORD_FAILED"),
    /** 头像修改失败 */
    SUSERB002("USERCENTER_IMAGE_FAILED"),
    /** 邮箱修改失败 */
    SUSERB003("USERCENTER_EMAIL_FAILED"),
    /** 信息修改失败 */
    SUSERB004("USERCENTER_PROFILE_FAILED");

    private final String i18nKey;

    UserCenterErrorCode(String i18nKey) {
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
