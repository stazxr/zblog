package com.github.stazxr.zblog.core.config;

import com.github.stazxr.zblog.core.config.properties.WebsiteProperties;
import com.github.stazxr.zblog.core.config.properties.ZblogProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Z-BLOG 配置中心
 *
 * @author SunTao
 * @since 2022-01-28
 */
@Configuration
@EnableConfigurationProperties(value = { ZblogProperties.class, WebsiteProperties.class })
public class ZblogConfig {
}
