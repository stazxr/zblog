package com.github.stazxr.zblog.core.base;

/**
 * 全局常量类
 *
 * @author SunTao
 * @since 2020-11-16
 */
public class BaseConst {
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
        public static final String V_4_0_0 = "4.0";

        /**
         * V_5.0.0
         */
        public static final String V_5_0_0 = "5.0";

        /**
         * V_P_1.0.0（门户版本）
         */
        public static final String V_P_1_0_0 = "P1.0";
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
