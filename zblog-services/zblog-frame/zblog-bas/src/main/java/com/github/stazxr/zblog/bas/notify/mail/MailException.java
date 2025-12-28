package com.github.stazxr.zblog.bas.notify.mail;

import com.github.stazxr.zblog.bas.exception.BaseException;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;

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
        super(new ExpMessageCode(message));
    }

    /**
     * Constructs a new MailException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public MailException(String message, Throwable cause) {
        super(new ExpMessageCode(message), cause);
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        String messageCode = getCode();
        String message = MailExceptionCode.of(messageCode);
        if (message != null) {
            return message.concat(": ").concat(super.getMessage());
        }
        return messageCode;
    }
}
