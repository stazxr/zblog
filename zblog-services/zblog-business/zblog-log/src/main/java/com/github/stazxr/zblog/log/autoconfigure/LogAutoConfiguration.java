package com.github.stazxr.zblog.log.autoconfigure;

import com.github.stazxr.zblog.log.aop.LogAspect;
import com.github.stazxr.zblog.log.autoconfigure.properties.LogProperties;
import com.github.stazxr.zblog.log.service.LogService;
import com.github.stazxr.zblog.log.service.impl.AsyncLogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 切面日志自动配置
 *
 * @author SunTao
 * @since 2026-02-04
 */
@Configuration
@EnableConfigurationProperties(LogProperties.class)
public class LogAutoConfiguration {
    @Bean
    @ConditionalOnBean(EnableLogMarker.class)
    public LogAspect operationLogAspect(LogService logService, AsyncLogService asyncLogService, LogProperties logProperties) {
        return new LogAspect(logService, asyncLogService, logProperties);
    }
}
