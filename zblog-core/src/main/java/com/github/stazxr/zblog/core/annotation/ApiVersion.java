package com.github.stazxr.zblog.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API 版本
 *
 * @author SunTao
 * @since 2022-03-20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiVersion {
     /**
      * 分组
      *
      * @return group
      */
     String[] group();
}
