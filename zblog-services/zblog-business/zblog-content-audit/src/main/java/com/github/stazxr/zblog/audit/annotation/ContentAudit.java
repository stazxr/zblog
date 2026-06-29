package com.github.stazxr.zblog.audit.annotation;

import java.lang.annotation.*;

/**
 * 内容审核注解。
 *
 * 标注的方法在执行时需要进行内容审核。
 *
 * 后续可结合 AOP 实现自动审核。
 *
 * @author SunTao
 * @since 2026-06-29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ContentAudit {
}
