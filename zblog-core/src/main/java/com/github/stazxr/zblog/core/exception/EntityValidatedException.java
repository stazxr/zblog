package com.github.stazxr.zblog.core.exception;

import com.github.stazxr.zblog.core.enums.ResultCode;

/**
 * 实体类校验异常
 *
 * @author SunTao
 * @since 2022-08-27
 */
public class EntityValidatedException extends ServiceException {
    /**
     * 生成一个默认配置信息错误异常
     */
    public EntityValidatedException() {
        super(ResultCode.PARAM_VALID);
    }

    /**
     * 生成一个带有错误信息的的业务异常
     *
     * @param message 错误信息
     */
    public EntityValidatedException(String message) {
        super(ResultCode.PARAM_VALID, message);
    }
}
