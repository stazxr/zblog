package com.github.stazxr.zblog.bas.context.autoconfigure;

import com.github.stazxr.zblog.bas.context.autoconfigure.properties.ContextProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for context component.
 *
 * <p>
 * Enables {@link ContextProperties} so that default tags and user-defined tags
 * can be loaded automatically into the Spring container.
 * </p>
 *
 * <p>
 * No additional beans are required; {@link ContextProperties#afterPropertiesSet()}
 * will handle initialization and merging.
 * </p>
 *
 * @author SunTao
 * @since 2024-05-16
 */
@Configuration
@EnableConfigurationProperties(ContextProperties.class)
public class ContextAutoConfiguration {
    // Empty configuration class; @EnableConfigurationProperties handles initialization
}
