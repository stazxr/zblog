package com.github.stazxr.zblog.notify.mail;

import com.github.stazxr.zblog.exception.BaseException;

/**
 * 邮件处理异常
 *
 * @author SunTao
 * @since 20204-08-04
 */
public class MailException extends BaseException {
    private static final long serialVersionUID = -5944640292601191861L;

    /**
     * Constructs a new MailException with the specified detail message.
     *
     * @param message the detail message
     */
    public MailException(String message) {
        super(message);
    }

    /**
     * Constructs a new MailException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public MailException(String message, Throwable cause) {
        super(message, cause);
    }
}
