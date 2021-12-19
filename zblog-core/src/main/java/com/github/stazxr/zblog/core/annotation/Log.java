package com.github.stazxr.zblog.core.annotation;

import com.github.stazxr.zblog.core.enums.LogLevel;
import com.github.stazxr.zblog.core.enums.LogType;

import java.lang.annotation.*;

/**
 * 标注日志信息
 *
 * @author SunTao
 * @since 2021-05-16
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Log {
    /**
     * 日志名称
     *
     * @return String
     */
    String name();

    /**
     * 日志类型
     *
     * @return OperateType
     */
    LogType type();

    /**
     * 日志级别
     *
     * @return OperateLevel
     */
    LogLevel level() default LogLevel.NOTICE;
}
