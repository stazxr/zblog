package com.github.stazxr.zblog.bas.idempotence;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 幂等注解
 *
 * @author SunTao
 * @since 2026-02-02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {
    /**
     * 幂等 key（支持 SpEL）
     */
    String key();

    /**
     * 过期时间
     */
    long expire() default 5;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 重复提交提示
     */
    String message() default "error.system.busy";
}
