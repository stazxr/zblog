package com.github.stazxr.zblog.bas.rest.autoconfigure;

import com.github.stazxr.zblog.bas.rest.autoconfigure.properties.RestProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 统一响应自动配置类
 *
 * @author SunTao
 * @since 2026-01-25
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RestProperties.class)
public class RestAutoConfiguration {
}
