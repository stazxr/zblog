package com.github.stazxr.zblog.core.exception;

import com.github.stazxr.zblog.core.enums.ResultCode;

/**
 * 错误的配置信息导致的异常
 *
 * @author SunTao
 * @since 2022-05-20
 */
public class BadConfigurationException extends ServiceException {
    /**
     * 生成一个默认配置信息错误异常
     */
    public BadConfigurationException() {
        super(ResultCode.BAD_CONFIGURATION);
    }

    /**
     * 生成一个带有错误信息的的业务异常
     *
     * @param message 错误信息
     */
    public BadConfigurationException(String message) {
        super(ResultCode.BAD_CONFIGURATION, message);
    }
}
