package com.github.stazxr.zblog.core.exception;

import com.github.stazxr.zblog.util.ThrowableUtils;

/**
 * 异常基类
 *
 * @author SunTao
 * @since 2022-03-09
 */
public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = 8442431512167536240L;

    /**
     * Constructs a new BaseException with the specified detail message.
     *
     * @param message the detail message
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * Constructs a new BaseException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Overrides getMessage() method to return a formatted message using MusesExceptionUtil.
     *
     * @return the formatted message
     */
    public String getMessage() {
        return ThrowableUtils.buildMessage(super.getMessage(), this.getCause());
    }
}
