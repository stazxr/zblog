package com.github.stazxr.zblog.core.exception;

/**
 * 自定义业务异常
 *
 * @author SunTao
 * @since 2020-11-16
 */
public class ServiceException extends RuntimeException {
    /**
     * serialId
     */
    private static final long serialVersionUID = 8442431512967536240L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
