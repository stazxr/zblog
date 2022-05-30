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

    private final String message;

    /**
     * 生成一个默认的业务异常信息 {@link ResultCode#SERVER_ERROR}
     */
    public ServiceException() {
        super();
        this.identifier = ResultCode.SERVER_ERROR.code();
        this.message = ResultCode.SERVER_ERROR.message();
    }

    /**
     * 生成一个带有错误信息的的业务异常 {@link ResultCode#SERVER_ERROR}
     *
     * @param message 错误信息
     */
    public ServiceException(String message) {
        super(message);
        this.identifier = ResultCode.SERVER_ERROR.code();
        this.message = message;
    }

    /**
     * 通过 ResultCode 枚举来抛出已经归档记录的异常
     *
     * @param resultCode {@link ResultCode}
     */
    public ServiceException(ResultCode resultCode) {
        super(resultCode.message());
        this.identifier = resultCode.code();
        this.message = resultCode.message();
    }

    /**
     * 通过 ResultCode 枚举来抛出已经归档记录的异常
     *
     * @param resultCode {@link ResultCode}
     * @param cause      异常详情
     */
    public ServiceException(ResultCode resultCode, Throwable cause) {
        super(resultCode.message(), cause);
        this.identifier = resultCode.code();
        this.message = resultCode.message();
    }

    /**
     * 通过 ResultCode 枚举来抛出已经归档记录的异常
     *
     * @param resultCode {@link ResultCode}
     * @param errorMsg 错误信息
     */
    public ServiceException(ResultCode resultCode, String errorMsg) {
        super(errorMsg);
        this.identifier = resultCode.code();
        this.message = errorMsg;
    }

    /**
     * 对异常 Throwable 进行包装，然后抛出
     *
     * @param cause 未知异常
     */
    public ServiceException(Throwable cause) {
        super(cause);
        this.identifier = ResultCode.SERVER_ERROR.code();
        this.message = cause.getMessage();
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
    public ServiceException(Integer code, String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.identifier = code;
        this.message = message;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
