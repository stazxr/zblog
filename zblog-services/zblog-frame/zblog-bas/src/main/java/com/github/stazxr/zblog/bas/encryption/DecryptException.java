package com.github.stazxr.zblog.bas.encryption;

/**
 * 解密异常
 *
 * @author SunTao
 * @since 2024-07-25
 */
public class DecryptException extends EncryptorException {
    private static final long serialVersionUID = -8976461621479467662L;

    /**
     * 构造一个新的 DecryptException 实例。
     *
     * @param messageCode 错误码
     */
    public DecryptException(String messageCode) {
        super(messageCode);
    }

    /**
     * 构造一个新的 DecryptException 实例。
     *
     * @param messageCode 错误码
     * @param throwable   异常信息
     */
    public DecryptException(String messageCode, Throwable throwable) {
        super(messageCode, throwable);
    }
}
