package com.github.stazxr.zblog.bas.encryption;

/**
 * 加密异常
 *
 * @author SunTao
 * @since 2024-07-25
 */
public class EncryptException extends EncryptorException {
    private static final long serialVersionUID = 3706959942870244319L;

    /**
     * 构造一个新的 EncryptException 实例。
     *
     * @param message 错误信息
     */
    public EncryptException(String message) {
        super(message);
    }

    /**
     * 构造一个新的 EncryptException 实例。
     *
     * @param message 错误信息
     * @param cause   原因
     */
    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }
}
