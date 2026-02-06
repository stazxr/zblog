package com.github.stazxr.zblog.bas.log.autoconfigure;

import com.github.stazxr.zblog.bas.log.autoconfigure.properties.LogControlProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 日志自动配置.
 *
 * @author SunTao
 * @since 2024-05-16
 */
@Configuration
@EnableConfigurationProperties(LogControlProperties.class)
public class LogAutoConfiguration {
}
