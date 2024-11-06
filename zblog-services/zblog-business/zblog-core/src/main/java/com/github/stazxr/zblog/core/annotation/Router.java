package com.github.stazxr.zblog.core.annotation;

import com.github.stazxr.zblog.core.base.BaseConst;

import java.lang.annotation.*;

/**
 * 路由注解，用于标注接口
 *
 * @author SunTao
 * @since 2020-11-14
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Router {
	/**
	 * 路由名称
	 *
	 * @return String 路由名称
	 */
	String name();

	/**
	 * 路由编码：受控级别的路由需要具有对应的编码权限才能访问
	 *
	 * @return String 路由编码（唯一）
	 */
	String code();

	/**
	 * 路由级别
	 * {@link BaseConst.PermLevel#OPEN} 公开权限，不需要认证授权即可访问
	 * {@link BaseConst.PermLevel#PUBLIC} 公共权限，认证即可访问
	 * {@link BaseConst.PermLevel#PERM} 受控权限，授权即可访问
	 *
	 * @return int 权限级别
	 */
	int level() default BaseConst.PermLevel.PERM;

	/**
	 * 路由备注
	 *
	 * @return String 路由备注
	 */
	String remark() default "";
}
