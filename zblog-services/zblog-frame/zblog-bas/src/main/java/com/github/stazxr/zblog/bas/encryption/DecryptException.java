package com.github.stazxr.zblog.bas.encryption;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 解密异常
 *
 * @author SunTao
 * @since 2024-07-25
 */
public class DecryptException extends EncryptorException {
    private static final long serialVersionUID = -8976461621479467662L;

    /**
     * 构造技术异常
     *
     * @param errorCode 错误码定义
     * @param args      国际化消息参数
     */
    public DecryptException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    /**
     * 构造基于错误码的技术异常，并指定 cause
     *
     * @param errorCode 错误码定义
     * @param cause     原始异常
     * @param args      国际化消息参数
     */
    public DecryptException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode, cause, args);
    }
}
