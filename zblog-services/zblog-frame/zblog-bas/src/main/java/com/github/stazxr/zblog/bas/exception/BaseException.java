package com.github.stazxr.zblog.bas.exception;

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
     * 错误码/错误信息
     */
    private String code = "";

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
     * Constructs a new BaseException with the specified detail message code.
     *
     * @param messageCode the detail message code
     */
    public BaseException(ExpMessageCode messageCode) {
        super("");
        code = messageCode.getCode();
    }

    /**
     * Constructs a new BaseException with the specified detail message code.
     *
     * @param messageCode the detail message code
     */
    public BaseException(ExpMessageCode messageCode, Throwable cause) {
        super("", cause);
        code = messageCode.getCode();
    }

    /**
     * Overrides getMessage() method to return a formatted message using MusesExceptionUtil.
     *
     * @return the formatted message
     */
    public String getMessage() {
        return ThrowableUtils.buildMessage(super.getMessage(), this.getCause());
    }

    /**
     * Get the message code
     *
     * @return message code
     */
    public String getCode() {
        return code;
    }

    /**
     * Retrieves the root cause of this exception.
     *
     * @return the root cause, or null if there is no root cause
     */
    public Throwable getRootCause() {
        return ThrowableUtils.getRootCause(this);
    }

    /**
     * Retrieves the most specific cause of this exception.
     *
     * @return the most specific cause, or this exception if no more specific cause is found
     */
    public Throwable getMostSpecificCause() {
        return ThrowableUtils.getMostSpecificCause(this);
    }

    /**
     * Checks whether this exception contains an instance of the specified exception type
     * within its cause chain.
     *
     * @param exType the exception type to look for
     * @return true if an instance of the specified exception type is found, false otherwise
     */
    public boolean contains(Class<?> exType) {
        if (exType == null) {
            return false;
        } else if (exType.isInstance(this)) {
            return true;
        } else {
            Throwable cause = this.getCause();
            if (cause == this) {
                return false;
            } else if (cause instanceof BaseException) {
                return ((BaseException) cause).contains(exType);
            } else {
                while (cause != null) {
                    if (exType.isInstance(cause)) {
                        return true;
                    }
                    if (cause.getCause() == cause) {
                        break;
                    }
                    cause = cause.getCause();
                }
                return false;
            }
        }
    }
}
