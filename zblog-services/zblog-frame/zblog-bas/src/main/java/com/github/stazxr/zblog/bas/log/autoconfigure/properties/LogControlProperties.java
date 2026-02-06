package com.github.stazxr.zblog.bas.log.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志控制配置, 用于控制是否打印请求和响应日志.
 *
 * 配置示例：
 *
 * zblog.base.log.control:
 *   enabled: true
 *   model: 0
 *   paths:
 *     - /api/**
 *
 * model说明：
 * 0 = paths 为允许打印日志路径
 * 1 = paths 为禁止打印日志路径
 *
 * @author SunTao
 */
@ConfigurationProperties(prefix = LogControlProperties.PREFIX)
public class LogControlProperties {

    public static final String PREFIX = "zblog.base.log.control";

    /**
     * 是否启用日志控制
     */
    private boolean enabled = false;

    /**
     * 模式
     * 0 = 白名单模式（paths为允许日志路径）
     * 1 = 黑名单模式（paths为禁止日志路径）
     */
    private int model = 1;

    /**
     * 路径列表
     */
    private List<String> paths = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
