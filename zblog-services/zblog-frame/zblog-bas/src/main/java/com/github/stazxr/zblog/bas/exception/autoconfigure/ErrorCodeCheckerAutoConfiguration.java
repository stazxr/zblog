package com.github.stazxr.zblog.bas.exception.autoconfigure;

import com.github.stazxr.zblog.bas.exception.autoconfigure.properties.ExceptionProperties;
import com.github.stazxr.zblog.bas.exception.code.ErrorCodeChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 错误码启动检查自动配置类
 *
 * <p>用于在 Spring Boot 启动阶段扫描指定包下所有 {@link com.github.stazxr.zblog.bas.exception.code.ErrorCode} 枚举，
 * 并校验错误码格式及重复性。</p>
 *
 * <p>仅当配置 {@code zblog.base.exception.error-code-config.check=true} 时才会生效。</p>
 *
 * <p>依赖配置类 {@link ExceptionProperties}，可通过配置文件控制扫描路径。</p>
 *
 * @author SunTao
 * @since 2026-01-25
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "zblog.base.exception.error-code-config", name = "check", havingValue = "true")
@AutoConfigureAfter(ExceptionAutoConfiguration.class)
public class ErrorCodeCheckerAutoConfiguration {
    private final ExceptionProperties exceptionProperties;

    @PostConstruct
    public void init() {
        ExceptionProperties.ErrorCodeConfig errorCodeConfig = exceptionProperties.getErrorCodeConfig();
        ErrorCodeChecker.check(errorCodeConfig.getScanPackage());
    }
}
