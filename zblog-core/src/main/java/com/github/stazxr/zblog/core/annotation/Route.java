package com.github.stazxr.zblog.core.annotation;

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
public @interface Route {
	/**
	 * 路由名称
	 *
	 * @return String
	 */
	String name();

	/**
	 * 权限编码
	 *
	 * @return String
	 */
	String permCode();
}
