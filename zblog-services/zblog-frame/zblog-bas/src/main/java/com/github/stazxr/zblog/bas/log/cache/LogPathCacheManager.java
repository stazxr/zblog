package com.github.stazxr.zblog.bas.log.cache;

import com.github.stazxr.zblog.bas.log.autoconfigure.properties.LogControlProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 日志路径缓存管理器.
 *
 * @author SunTao
 * @since 2024-05-19
 */
@Component
public class LogPathCacheManager {
    private static final Logger log = LoggerFactory.getLogger(LogPathCacheManager.class);

    private LogControlProperties logControlProperties;

    /**
     * 初始化时清空缓存
     */
    @PostConstruct
    public void initCache() {
        LogPathCache.clearCache();
    }

    /**
     * 判断某路径是否打印日志
     */
    public boolean enabledLog(String path) {
        try {
            if (!logControlProperties.isEnabled()) {
                // 未开启请求响应日志打印
                return false;
            }

            return LogPathCache.allowedLogWithCache(path, logControlProperties.getModel(), logControlProperties.getPaths());
        } catch (Exception e) {
            log.error("日志路径判断异常", e);
            return true;
        }
    }

    @Autowired
    public void setLogControlProperties(LogControlProperties logControlProperties) {
        this.logControlProperties = logControlProperties;
    }
}
