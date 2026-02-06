package com.github.stazxr.zblog.bas.validation;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 校验错误码定义。
 *
 * @author SunTao
 * @since 2026-01-25
 */
public enum ValidationErrorCode implements ErrorCode {
    /**
     * 参数校验失败（MethodArgumentNotValidException）
     */
    EVALIA000("error.service.validation.param"),

    /**
     * 数据校验失败（AssertException）
     */
    EVALIA001("error.service.validation.data"),

    /**
     * 参数错误（ServletRequestBindingException,TypeMismatchException,UnexpectedTypeException）
     */
    EVALIA002("error.service.validation.unknown");

    private final String i18nKey;

    ValidationErrorCode(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * 错误码
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