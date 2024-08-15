package com.github.stazxr.zblog.props;

import com.github.stazxr.zblog.common.exception.BaseException;

/**
 * 属性源加载异常类，用于表示在加载属性源时发生的异常情况。
 *
 * @author SunTao
 * @since 2024-07-25
 */
public class PropertySourceLoadException extends BaseException {
    private static final long serialVersionUID = 458776512891696908L;

    /**
     * 构造一个带有指定详细消息和根本原因的异常。
     *
     * @param message 异常的详细消息
     * @param cause 引起此异常的原因
     */
    public PropertySourceLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
