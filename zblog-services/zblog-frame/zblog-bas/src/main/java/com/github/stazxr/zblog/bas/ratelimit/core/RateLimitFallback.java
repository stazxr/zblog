package com.github.stazxr.zblog.bas.ratelimit.core;

/**
 * 限流降级策略
 *
 * @author SunTao
 * @since 2026-08-27
 */
public enum RateLimitFallback {
    /**
     * 拒绝请求
     * <p>
     * 触发限流后抛出限流异常，由全局异常处理返回限流提示。
     * </p>
     */
    REJECT,

    /**
     * 静默成功
     * <p>
     * 触发限流后不执行原业务方法，直接返回成功结果。
     * 适用于访问记录、点击统计、埋点等非核心业务。
     * </p>
     */
    SUCCESS
}
