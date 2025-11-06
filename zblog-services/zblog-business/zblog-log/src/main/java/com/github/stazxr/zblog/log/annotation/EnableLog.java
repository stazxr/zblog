package com.github.stazxr.zblog.log.annotation;

import com.github.stazxr.zblog.log.annotation.properties.LogProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 日志组件开关
 *
 * @author SunTao
 * @since 2022-06-20
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ LogProperties.class })
public @interface EnableLog {
}
