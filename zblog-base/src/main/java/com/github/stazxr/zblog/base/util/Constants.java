package com.github.stazxr.zblog.base.util;

/**
 * 全局常量类
 *
 * @author SunTao
 * @since 2020-11-16
 */
public class Constants {
    /**
     * 权限树中顶层权限的ID
     */
    public static final Long TOP_MENU_ID = 0L;

    /**
     * 权限树中顶层权限名称
     */
    public static final String TOP_MENU_NAME = "顶层菜单";

    /**
     * 系统管理员
     */
    public static final String USER_ADMIN = "admin";

    /**
     * 缓存不设置过期时间
     */
    public static final int FOREVER_CACHE = 0;

    /**
     * 路由状态
     */
    public static final class RouterStatus {
        private RouterStatus() {
            throw new IllegalStateException();
        }

        /**
         * 路由未纳管
         */
        public static final String NONE = "none";

        /**
         * 路由被禁用，只有管理员可以访问
         */
        public static final String DISABLED = "disabled";

        /**
         * 路由状态正常
         */
        public static final String OK = "ok";
    }

    /**
     * 内置安全系统的角色
     */
    public static final class SecurityRole {
        private SecurityRole() {
            throw new IllegalStateException();
        }

        /**
         * 角色：ROLE_OPEN
         */
        public static final String OPEN = "ROLE_OPEN";

        /**
         * 角色：ROLE_PUBLIC
         */
        public static final String PUBLIC = "ROLE_PUBLIC";

        /**
         * 角色：ROLE_NONE
         */
        public static final String NONE = "ROLE_NONE";

        /**
         * 角色：ROLE_FORBIDDEN
         */
        public static final String FORBIDDEN = "ROLE_FORBIDDEN";
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
         * 登录验证码缓存的Key值，有效时间已配置文件为准
         */
        loginCode("loginCode", -1);

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
