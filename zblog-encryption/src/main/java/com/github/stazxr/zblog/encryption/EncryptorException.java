package com.github.stazxr.zblog.encryption;

/**
 * 加解密过程中的异常基类
 *
 * @author SunTao
 * @since 2024-07-25
 */
public class EncryptorException extends RuntimeException {
    private static final long serialVersionUID = 7808497479350394417L;

    /**
     * 构造一个新的 EncryptorException 实例。
     * @param message 错误信息
     */
    public EncryptorException(String message) {
        super(message);
    }

    /**
     * 构造一个新的 EncryptorException 实例。
     * @param message 错误信息
     * @param cause 原因
     */
    public EncryptorException(String message, Throwable cause) {
        super(message, cause);
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
