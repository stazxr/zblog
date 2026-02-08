package com.github.stazxr.zblog.bas.exception;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 业务异常
 *
 * <p>用于表示业务规则校验失败、状态不合法、资源不存在等
 * <b>可预期的业务异常场景</b>。</p>
 *
 * <p><b>强制要求：</b>必须携带 {@link ErrorCode}，禁止直接使用原始 message。</p>
 *
 * @author SunTao
 * @since 2026-01-25
 */
public class ServiceException extends BaseException {
    private static final long serialVersionUID = 3903109720488016902L;

    /**
     * 构造业务异常（无占位参数）
     *
     * @param errorCode 错误码定义
     */
    public ServiceException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * 构造业务异常（带国际化参数）
     *
     * @param errorCode 错误码定义
     * @param args      国际化消息参数
     */
    public ServiceException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    /**
     * 构造业务异常（带国际化参数）
     *
     * @param errorCode 错误码定义
     * @param cause     异常信息
     * @param args      国际化消息参数
     */
    public ServiceException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode, args);
        initCause(cause);
    }
}
