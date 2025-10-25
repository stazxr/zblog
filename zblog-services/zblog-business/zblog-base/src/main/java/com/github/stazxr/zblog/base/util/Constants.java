package com.github.stazxr.zblog.base.util;

/**
 * 全局常量类
 *
 * @author SunTao
 * @since 2020-11-16
 */
public class Constants {
    /**
     * 超级管理员ID
     */
    public static final Long SUPER_USER_ID = 1L;

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
    public static final String TOP_PERM_NAME = "Z-BLOG";

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
     * AUTHENTICATION_PREFIX
     */
    public static final String AUTHENTICATION_PREFIX = "Bearer ";

    /**
     * 默认排序
     */
    public static final int DEFAULT_SORT = 99999;

    /**
     * 否
     */
    public static final Integer FALSE = 0;

    /**
     * 是
     */
    public static final Integer TRUE = 1;

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

    /**
     * Cache Key
     *
     * @author SunTao
     * @since 2020-11-14
     */
    public enum SysCacheKey {
        /**
         * 缓存接口访问级别
         */
        interfaceLevel("intLevel:%s", 3600),

        /**
         * token
         */
        usrTkn("usrTkn", 1800),

        /**
         * ssoTkn
         */
        ssoTkn("ssoTkn:%s", 604800),

        /**
         * preTkn
         */
        preTkn("preTkn:%s", -1),

        /**
         * email code
         */
        emailCode("emailCode:%s:%s", 300);

        /**
         * 缓存Key
         */
        private final String cacheKey;

        /**
         * 缓存有效时间，单位秒
         */
        private final int duration;

        SysCacheKey(String cacheKey, int duration) {
            this.cacheKey = cacheKey;
            this.duration = duration;
        }

        public String cacheKey() {
            return cacheKey;
        }

        public int duration() {
            return duration;
        }
    }
}
