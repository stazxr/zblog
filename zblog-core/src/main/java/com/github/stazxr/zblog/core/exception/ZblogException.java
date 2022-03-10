package com.github.stazxr.zblog.core.exception;

/**
 * 系统异常
 *
 * @author SunTao
 * @since 2022-03-09
 */
class ZblogException extends RuntimeException {
    /**
     * serialId
     */
    private static final long serialVersionUID = 8442431512167536240L;

    public ZblogException() {
        super();
    }

    public ZblogException(String message) {
        super(message);
    }

    public ZblogException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZblogException(Throwable cause) {
        super(cause);
    }

    public ZblogException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
