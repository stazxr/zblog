package com.github.stazxr.zblog.bas.exception.autoconfigure.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 异常模块配置类
 *
 * <p>用于统一管理异常相关配置，例如错误码扫描开关、扫描包路径等</p>
 *
 * <p>配置前缀：<b>zblog.base.exception</b></p>
 *
 * @author SunTao
 * @since 2026-01-25
 */
@ConfigurationProperties(prefix = ExceptionProperties.CONFIG_PREFIX)
public class ExceptionProperties {
    static final String CONFIG_PREFIX = "zblog.base.exception";

    /**
     * 错误码配置信息
     */
    private ErrorCodeConfig errorCodeConfig = new ErrorCodeConfig();

    public ErrorCodeConfig getErrorCodeConfig() {
        return errorCodeConfig;
    }

    public void setErrorCodeConfig(ErrorCodeConfig errorCodeConfig) {
        this.errorCodeConfig = errorCodeConfig;
    }

    @Getter
    @Setter
    public static class ErrorCodeConfig {
        /**
         * 是否开启错误码格式检查，默认 true
         */
        private boolean check = true;

        /**
         * 错误码扫描的起始包，默认 com.github.stazxr.zblog
         */
        private String scanPackage = "com.github.stazxr.zblog";
    }
}
