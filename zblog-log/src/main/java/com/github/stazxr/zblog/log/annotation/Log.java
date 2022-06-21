package com.github.stazxr.zblog.log.annotation;

import java.lang.annotation.*;

/**
 * 标注日志
 *
 *  在方法上使用此注解，将会记录执行信息和异常信息
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
