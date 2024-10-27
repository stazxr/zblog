package com.github.stazxr.zblog.bas.log.cache;

import com.github.stazxr.zblog.bas.log.properties.LogControlProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Manager for controlling logging based on request paths.
 * This component initializes and manages the path cache for logging.
 *
 * @author SunTao
 * @since 2024-05-19
 */
@Slf4j
@Component
public class LogPathCacheManager {
    private LogControlProperties logControlProperties;

    /**
     * Initializes the cache during bean initialization.
     */
    @PostConstruct
    public void initCache() {
        LogPathCache.clearCache();
    }

    /**
     * Determines if logging is enabled for the given path.
     *
     * @param path Request path
     * @return boolean true if logging is enabled, false otherwise
     */
    public boolean enabledLog(String path) {
        try {
            return LogPathCache.allowedLogWithCache(path, logControlProperties.getExcludePath());
        } catch (Exception e) {
            log.error("LogControlPathCacheManager -> LogControlPathCache error", e);
            return false;
        }
    }

    /**
     * Sets the LogControlProperties instance via dependency injection.
     *
     * @param logControlProperties Log control properties
     */
    @Autowired
    public void setLogControlProperties(LogControlProperties logControlProperties) {
        this.logControlProperties = logControlProperties;
    }
}
