package com.github.stazxr.zblog.log.annotation;

import java.lang.annotation.*;

/**
 * 注解标注的方法不记录切面日志
 *
 * @author SunTao
 * @since 2021-09-26
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreLog {
}
