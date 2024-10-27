package com.github.stazxr.zblog.core.annotation;

import com.github.stazxr.zblog.core.base.BaseConst;

import java.lang.annotation.*;

/**
 * 标注路由信息
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
	 * @return String
	 */
	String name();

	/**
	 * 权限编码：受控权限需要具有对应的编码权限才能访问
	 *
	 * @return String
	 */
	String code();

	/**
	 * 权限级别
	 * {@link BaseConst.PermLevel#OPEN} 公开权限，不需要认证授权即可访问
	 * {@link BaseConst.PermLevel#PUBLIC} 公共权限，认证即可访问
	 * {@link BaseConst.PermLevel#PERM} 受控权限，授权即可访问
	 *
	 * @return int
	 */
	int level() default BaseConst.PermLevel.PERM;

	/**
	 * 路由备注
	 *
	 * @return String
	 */
	String remark() default "";
}
