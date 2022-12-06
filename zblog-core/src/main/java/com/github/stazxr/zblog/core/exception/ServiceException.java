package com.github.stazxr.zblog.core.exception;

import com.github.stazxr.zblog.core.enums.ResultCode;

/**
 * 自定义业务异常
 *
 * @author SunTao
 * @since 2020-11-16
 */
public class ServiceException extends ZblogException {
    /**
     * serialId
     */
    private static final long serialVersionUID = 8442431512967536240L;

    /**
     * 业务自定义的状态码或响应码
     */
    private final Integer identifier;

    /**
     * 错误提示信息
     */
    private final String message;

    /**
     * 系统异常
     */
    private Throwable throwable = null;

    /**
     * 生成一个默认的业务异常信息 {@link ResultCode#SERVER_EXP}
     */
    public ServiceException() {
        this(ResultCode.SERVER_EXP);
    }

    /**
     * 生成一个带有错误信息的的业务异常 {@link ResultCode#SERVER_EXP}
     *
     * @param message 错误信息
     */
    public ServiceException(String message) {
        this(ResultCode.SERVER_EXP, message);
    }

    /**
     * 通过 ResultCode 枚举来抛出已经归档记录的异常
     *
     * @param resultCode {@link ResultCode}
     */
    public ServiceException(ResultCode resultCode) {
        this(resultCode.code(), resultCode.message());
    }

    /**
     * 通过 ResultCode 枚举来抛出已经归档记录的异常
     *
     * @param resultCode {@link ResultCode}
     * @param errorMsg 错误信息
     */
    public ServiceException(ResultCode resultCode, String errorMsg) {
        this(resultCode.code(), errorMsg);
    }

    /**
     * 抛出自定义异常
     *
     * @param code    异常标识
     * @param message 异常信息
     */
    public ServiceException(Integer code, String message) {
        super(message);
        this.identifier = code;
        this.message = message;
    }

    /**
     * 对异常 Throwable 进行包装，然后抛出
     *
     * @param cause 未知异常
     */
    public ServiceException(Throwable cause) {
        this(ResultCode.SERVER_EXP.code(), cause.getMessage(), cause);
    }

    /**
     * 生成一个带有错误信息的的业务异常 {@link ResultCode#SERVER_EXP}
     *
     * @param message 业务提示信息
     * @param cause   异常信息
     */
    public ServiceException(String message, Throwable cause) {
        this(ResultCode.SERVER_EXP.code(), message, cause);
    }

    /**
     * 通过 ResultCode 枚举来抛出已经归档记录的异常
     *
     * @param resultCode {@link ResultCode}
     * @param cause      异常详情
     */
    public ServiceException(ResultCode resultCode, Throwable cause) {
        this(resultCode.code(), resultCode.message(), cause);
    }

    /**
     * 抛出自定义异常
     *
     * @param code    异常标识
     * @param message 异常信息
     * @param cause   异常详情
     */
    public ServiceException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.identifier = code;
        this.message = message;
        this.throwable = cause;
    }

    /**
     * 抛出自定义异常
     *
     * @param code                异常标识
     * @param message             异常信息
     * @param cause               异常详情
     * @param enableSuppression   是否异常挂起
     * @param writableStackTrace  表示是否生成栈追踪信息
     */
    public ServiceException(Integer code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.identifier = code;
        this.message = message;
        this.throwable = cause;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
