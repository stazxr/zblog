package com.github.stazxr.zblog.bas.log.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration properties for controlling logging behavior.
 *
 * This class defines properties related to logging control,
 * such as enabling/disabling logging and specifying excluded paths.
 *
 * @author SunTao
 * @since 2024-05-16
 */
@ConfigurationProperties(prefix = LogControlProperties.PREFIX)
public class LogControlProperties {
    static final String PREFIX = "zblog.base.log.control";

    /**
     * Flag to enable or disable request and response logging.
     * Default is false (logging disabled).
     */
    private boolean enabled = false;

    /**
     * List of paths for which logging should be disabled.
     */
    private List<String> excludePath = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getExcludePath() {
        return excludePath == null ? new ArrayList<>() : excludePath;
    }

    public void setExcludePath(List<String> excludePath) {
        this.excludePath = excludePath;
    }
}
