package com.github.stazxr.zblog.log.annotation;

import java.lang.annotation.*;

/**
 * 标注日志，忽略记录接口日志信息
 *
 * @author SunTao
 * @since 2021-09-26
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoredLog {
}
