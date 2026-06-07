package com.github.stazxr.zblog.bas.rest;

/**
 * 响应类型
 *
 * 额外扩展：com.github.stazxr.zblog.bas.security.core.TokenErrorType
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
}
