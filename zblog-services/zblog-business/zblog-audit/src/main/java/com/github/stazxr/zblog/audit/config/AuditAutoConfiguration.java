package com.github.stazxr.zblog.audit.config;

import com.github.stazxr.zblog.audit.config.properties.AuditProperties;
import com.github.stazxr.zblog.audit.config.properties.XssProcessorConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 审核配置自动装配
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
@Configuration
@EnableConfigurationProperties({AuditProperties.class, XssProcessorConfig.class})
public class AuditAutoConfiguration {
}
