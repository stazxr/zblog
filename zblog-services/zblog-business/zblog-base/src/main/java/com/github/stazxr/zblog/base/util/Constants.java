package com.github.stazxr.zblog.base.util;

/**
 * 全局常量类
 *
 * @author SunTao
 * @since 2020-11-16
 */
public class Constants {
    /**
     * 树结构的顶部序列
     */
    public static final Long TOP_ID = 0L;

    /**
     * 权限树中顶层权限的ID
     */
    public static final Long TOP_PERM_ID = TOP_ID;

    /**
     * 权限树中顶层权限名称
     */
    public static final String TOP_PERM_NAME = "顶级菜单";

    /**
     * 一时的秒数
     */
    public static final int ONE_HOUR_SEC_UNIT = 3600;

    /**
     * 一天的秒数
     */
    public static final int ONE_DAY_SEC_UNIT = ONE_HOUR_SEC_UNIT * 24;

    /**
     * 一周的秒数
     */
    public static final int ONE_WEEK_SEC_UNIT = ONE_DAY_SEC_UNIT * 7;

    /**
     * 内置安全系统的角色
     */
    public static final class SecurityRole {
        private SecurityRole() {
            throw new IllegalStateException();
        }

        /**
         * 角色：ROLE_OPEN（权限包含该角色，代表权限可以匿名访问）
         */
        public static final String OPEN = "ROLE_OPEN";

        /**
         * 角色：ROLE_PUBLIC（权限包含该角色，代表权限登录后就可以访问）
         */
        public static final String PUBLIC = "ROLE_PUBLIC";

        /**
         * 角色：ROLE_FORBIDDEN（权限包含该角色，代表权限被禁止访问，只有内置管理员可以访问）
         */
        public static final String FORBIDDEN = "ROLE_FORBIDDEN";

        /**
         * 角色：ROLE_NONE（不存在角色可以访问该接口）
         */
        public static final String NONE = "ROLE_NONE";

        /**
         * 角色：ROLE_NULL（接口不存在，不允许访问）
         */
        public static final String NULL = "ROLE_NULL";

        /**
         * 角色：ROLE_NO_TEST（此角色标记的资源不允许测试用户访问）
         */
        public static final String NO_TEST = "ROLE_NO_TEST";
    }
}
