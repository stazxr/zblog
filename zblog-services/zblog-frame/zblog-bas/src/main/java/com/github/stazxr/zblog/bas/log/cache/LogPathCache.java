package com.github.stazxr.zblog.bas.log.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache for controlling logging based on request paths.
 * This class manages inclusion and exclusion of paths for.
 *
 * @author SunTao
 * @since 2024-05-19
 */
@Slf4j
public class LogPathCache {
    /**
     * Map of paths that are included for logging.
     */
    private static final Map<String, Date> PATH_INCLUDE_MAP = new ConcurrentHashMap<>(16);

    /**
     * Map of paths that are excluded from logging.
     */
    private static final Map<String, Date> PATH_EXCLUDE_MAP = new ConcurrentHashMap<>(16);

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * Determines if logging is allowed for the given path, considering exclusion list.
     *
     * @param path   Request path
     * @param model  the log model
     * @param paths  List of paths to enabled/exclude from logging
     * @return boolean true if logging is allowed, false otherwise
     */
    public static boolean allowedLogWithCache(String path, int model, List<String> paths) {
        if (PATH_EXCLUDE_MAP.containsKey(path)) {
            return false;
        }

        if (PATH_INCLUDE_MAP.containsKey(path)) {
            return true;
        }

        for (String path0 : paths) {
            if (PATH_MATCHER.match(path0, path)) {
                if (model == 0) {
                    PATH_INCLUDE_MAP.put(path, new Date());
                    log.debug("log control path cache in include map: {}", path);
                    return true;
                } else {
                    PATH_EXCLUDE_MAP.put(path, new Date());
                    log.debug("log control path cache in exclude map: {}", path);
                    return false;
                }
            }
        }

        if (model == 0) {
            PATH_EXCLUDE_MAP.put(path, new Date());
            log.debug("log control path cache in exclude map: {}", path);
            return false;
        } else {
            PATH_INCLUDE_MAP.put(path, new Date());
            log.debug("log control path cache in include map: {}", path);
            return true;
        }
    }

    /**
     * Clears the path cache.
     */
    public static void clearCache() {
        PATH_INCLUDE_MAP.clear();
        PATH_EXCLUDE_MAP.clear();
    }
}
