package com.github.stazxr.zblog.log.annotation.properties;

/**
 * 日志配置信息
 *
 * @author SunTao
 * @since 2022-06-20
 */
public class LogProperties {
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
