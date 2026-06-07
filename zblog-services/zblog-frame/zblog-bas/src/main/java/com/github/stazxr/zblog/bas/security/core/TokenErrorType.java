package com.github.stazxr.zblog.bas.security.core;

/**
 * 令牌错误码类型
 *
 * @author SunTao
 * @since 2026-06-07
 */
public enum TokenErrorType {
    /**
     * 前端为上送访问令牌（可能过期也可能未登录，前端需特殊处理）
     */
    TET_001,

    /**
     * 未登录，前端直接跳转登录页
     */
    TET_002,

    /**
     * 账号状态异常，前端对前提示错误消息，然后调用登出并跳转登录页
     */
    TET_003,

    /**
     * 登录失效（永久），前端跳转登录页
     */
    TET_004,

    /**
     * 登录失效（可续签），前端调用续签接口
     */
    TET_005,

    /**
     * 系统异常，跳转登录页
     */
    TET_006
}