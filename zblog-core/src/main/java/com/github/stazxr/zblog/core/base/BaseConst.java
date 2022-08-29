package com.github.stazxr.zblog.core.base;

/**
 * 全局常量类
 *
 * @author SunTao
 * @since 2020-11-16
 */
public class BaseConst {
    /**
     * 系统名称
     */
    public static final String SYS_NAME = "ZBLOG后台管理系统";

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
    public static final class DictId {
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
    public static final class DictKey {
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
     * 接口访问级别
     */
    public static final class PermLevel {
        private PermLevel() {
            throw new IllegalStateException();
        }

        /**
         * 公开权限，直接访问
         */
        public static final int OPEN = 1;

        /**
         * 公共权限，认证即可访问
         */
        public static final int PUBLIC = 2;

        /**
         * 受控权限，授权即可访问
         */
        public static final int PERM = 3;
    }

    /**
     * 扩展的接口访问级别,特殊用途
     */
    public static final class PermLevelExtend {
        private PermLevelExtend() {
            throw new IllegalStateException();
        }

        /**
         * 禁止访问
         */
        public static final int FORBIDDEN = 99;

        /**
         * 未配置@Router注解,接口不对外,不允许访问
         */
        public static final int NULL = 98;
    }
}
