package com.github.stazxr.zblog.bas.security.authz;

/**
 * DefaultRoleCode 定义了一组系统默认的角色代码常量，用于控制资源的访问权限。
 * <p>这些角色代码用于标记特定资源的访问条件，如是否允许匿名访问、禁止访问等。</p>
 *
 * @author SunTao
 * @since 2024-11-13
 */
public abstract class DefaultRoleCode {
    /**
     * 角色代码：ROLE_NO_TEST - 标记资源不允许测试用户访问。
     */
    public static final String NO_TEST = "ROLE_NO_TEST";

    /**
     * 角色代码：ROLE_OPEN - 标记资源允许匿名访问。
     */
    public static final String OPEN = "ROLE_OPEN";

    /**
     * 角色代码：ROLE_PUBLIC - 标记资源允许登录后访问。
     */
    public static final String PUBLIC = "ROLE_PUBLIC";

    /**
     * 角色代码：ROLE_FORBIDDEN - 标记资源被禁止访问，只有内置管理员可以访问。
     */
    public static final String FORBIDDEN = "ROLE_FORBIDDEN";

    /**
     * 角色代码：ROLE_NONE - 标记资源无访问角色，只有内置管理员可以访问。
     */
    public static final String NONE = "ROLE_NONE";

    /**
     * 角色代码：ROLE_NULL - 标记资源不存在。
     */
    public static final String NULL = "ROLE_NULL";
}
