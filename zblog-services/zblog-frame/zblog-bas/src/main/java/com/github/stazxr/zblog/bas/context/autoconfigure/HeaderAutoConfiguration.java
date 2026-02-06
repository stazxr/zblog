package com.github.stazxr.zblog.bas.context.autoconfigure;

import com.github.stazxr.zblog.bas.context.autoconfigure.properties.HeaderProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class to enable HeaderProperties.
 *
 * <p>
 * By enabling {@link HeaderProperties}, Spring will create and inject the bean,
 * and trigger its {@link HeaderProperties#afterPropertiesSet()} lifecycle.
 * </p>
 *
 * @author SunTao
 * @since 2024-05-05
 */
@Configuration
@EnableConfigurationProperties(HeaderProperties.class)
public class HeaderAutoConfiguration {
    // No additional code required; the properties bean will auto-initialize
}
