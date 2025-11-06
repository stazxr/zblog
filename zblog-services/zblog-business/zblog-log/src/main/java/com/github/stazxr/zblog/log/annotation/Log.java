package com.github.stazxr.zblog.log.annotation;

import java.lang.annotation.*;

/**
 * 操作日志标注
 *
 * @author SunTao
 * @since 2021-05-16
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String value() default "";
}
