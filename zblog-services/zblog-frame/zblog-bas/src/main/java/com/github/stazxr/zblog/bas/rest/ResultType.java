package com.github.stazxr.zblog.bas.rest;

/**
 * 响应类型
 *
 * @author SunTao
 * @since 2026-06-01
 */
public interface ResultType {
    /**
     * 成功
     */
    String SUCCESS = "ST000000";

    /**
     * 登录失败
     */
    String LOGIN_FAILED = "ST000001";

    /**
     * 未登录
     */
    String UNAUTHORIZED = "ST000002";

    /**
     * Token过期
     */
    String TOKEN_EXPIRED = "TOKEN_EXPIRED";

    /**
     * Token无效
     */
    String TOKEN_INVALID = "TOKEN_INVALID";

    /**
     * 权限不足
     */
    String FORBIDDEN = "FORBIDDEN";

    /**
     * 参数异常
     */
    String VALIDATE_ERROR = "VALIDATE_ERROR";

    /**
     * 系统异常
     */
    String SYSTEM_ERROR = "SYSTEM_ERROR";
}