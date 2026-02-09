package com.github.stazxr.zblog.bas.ratelimit;

import com.github.stazxr.zblog.bas.exception.ServiceException;
import com.github.stazxr.zblog.bas.exception.code.ErrorCode;

/**
 * 限流异常
 *
 * @author SunTao
 * @since 2025-02-09
 */
public class RateLimitException extends ServiceException {
    private static final long serialVersionUID = -3570701293314015542L;

    /**
     * 构造业务异常（带国际化参数）
     *
     * @param errorCode 错误码定义
     * @param args      国际化消息参数
     */
    public RateLimitException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
