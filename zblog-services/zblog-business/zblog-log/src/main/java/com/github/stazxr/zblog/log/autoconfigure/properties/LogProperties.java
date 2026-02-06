package com.github.stazxr.zblog.log.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 *
 * @author SunTao
 * @since 2022-06-20
 */
@ConfigurationProperties(prefix = LogProperties.CONFIG_PREFIX)
public class LogProperties {
    static final String CONFIG_PREFIX = "zblog.aop-log";

    /**
     * 是否启用切面日志
     */
    private boolean enabled = true;

    /**
     * 是否开启异步日志写入
     */
    private boolean async = true;

    /**
     * 是否记录参数
     */
    private boolean recordParam = true;

    /**
     * 是否记录返回值（建议关闭）
     */
    private boolean recordResult = false;

    /**
     * 最大参数长度
     */
    private int maxParamLength = 65535;

    /**
     * 最大返回长度
     */
    private int maxResultLength = 65535;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isRecordParam() {
        return recordParam;
    }

    public void setRecordParam(boolean recordParam) {
        this.recordParam = recordParam;
    }

    public boolean isRecordResult() {
        return recordResult;
    }

    public void setRecordResult(boolean recordResult) {
        this.recordResult = recordResult;
    }

    public int getMaxParamLength() {
        return maxParamLength;
    }

    public void setMaxParamLength(int maxParamLength) {
        this.maxParamLength = maxParamLength;
    }

    public int getMaxResultLength() {
        return maxResultLength;
    }

    public void setMaxResultLength(int maxResultLength) {
        this.maxResultLength = maxResultLength;
    }
}
