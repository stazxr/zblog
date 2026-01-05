package com.github.stazxr.zblog.core.exception;

/**
 * 数据校验异常，业务校验失败，如角色编码已存在等
 *
 * @author SunTao
 * @since 2022-08-27
 */
@Deprecated
public class DataValidatedException extends RuntimeException {
    private static final long serialVersionUID = 1641043720556318233L;

    /**
     * 生成一个带有错误信息的的业务异常
     *
     * @param message 错误信息
     */
    public DataValidatedException(String message) {
    }
}
