package com.github.stazxr.zblog.bas.log.config;

import com.github.stazxr.zblog.bas.log.properties.LogControlProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for logging related beans and properties.
 *
 * This class scans for components in the package 'com.github.stazxr.zblog.bas.log'
 * and enables configuration properties from LogControlProperties.
 *
 * @author SunTao
 * @since 2024-05-16
 */
@Configuration
@ComponentScan("com.github.stazxr.zblog.bas.log")
@EnableConfigurationProperties({LogControlProperties.class})
public class LogConfiguration {
}
