package com.github.stazxr.zblog.core.util;

import com.github.stazxr.zblog.core.cache.Cache;

/**
 * 系统内置缓存工具类
 *
 * @author SunTao
 * @since 2022-03-02
 */
public class CacheUtils {
    private static Cache cache;

    private static Cache instance() {
        if (CacheUtils.cache == null) {
            CacheUtils.cache = SpringContextUtils.getBean(Cache.class);
        }
        return CacheUtils.cache;
    }

    public static Cache cache() {
        return instance();
    }
}
