package com.github.stazxr.zblog.bas.encryption;

import com.github.stazxr.zblog.bas.exception.BaseException;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;

/**
 * 加解密过程中的异常基类
 *
 * @author SunTao
 * @since 2024-07-25
 */
public class EncryptorException extends BaseException {
    private static final long serialVersionUID = 7808497479350394417L;

    /**
     * 构造一个新的 EncryptorException 实例。
     * @param message 错误信息
     */
    public EncryptorException(String message) {
        super(new ExpMessageCode(message));
    }

    /**
     * 构造一个新的 EncryptorException 实例。
     * @param message 错误信息
     * @param cause 原因
     */
    public EncryptorException(String message, Throwable cause) {
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
