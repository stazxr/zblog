package com.github.stazxr.zblog.core.annotation;

import java.lang.annotation.*;

/**
 * 标注不需要统一封装的接口
 *
 * @author SunTao
 * @since 2022-02-07
 */
@Documented
@Inherited
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResult {
}
