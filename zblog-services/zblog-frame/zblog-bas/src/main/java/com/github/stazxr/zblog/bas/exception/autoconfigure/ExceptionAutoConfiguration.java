package com.github.stazxr.zblog.bas.exception.autoconfigure;

import com.github.stazxr.zblog.bas.exception.autoconfigure.properties.ExceptionProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 异常模块自动配置类
 *
 * <p>Spring Boot 启动时自动扫描该配置类，可通过配置文件控制错误码扫描开关。</p>
 *
 * @author SunTao
 * @since 2026-01-25
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ExceptionProperties.class)
public class ExceptionAutoConfiguration {
}
