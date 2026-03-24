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
    public static final String SYSTEM_USER = "system";

    /**
     * system userId
     */
    public static final Long SYSTEM_USER_ID = 1L;

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

        /**
         * 文件存储类型
         */
        public static final String ACTIVE_UPLOAD_TYPE = "ACTIVE_UPLOAD_TYPE";

        /**
         * 文件上传白名单
         */
        public static final String FILE_UPLOAD_WHITE_LIST = "FILE_UPLOAD_WHITE_LIST";

        /**
         * 云存储配置-本地
         */
        public static final String CLOUD_STORAGE_LOCAL_CONFIG = "CLOUD_STORAGE_LOCAL_CONFIG";

        /**
         * 云存储配置-阿里云
         */
        public static final String CLOUD_STORAGE_A_LI_CONFIG = "CLOUD_STORAGE_A_LI_CONFIG";

        /**
         * 云存储配置-七牛云
         */
        public static final String CLOUD_STORAGE_QI_NIU_CONFIG = "CLOUD_STORAGE_QI_NIU_CONFIG";
    }

    /**
     * 接口访问级别
     *
     * 该类将会被删除，使用 bas 框架的 RouterLevel 替换
     */
    @Deprecated
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
        public static final int PUBLIC = 1 << 1;

        /**
         * 受控权限，授权即可访问
         */
        public static final int PERM = 1 << 2;
    }

    /**
     * 扩展的接口访问级别，特殊用途
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
         * 接口编码没有对应的路由配置
         */
        public static final int NULL = 98;
    }

    /**
     * 操作类型
     */
    public static final class Action {
        private Action() {
            throw new IllegalStateException();
        }

        /**
         * 新增
         */
        public static final String ADD = "add";

        /**
         * 编辑
         */
        public static final String EDIT = "edit";

        /**
         * 删除
         */
        public static final String DELETE = "delete";
    }

    /**
     * API 版本
     */
    public static final class ApiVersion {
        private ApiVersion() {
            throw new IllegalStateException();
        }

        /**
         * V_4.0.0
         */
        public static final String V_4_0_0 = "v4";

        /**
         * V_4.1.0
         */
        public static final String V_4_1_0 = "v4_1";

        /**
         * V_4.2.0
         */
        public static final String V_4_2_0 = "v4_2";

        /**
         * V_5.0.0
         */
        public static final String V_5_0_0 = "v5";
    }

    /**
     * 全局缓存键值
     */
    public enum GlobalCacheKey {
        /**
         * 资源信息
         *
         * 1、当资源等级变更时（权限的新增，编辑和删除），需要更新缓存的资源信息
         * 2、当修改源码时，需要注意清理缓存中的资源信息
         */
        resource("RESOURCE:%s_%s", 24 * 60 * 60 * 1000),

        /**
         * 资源对应的角色清单
         *
         * 角色授权，权限变更需要更新缓存
         */
        resourceRoles("RESOURCE_ROLES:%s_%s", 24 * 60 * 60 * 1000);

        /**
         * 缓存Key
         */
        private final String key;

        /**
         * 缓存有效时间，单位秒
         */
        private final int duration;

        GlobalCacheKey(String key, int duration) {
            this.key = key;
            this.duration = duration;
        }

        public String getKey() {
            return key;
        }

        public int duration() {
            return duration;
        }
    }
}
