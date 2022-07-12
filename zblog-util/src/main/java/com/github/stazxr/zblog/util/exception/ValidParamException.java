package com.github.stazxr.zblog.util.exception;

/**
 * 自定义参数校验错误异常
 *
 * @author SunTao
 * @since 2022-07-12
 */
public class ValidParamException extends RuntimeException {
    /**
     * 生成一个默认参数校验错误异常
     */
    public ValidParamException() {
        super("参数校验错误");
    }

    /**
     * 生成一个带有错误信息的的参数校验异常
     *
     * @param message 错误信息
     */
    public ValidParamException(String message) {
        super(message);
    }
}
