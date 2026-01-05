package com.github.stazxr.zblog.bas.file;

import com.github.stazxr.zblog.bas.exception.BaseException;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;

/**
 * 自定义文件异常类，用于处理文件上传下载相关的错误。
 *
 * @author SunTao
 * @since 2024-10-24
 */
public class FileException extends BaseException {
    private static final long serialVersionUID = -3311095449947924617L;

    /**
     * 使用指定的详细消息构造一个新的 FileException 实例。
     *
     * @param message 详细消息
     */
    public FileException(String message) {
        super(message);
    }

    /**
     * 使用指定的详细消息和原因构造一个新的 FileException 实例。
     *
     * @param message 详细消息
     * @param cause 引起此异常的原因，允许为 null，表示原因不存在或未知
     */
    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用国际化消息码构造异常
     *
     * @param expMessageCode 异常消息码
     */
    public FileException(ExpMessageCode expMessageCode) {
        super(expMessageCode);
    }

    /**
     * 使用国际化消息码及根因构造异常
     *
     * @param expMessageCode 异常消息码
     * @param cause          根因异常
     */
    public FileException(ExpMessageCode expMessageCode, Throwable cause) {
        super(expMessageCode, cause);
    }
}

