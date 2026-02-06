package com.github.stazxr.zblog.bas.validation;

import com.github.stazxr.zblog.bas.exception.ServiceException;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.exception.code.ErrorCodeUtils;

/**
 * 参数校验异常
 *
 * @author SunTao
 * @since 2025-08-16
 */
public class AssertException extends ServiceException {
    private static final long serialVersionUID = 4326907104671228501L;

    /**
     * 构造业务异常
     *
     * @param message   错误信息
     * @param args      消息参数
     */
    public AssertException(String message, Object... args) {
        super(ErrorCodeUtils.of(ValidationErrorCode.EVALIA001.getCode(), message), args);
    }

    /**
     * 构造业务异常（带国际化参数）
     *
     * @param errorCode 错误码定义
     * @param args      国际化消息参数
     */
    public AssertException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
