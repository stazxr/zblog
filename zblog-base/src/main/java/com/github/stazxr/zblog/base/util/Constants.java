package com.github.stazxr.zblog.base.util;

import com.github.stazxr.zblog.core.config.properties.CacheConfigProperties;

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
    public static final Long TOP_PERM_ID = 0L;

    /**
     * 权限树中顶层权限名称
     */
    public static final String TOP_PERM_NAME = "Z-BLOG";

    /**
     * 系统管理员
     */
    public static final String USER_ADMIN = "admin";

    /**
     * 测试用户
     */
    public static final String USER_TEST = "test";

    /**
     * 系统管理员序列
     */
    public static final Long USER_ADMIN_ID = 1L;

    /**
     * 测试用户序列
     */
    public static final Long USER_TEST_ID = 2L;

    /**
     * 缓存不设置过期时间
     */
    public static final int FOREVER_CACHE = CacheConfigProperties.FOREVER_CACHE;

    /**
     * AUTHENTICATION_PREFIX
     */
    public static final String AUTHENTICATION_PREFIX = "Bearer ";

    /**
     * 系统默认用户
     */
    public static final long DEFAULT_ROLE_ID = 3561010960472735744L;

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
    public enum CacheKey {
        /**
         * 登录验证码
         */
        @Deprecated
        loginNumCode("loginNumCode", 300),

        /**
         * 系统内置的验证码配置信息
         */
        captchaConfig("captchaConfig", FOREVER_CACHE),

        /**
         * 登录验证码缓存的Key值，有效时间已配置文件为准，配置文件未配置则已此处为准
         */
        loginCode("loginCode", 90),

        /**
         * 缓存接口访问级别
         */
        interfaceLevel("interfaceLevel", 3600),

        /**
         * token
         */
        usrTkn("usrTkn", 1800),

        /**
         * email code
         */
        emailCode("emailCode", 300);

        /**
         * 缓存Key
         */
        private final String cacheKey;

        /**
         * 缓存有效时间，单位秒
         */
        private final int duration;

        CacheKey(String cacheKey, int duration) {
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
