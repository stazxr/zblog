package com.github.stazxr.zblog.bas.exception;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 技术异常（SystemException）
 *
 * <p>用于表示系统级错误或不可预期异常，例如：</p>
 * <ul>
 *   <li>数据库 / Redis / MQ 异常</li>
 *   <li>RPC 调用失败</li>
 *   <li>IO、网络异常</li>
 *   <li>程序 Bug（NPE、数组越界等）</li>
 * </ul>
 *
 * <p>该异常既可以使用 ErrorCode，也可以直接使用原始 message。</p>
 *
 * @author SunTao
 * @since 2026-01-25
 */
public class SystemException extends BaseException {
    private static final long serialVersionUID = 9017392849182736453L;

    /**
     * 构造技术异常
     *
     * @param errorCode 错误码定义
     * @param args      国际化消息参数
     */
    public SystemException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    /**
     * 构造基于原始消息的技术异常
     *
     * @param message 原始异常描述
     */
    public SystemException(String message) {
        super(message);
    }

    /**
     * 构造基于原始消息的技术异常，并指定 cause
     *
     * @param message 原始异常描述
     * @param cause   原始异常
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造基于错误码的技术异常，并指定 cause
     *
     * @param errorCode 错误码定义
     * @param cause     原始异常
     * @param args      国际化消息参数
     */
    public SystemException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode, args);
        initCause(cause);
    }
}
