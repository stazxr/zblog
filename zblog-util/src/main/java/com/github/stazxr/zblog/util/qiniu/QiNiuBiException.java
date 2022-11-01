package com.github.stazxr.zblog.util.qiniu;

/**
 * 七牛云相关异常封装
 *
 * @author SunTao
 * @since 2022-10-31
 */
public class QiNiuBiException extends RuntimeException {
    /**
     * 构造异常对象
     *
     * @param message 错误信息
     */
    public QiNiuBiException(String message) {
        super(message);
    }

    /**
     * 构造异常对象
     *
     * @param message 错误信息
     * @param cause   异常信息
     */
    public QiNiuBiException(String message, Throwable cause) {
        super(message, cause);
    }
}
