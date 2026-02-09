package com.github.stazxr.zblog.bas.ratelimit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流注解
 *
 * @author SunTao
 * @since 2025-02-09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    /**
     * 时间窗口（秒）
     */
    int time() default 1;

    /**
     * 最大访问次数
     */
    int count() default 1;

    /**
     * 提示消息
     */
    String message() default "";

    /**
     * 是否启用 IP 限流
     */
    boolean enableIp() default false;

    /**
     * 是否启用用户限流
     */
    boolean enableUser() default false;

    /**
     * 是否启用接口限流
     */
    boolean enableApi() default false;
}
