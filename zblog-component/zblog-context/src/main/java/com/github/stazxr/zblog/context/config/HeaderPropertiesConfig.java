package com.github.stazxr.zblog.context.config;

import com.github.stazxr.zblog.context.util.EnvironmentHelper;
import com.github.stazxr.zblog.context.properties.HeaderProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Header properties.
 * <p>
 * This configuration class is used to inject the HeaderProperties object into EnvironmentHelper.
 * </p>
 *
 * @since 2024-05-05
 * @author SunTao
 */
@Configuration
@EnableConfigurationProperties({HeaderProperties.class})
public class HeaderPropertiesConfig {
    /**
     * Constructor used to inject the HeaderProperties object.
     *
     * @param properties HeaderProperties object
     */
    public HeaderPropertiesConfig(HeaderProperties properties) {
        EnvironmentHelper.setHeaderProperties(properties);
    }
}
