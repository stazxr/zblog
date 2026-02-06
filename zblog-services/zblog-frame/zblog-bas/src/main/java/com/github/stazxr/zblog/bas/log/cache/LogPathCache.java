package com.github.stazxr.zblog.bas.log.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志路径缓存工具类.
 *
 * @author SunTao
 * @since 2024-05-19
 */
public class LogPathCache {

    private static final Logger log = LoggerFactory.getLogger(LogPathCache.class);

    /**
     * 最大缓存数量，防止OOM
     */
    private static final int MAX_CACHE_SIZE = 1024;

    /**
     * 允许打印日志路径缓存
     */
    private static final Map<String, Boolean> INCLUDE_CACHE = new ConcurrentHashMap<>();

    /**
     * 禁止打印日志路径缓存
     */
    private static final Map<String, Boolean> EXCLUDE_CACHE = new ConcurrentHashMap<>();

    /**
     * Spring路径匹配器
     */
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 判断是否允许打印日志（带缓存）
     */
    public static boolean allowedLogWithCache(String path, int model, List<String> paths) {
        // 命中排除缓存
        if (EXCLUDE_CACHE.containsKey(path)) {
            return false;
        }

        // 命中允许缓存
        if (INCLUDE_CACHE.containsKey(path)) {
            return true;
        }

        boolean matched = false;
        for (String pattern : paths) {
            if (PATH_MATCHER.match(pattern, path)) {
                matched = true;
                break;
            }
        }

        boolean allowed;
        if (model == 0) {
            // model = 0 → 白名单模式
            allowed = matched;
        } else {
            // model = 1 → 黑名单模式
            allowed = !matched;
        }

        cacheResult(path, allowed);
        return allowed;
    }

    /**
     * 缓存结果并限制大小
     */
    private static void cacheResult(String path, boolean allowed) {
        Map<String, Boolean> target = allowed ? INCLUDE_CACHE : EXCLUDE_CACHE;
        if (target.size() > MAX_CACHE_SIZE) {
            log.debug("日志路径缓存已满，清空缓存");
            clearCache();
        }
        target.put(path, Boolean.TRUE);
    }

    /**
     * 清空缓存
     */
    public static void clearCache() {
        INCLUDE_CACHE.clear();
        EXCLUDE_CACHE.clear();
    }
}
