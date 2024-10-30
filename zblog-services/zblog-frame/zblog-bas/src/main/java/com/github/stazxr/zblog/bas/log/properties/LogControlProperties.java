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
     * The model of paths.
     * Default is 0 (the paths is enabled path list)
     */
    private int model = 0;

    /**
     * List of paths for which logging should be disabled or enabled(by model).
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
        return paths == null ? new ArrayList<>() : paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
