package com.github.stazxr.zblog.util.exception;

/**
 * Assert校验失败异常
 *
 * @author SunTao
 * @since 2022-07-12
 */
public class AssertionViolatedException extends RuntimeException {
    private static final long serialVersionUID = -296455436186838477L;

    /**
     * 构造异常对象
     *
     * @param message 错误信息
     */
    public AssertionViolatedException(String message) {
        super(message);
    }
}
