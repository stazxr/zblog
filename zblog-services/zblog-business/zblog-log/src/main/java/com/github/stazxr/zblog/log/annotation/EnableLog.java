package com.github.stazxr.zblog.log.annotation;

import com.github.stazxr.zblog.log.autoconfigure.EnableLogMarker;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 切面日志开关
 *
 * @author SunTao
 * @since 2022-06-20
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableLogMarker.class)
public @interface EnableLog {
}
