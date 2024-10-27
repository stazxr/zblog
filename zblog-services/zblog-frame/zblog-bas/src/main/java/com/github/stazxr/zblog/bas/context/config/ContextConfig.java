package com.github.stazxr.zblog.bas.context.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for context component.
 * <p>
 * This configuration class enables component scanning for the Context component
 * to automatically detect and register Spring components like controllers, services,
 * repositories, and other beans within the 'com.github.stazxr.zblog.context' package and its sub-packages.
 * </p>
 *
 * @since 2024-05-16
 * @author SunTao
 */
@Configuration
@ComponentScan("com.github.stazxr.zblog.bas.context")
public class ContextConfig {
}
