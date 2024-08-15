package com.github.stazxr.zblog.sequence;

import com.github.stazxr.zblog.common.exception.BaseException;
import com.github.stazxr.zblog.common.exception.ExpMessageCode;

/**
 * 序号异常
 *
 * @author SunTao
 * @since 2024-08-10
 */
public class SequenceException extends BaseException {
    private static final long serialVersionUID = -2147223112586046182L;

    public SequenceException(String message) {
        super(new ExpMessageCode(message));
    }

    public SequenceException(String message, Throwable cause) {
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
        String message = StatusCode.of(messageCode);
        if (message != null) {
            messageCode = "[" + messageCode + "] ".concat(message).concat(super.getMessage());
        }
        return messageCode;
    }
}
