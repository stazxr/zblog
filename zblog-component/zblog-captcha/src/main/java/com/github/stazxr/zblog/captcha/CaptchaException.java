package com.github.stazxr.zblog.captcha;

import com.github.stazxr.zblog.common.exception.BaseException;

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
public class CaptchaException extends BaseException {
    private static final long serialVersionUID = -4486935430351080923L;

    /**
     * 使用指定的详细消息构造一个新的 CaptchaException 实例。
     *
     * @param message 详细消息
     */
    public CaptchaException(String message) {
        super(message);
    }

    /**
     * 使用指定的详细消息和原因构造一个新的 CaptchaException 实例。
     *
     * @param message 详细消息
     * @param cause 引起此异常的原因，允许为 null，表示原因不存在或未知
     */
    public CaptchaException(String message, Throwable cause) {
        super(message, cause);
    }
}

