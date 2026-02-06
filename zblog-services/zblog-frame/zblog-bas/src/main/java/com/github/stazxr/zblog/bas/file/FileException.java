package com.github.stazxr.zblog.bas.file;

import com.github.stazxr.zblog.bas.exception.SystemException;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 自定义文件异常类，用于处理文件上传下载相关的错误。
 *
 * @author SunTao
 * @since 2024-10-24
 */
public class FileException extends SystemException {
    private static final long serialVersionUID = -3311095449947924617L;

    /**
     * 构造技术异常（带国际化参数）
     *
     * @param errorCode 错误码定义
     * @param args      国际化消息参数
     */
    public FileException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    /**
     * 构造基于错误码的技术异常，并指定 cause
     *
     * @param errorCode 错误码定义
     * @param cause     原始异常
     * @param args      国际化消息参数
     */
    public FileException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode, cause, args);
    }
}
