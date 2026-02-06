package com.github.stazxr.zblog.bas.notify.mail;

import com.github.stazxr.zblog.bas.exception.SystemException;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 邮件处理异常
 *
 * @author SunTao
 * @since 20204-08-04
 */
public class MailException extends SystemException {
    private static final long serialVersionUID = -5944640292601191861L;

    /**
     * 构造技术异常
     *
     * @param errorCode 错误码定义
     * @param args      国际化消息参数
     */
    public MailException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    /**
     * 构造基于错误码的技术异常，并指定 cause
     *
     * @param errorCode 错误码定义
     * @param cause     原始异常
     * @param args      国际化消息参数
     */
    public MailException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode, cause, args);
    }
}
