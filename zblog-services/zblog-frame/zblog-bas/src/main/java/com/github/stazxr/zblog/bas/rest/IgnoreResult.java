package com.github.stazxr.zblog.bas.rest;

import java.lang.annotation.*;

/**
 * 标记接口或方法忽略统一响应体包装。
 *
 * <p>当接口方法上使用此注解时，`ResponseBodyAdvice` 将不会将返回值自动包装为 {@link Result}。</p>
 *
 * <p>使用场景示例：</p>
 * <ul>
 *     <li>第三方回调接口，不希望返回统一格式</li>
 *     <li>返回特殊数据格式（如文件流、图片、Excel 等）</li>
 * </ul>
 *
 * <p>注意：标注后，接口返回值不会进行 Result 包装，
 * 但仍可使用 {@link com.github.stazxr.zblog.bas.exception.handler.GlobalExceptionHandler} 处理异常。</p>
 *
 * @author SunTao
 * @since 2026-01-25
 */
@Documented
@Inherited
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResult {
}
