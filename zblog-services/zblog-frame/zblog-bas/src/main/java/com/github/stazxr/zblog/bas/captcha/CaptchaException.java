package com.github.stazxr.zblog.bas.captcha;

import com.github.stazxr.zblog.bas.exception.BaseException;
import com.github.stazxr.zblog.bas.exception.SystemException;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 自定义验证码异常类，用于处理与验证码相关的错误。
 * <p>
 * 这个异常用于表示在处理验证码操作过程中遇到的问题。
 * 它继承自 {@link BaseException}，提供了一个更具体的异常类型以处理验证码相关的问题。
 * </p>
 *
 * @author SunTao
 * @since 2024-08-20
 */
public class CaptchaException extends SystemException {
    private static final long serialVersionUID = -4486935430351080923L;

    /**
     * 构造技术异常
     *
     * @param errorCode 错误码定义
     * @param args      国际化消息参数
     */
    public CaptchaException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    /**
     * 构造基于错误码的技术异常，并指定 cause
     *
     * @param errorCode 错误码定义
     * @param cause     原始异常
     * @param args      国际化消息参数
     */
    public CaptchaException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode, cause, args);
    }
}

