package com.github.stazxr.zblog.core.base;

/**
 * 全局常量类
 *
 * @author SunTao
 * @since 2020-11-16
 */
public class BaseConst {
    /**
     * 系统根包路径
     */
    public static final String BASE_PACKAGE = "com.github.stazxr.zblog";

    /**
     * system username
     */
    public static final String USER_SYSTEM = "SYSTEM";

    /**
     * admin username
     */
    public static final String USER_ADMIN = "ADMIN";

    /**
     * 匿名用户
     */
    public static final String ANONYMITY_USERNAME = "匿名用户";

    /**
     * 系统初始化内置字典ID
     */
    public static class DictId {
        private DictId() {
            throw new IllegalStateException();
        }

        /**
         * 系统参数字典ID
         */
        public static final Long SYS = 1L;

        /**
         * 系统参数字典ID
         */
        public static final Long WORK_ID = 1314L;
    }

    /**
     * 系统初始化内置字典Key
     */
    public static class DictKey {
        private DictKey() {
            throw new IllegalStateException();
        }

        /**
         * 路由白名单
         */
        public static final String ROUTER_WHITE_LIST = "routerWhiteList";

        /**
         * 路由黑名单
         */
        public static final String ROUTER_BLACK_LIST = "routerBlackList";
    }

    /**
     * 权限级别
     */
    public static class PermLevel {
        private PermLevel() {
            throw new IllegalStateException();
        }

        /**
         * 公开权限，不需要认证授权即可访问
         */
        public static final int OPEN = 1;

        /**
         * 公共权限，认证即可访问
         */
        public static final int PUBLIC = 2;

        /**
         * 公共权限，认证即可访问
         */
        public static final int PERM = 3;
    }
}
