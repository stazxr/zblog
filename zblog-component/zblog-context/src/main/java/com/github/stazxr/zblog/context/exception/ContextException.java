package com.github.stazxr.zblog.context.exception;

import com.github.stazxr.zblog.common.exception.BaseException;

/**
 * The Context exceptions in the framework.
 * Extends RuntimeException to allow unchecked exception handling.

 * @author SunTao
 * @since 2024-06-26
 */
public class ContextException extends BaseException {
    private static final long serialVersionUID = -8451328244154695168L;

    public ContextException(ContextErrorCode errorCode) {
        super(errorCode.toString());
    }

    public ContextException(ContextErrorCode errorCode, Throwable cause) {
        super(errorCode.toString(), cause);
    }
}
