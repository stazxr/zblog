package com.github.stazxr.zblog.core.exception;

/**
 * 自定义业务异常，Json转换错误
 *
 * @author SunTao
 * @since 2020-11-16
 */
public class ParseJsonException extends RuntimeException {
    /**
     * serialId
     */
    private static final long serialVersionUID = 8442431512967536240L;

    public ParseJsonException() {
        super();
    }

    public ParseJsonException(String message) {
        super(message);
    }

    public ParseJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
