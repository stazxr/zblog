package com.github.stazxr.zblog.bas.idempotence;

import com.github.stazxr.zblog.bas.exception.ServiceException;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 幂等异常
 *
 * @author SunTao
 * @since 2026-02-02
 */
public class IdempotentException extends ServiceException {
    private static final long serialVersionUID = -270678276464386533L;

    /**
     * 构造业务异常（无占位参数）
     *
     * @param errorCode 错误码定义
     */
    public IdempotentException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * 构造业务异常（带国际化参数）
     *
     * @param errorCode 错误码定义
     * @param args      国际化消息参数
     */
    public IdempotentException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
