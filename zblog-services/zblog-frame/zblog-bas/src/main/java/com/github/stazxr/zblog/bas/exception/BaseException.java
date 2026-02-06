package com.github.stazxr.zblog.bas.exception;

import com.github.stazxr.zblog.bas.exception.code.ErrorCode;
import com.github.stazxr.zblog.bas.exception.support.MessageSupportLoader;

/**
 * 异常体系基类
 *
 * <p>作为系统中所有自定义异常的根类，提供以下能力：</p>
 * <ul>
 *   <li>统一的错误码（ErrorCode）模型</li>
 *   <li>基于 SPI 的国际化消息解析能力</li>
 *   <li>兼容“错误码异常”和“原始消息异常”两种使用方式</li>
 * </ul>
 *
 * @author SunTao
 * @since 2026-01-25
 */
public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = 5277461682828360566L;

    /**
     * 错误码
     */
    private final ErrorCode errorCode;

    /**
     * 国际化参数
     */
    private final Object[] args;

    /**
     * 原始消息
     */
    private final String rawMessage;

    /**
     * 标准错误码异常（推荐）
     */
    protected BaseException(ErrorCode errorCode, Object... args) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
        this.args = (args == null ? new Object[0] : args);
        this.rawMessage = null;
    }

    /**
     * 自定义消息异常（兜底 / 技术异常）
     */
    protected BaseException(String message) {
        super(message);
        this.errorCode = null;
        this.args = null;
        this.rawMessage = message;
    }

    /**
     * 自定义消息 + cause
     */
    protected BaseException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
        this.args = null;
        this.rawMessage = message;
    }

    /**
     * 返回错误码
     */
    public String getCode() {
        return errorCode != null ? errorCode.getCode() : null;
    }

    /**
     * 返回异常信息
     */
    @Override
    public String getMessage() {
        if (hasErrorCode()) {
            return MessageSupportLoader.getMessage(errorCode.getI18nKey(), args);
        }
        return rawMessage;
    }

    /**
     * 判断当前异常是否携带错误码
     *
     * @return true：标准错误码异常；false：原始消息异常
     */
    public boolean hasErrorCode() {
        return errorCode != null;
    }
}
