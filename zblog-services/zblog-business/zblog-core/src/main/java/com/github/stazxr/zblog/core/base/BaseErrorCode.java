package com.github.stazxr.zblog.core.base;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 错误码配置
 *
 * @author SunTao
 * @since 2026-02-06
 */
public enum BaseErrorCode implements ErrorCode {
    /* ***** 业务异常 ***** */

    /** 数据不存在 */
    ECOREA001("DATA_NOT_FOUND"),

    /* ***** 技术异常 ***** */

    /** 新增失败 */
    SCOREA001("ADD_FAILED"),
    /** 编辑失败 */
    SCOREA002("EDIT_FAILED"),
    /** 删除失败 */
    SCOREA003("DELETE_FAILED"),
    /** 邮件发送失败 */
    SCOREA004("EMAIL_SEND_ERROR"),

    /** 技术参数缺失 */
    SCOREB000("TECH_PARAM_MISS"),
    /** 参数异常[PE001] */
    SCOREB001("TECH_PARAM_INVALID1"),
    /** 参数异常[PE002] */
    SCOREB002("TECH_PARAM_INVALID2"),
    /** 参数异常[PE003] */
    SCOREB003("TECH_PARAM_INVALID3"),

    /** 数据加密失败 */
    SCOREC001("DATA_ENCRYPT_FAILED"),
    /** 数据解密失败 */
    SCOREC002("DATA_DECRYPT_FAILED"),
    ;

    private final String i18nKey;

    BaseErrorCode(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * 错误码（唯一且符合统一规范）
     */
    @Override
    public String getCode() {
        return name();
    }

    /**
     * 国际化 key，用于获取对应消息
     */
    @Override
    public String getI18nKey() {
        return i18nKey;
    }
}
