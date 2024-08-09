package com.github.stazxr.zblog.sequence;

/**
 * 序号异常
 *
 * @author SunTao
 * @since 2024-08-10
 */
public class SequenceException extends RuntimeException {
    private static final long serialVersionUID = -2147223112586046182L;

    public SequenceException(String message) {
        super(message);
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        String messageCode = super.getMessage();
        String message = StatusCode.of(messageCode);
        if (message != null) {
            messageCode = messageCode.concat(": ").concat(message);
        }
        return messageCode;
    }
}
