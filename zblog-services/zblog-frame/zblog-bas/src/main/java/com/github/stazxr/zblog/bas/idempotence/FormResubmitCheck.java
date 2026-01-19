package com.github.stazxr.zblog.bas.idempotence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表单重复提交校验
 *
 * @author SunTao
 * @since 2023-12-10
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormResubmitCheck {
    /**
     * 提示信息
     *
     * @return String
     */
    String value() default "系统繁忙，请稍后再试";

    /**
     * 是否全局校验（false 校验时会携带用户信息）
     *
     * @return boolean
     */
    boolean global() default true;

    /**
     * 禁止重复提交的时间
     *
     * @return int 秒
     */
    int timeout() default 1;
}
