package com.github.stazxr.zblog.bas.router;

import java.lang.annotation.*;

/**
 * API版本控制注解
 *
 * @author SunTao
 * @since 2026-02-03
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ApiVersion {
    /**
     * 版本
     */
    String value();

    /**
     * 是否为废弃版本
     * true 表示该版本即将移除
     */
    boolean deprecated() default false;

    /**
     * 废弃说明（可选）
     */
    String deprecatedDesc() default "";

    /**
     * 接口版本说明（可选）
     */
    String description() default "";
}
