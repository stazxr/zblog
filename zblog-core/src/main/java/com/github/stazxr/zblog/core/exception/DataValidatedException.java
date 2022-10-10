package com.github.stazxr.zblog.core.exception;

import com.github.stazxr.zblog.core.enums.ResultCode;

/**
 * 数据校验异常，业务校验失败，如角色编码已存在等
 *
 * @author SunTao
 * @since 2022-08-27
 */
public class DataValidatedException extends ServiceException {
    /**
     * 生成一个默认配置信息错误异常
     */
    public DataValidatedException() {
        super(ResultCode.PARAM_VALID);
    }

    /**
     * 生成一个带有错误信息的的业务异常
     *
     * @param message 错误信息
     */
    public DataValidatedException(String message) {
        super(ResultCode.PARAM_VALID, message);
    }
}
