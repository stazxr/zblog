package com.github.stazxr.zblog.bas.validation;

import com.github.stazxr.zblog.bas.exception.BaseException;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;

/**
 * 参数校验异常
 *
 * @author SunTao
 * @since 2025-08-16
 */
public class AssertException extends BaseException {
    private static final long serialVersionUID = 4326907104671228501L;

    /**
     * 使用自定义消息构造异常
     *
     * @param message 异常消息
     */
    public AssertException(String message) {
        super(message);
    }

    /**
     * 使用国际化消息码构造异常
     *
     * @param expMessageCode 异常消息码
     */
    public AssertException(ExpMessageCode expMessageCode) {
        super(expMessageCode);
    }
}
