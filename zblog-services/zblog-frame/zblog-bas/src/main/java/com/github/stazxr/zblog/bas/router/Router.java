package com.github.stazxr.zblog.bas.router;

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
	 * 路由编码：受控级别的路由 {@link com.github.stazxr.zblog.bas.router.RouterLevel#PERM} 需要具有对应的编码权限才能访问
	 *
	 * @return String 路由编码（服务内唯一）
	 */
	String code();

	/**
	 * 路由访问级别
	 *
	 * <li>{@link com.github.stazxr.zblog.bas.router.RouterLevel#OPEN} 公开权限，不需要认证授权即可访问</li>
	 * <li>{@link com.github.stazxr.zblog.bas.router.RouterLevel#PUBLIC} 公共权限，认证即可访问</li>
	 * <li>{@link com.github.stazxr.zblog.bas.router.RouterLevel#PERM} 受控权限，授权即可访问</li>
	 *
	 * @return int 权限级别
	 */
	int level() default RouterLevel.PERM;

	/**
	 * 额外信息，只开发用，不做页面展示
	 *
	 * @return 接口额外信息
	 */
	String remark() default "";
}
