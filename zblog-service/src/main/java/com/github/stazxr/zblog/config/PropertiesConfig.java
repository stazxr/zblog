package com.github.stazxr.zblog.config;

import com.github.stazxr.zblog.config.properties.SwaggerProperties;
import com.github.stazxr.zblog.config.properties.SystemProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义配置
 *
 * @author SunTao
 * @since 2021-12-05
 */
@Configuration
@EnableConfigurationProperties({SystemProperties.class, SwaggerProperties.class})
public class PropertiesConfig {
}
