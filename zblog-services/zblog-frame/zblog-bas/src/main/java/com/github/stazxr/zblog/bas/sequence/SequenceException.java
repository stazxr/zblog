package com.github.stazxr.zblog.bas.sequence;

import com.github.stazxr.zblog.bas.exception.SystemException;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 序号异常
 *
 * @author SunTao
 * @since 2024-08-10
 */
public class SequenceException extends SystemException {
    private static final long serialVersionUID = -2147223112586046182L;

    /**
     * 构造技术异常
     *
     * @param errorCode 错误码定义
     * @param args      国际化消息参数
     */
    public SequenceException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    /**
     * 构造基于错误码的技术异常，并指定 cause
     *
     * @param errorCode 错误码定义
     * @param cause     原始异常
     * @param args      国际化消息参数
     */
    public SequenceException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode, cause, args);
    }
}
