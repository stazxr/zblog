package com.github.stazxr.zblog.bas.context.autoconfigure.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.stazxr.zblog.bas.context.entity.ContextTag;
import com.github.stazxr.zblog.bas.context.exception.ContextErrorCode;
import com.github.stazxr.zblog.bas.context.exception.ContextException;
import com.github.stazxr.zblog.bas.context.util.HeaderContextHolder;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.LocalHostUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.InputStream;
import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuration properties for ZBLOG context tags.
 *
 * <p>
 * Loads default tags from a YAML file and merges with user-defined tags.
 * Default tags cannot be overridden by custom tags.
 * </p>
 *
 * <p>
 * Provides lifecycle initialization via {@link InitializingBean}.
 * </p>
 *
 * @author SunTao
 * @since 2024-05-05
 */
@ConfigurationProperties(prefix = ContextProperties.CONFIG_PREFIX)
public class ContextProperties implements Serializable, InitializingBean {
    private static final long serialVersionUID = 1761392456056935111L;

    private static final Logger log = LoggerFactory.getLogger(ContextProperties.class);

    /** Configuration prefix */
    public static final String CONFIG_PREFIX = "zblog.base.context";

    /** Default YAML file containing framework default tags */
    private static final String DEFAULT_MUSES_TAGS_FILE = "/default-tags.yml";

    /** JVM deployment area property. */
    private static final String JVM_DEPLOY_AREA = "zblog.bas.deploy.area";

    /** JVM deployment center property. */
    private static final String JVM_DEPLOY_CENTER = "zblog.bas.deploy.center";

    /** JVM deployment unit property. */
    private static final String JVM_DEPLOY_UNIT = "zblog.bas.deploy.unit";

    /** JVM deployment IP property. */
    private static final String JVM_DEPLOY_IP = "zblog.bas.deploy.ip";

    /** Caller system code (required) */
    private String sysCode;

    /** Caller application code, defaults to spring.application.name */
    private String appCode;

    /** Deployment information */
    private ContextProperties.Deploy deploy = new ContextProperties.Deploy();

    /** List of all context tags */
    private List<ContextTag> tags = new ArrayList<>();

    // ====================== Getters / Setters ======================

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public ContextProperties.Deploy getDeploy() {
        return deploy;
    }

    public void setDeploy(ContextProperties.Deploy deploy) {
        this.deploy = deploy;
    }

    public List<ContextTag> getTags() {
        return tags;
    }

    public void setTags(List<ContextTag> tags) {
        this.tags = tags;
    }

    /**
     * Get only tag names.
     *
     * @return List of tag names
     */
    public List<String> getTagNames() {
        return this.tags.stream().map(ContextTag::getTagName).collect(Collectors.toList());
    }

    // ====================== Lifecycle Methods ======================

    /**
     * Initializes the bean after properties are set.
     *
     * <p>
     * Loads default tags from YAML and merges them with custom tags.
     * Default tags cannot be overridden.
     * </p>
     *
     * @throws ContextException if YAML loading fails
     * @throws SocketException if local ip fails
     * @throws UnknownHostException if local ip fails
     */
    @Override
    public void afterPropertiesSet() throws SocketException, UnknownHostException {
        // Validate mandatory sysCode.
        if (StringUtils.isBlank(sysCode)) {
            throw new IllegalArgumentException("Properties [" + CONFIG_PREFIX + ".sysCode] must be set.");
        }

        if (StringUtils.isBlank(appCode)) {
            throw new IllegalArgumentException("Properties [" + CONFIG_PREFIX + ".appCode] must be set.");
        }

        // Initialize deployment information.
        initializeDeploy();

        // Initialize global static access.
        HeaderContextHolder.init(this);

        try (InputStream is = this.getClass().getResourceAsStream(DEFAULT_MUSES_TAGS_FILE)) {
            if (is == null) {
                log.warn("Default tags file {} not found, skipping default tags load.", DEFAULT_MUSES_TAGS_FILE);
                if (this.tags == null) {
                    this.tags = new ArrayList<>();
                }
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            ContextProperties defaultProps = objectMapper.readValue(is, ContextProperties.class);

            log.info("Loading default context tags: {}", defaultProps.getTagNames());
            addTags(defaultProps.getTags());
        } catch (Exception e) {
            throw new ContextException(ContextErrorCode.ZCXT001, e);
        } finally {
            if (this.tags != null) {
                log.info("Loaded all context tags: {}", this.tags.stream().map(ContextTag::getTagName).collect(Collectors.toList()));
            }
        }
    }

    /**
     * Merge default tags with custom tags.
     *
     * <p>
     * Default tags cannot be overridden.
     * </p>
     *
     * @param defaultTags default tags loaded from YAML
     */
    private void addTags(List<ContextTag> defaultTags) {
        if (this.tags == null) {
            this.tags = new ArrayList<>(defaultTags);
        } else {
            List<ContextTag> merged = new ArrayList<>(defaultTags);
            for (ContextTag customTag : this.tags) {
                if (merged.contains(customTag)) {
                    // Prevent overriding default tags
                    log.warn("Tag '{}' cannot be customized; using default value.", customTag.getTagName());
                } else {
                    merged.add(customTag);
                }
            }
            this.tags = merged;
        }
    }

    /**
     * Initializes deployment information.
     *
     * @throws SocketException if local IP lookup fails
     * @throws UnknownHostException if local IP lookup fails
     */
    private void initializeDeploy() throws SocketException, UnknownHostException {
        // JVM parameters have the highest priority.
        String deployArea = System.getProperty(JVM_DEPLOY_AREA);
        if (StringUtils.isNotBlank(deployArea)) {
            deploy.setDeployArea(deployArea);
        }

        String deployCenter = System.getProperty(JVM_DEPLOY_CENTER);
        if (StringUtils.isNotBlank(deployCenter)) {
            deploy.setDeployCenter(deployCenter);
        }

        String deployUnit = System.getProperty(JVM_DEPLOY_UNIT);
        if (StringUtils.isNotBlank(deployUnit)) {
            try {
                deploy.setDeployUnit(Integer.parseInt(deployUnit));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "JVM property [" + JVM_DEPLOY_UNIT + "] must be an integer: " + deployUnit, e);
            }
        }

        String deployIp = System.getProperty(JVM_DEPLOY_IP);
        if (StringUtils.isNotBlank(deployIp)) {
            deploy.setDeployIp(deployIp);
        }

        // If no JVM IP is configured, automatically obtain the local IP.
        if (StringUtils.isBlank(deploy.getDeployIp())) {
            String localIp = LocalHostUtils.getLocalIp();
            if (StringUtils.isBlank(localIp)) {
                throw new IllegalStateException("Cannot determine local deploy IP.");
            }
            deploy.setDeployIp(localIp);
        }
    }

    /**
     * Deployment information holder.
     */
    public static class Deploy {

        /** Deployment region */
        private String deployArea;

        /** Deployment center / room */
        private String deployCenter;

        /** Deployment server IP */
        private String deployIp;

        /** Deployment unit [0, 17] */
        private int deployUnit = 0;

        public String getDeployArea() {
            return deployArea;
        }

        public void setDeployArea(String deployArea) {
            this.deployArea = deployArea;
        }

        public String getDeployCenter() {
            return deployCenter;
        }

        public void setDeployCenter(String deployCenter) {
            this.deployCenter = deployCenter;
        }

        public String getDeployIp() {
            return deployIp;
        }

        public void setDeployIp(String deployIp) {
            this.deployIp = deployIp;
        }

        public int getDeployUnit() {
            return deployUnit;
        }

        /**
         * Sets deployment unit.
         *
         * @param deployUnit must be in range [0, 17]
         */
        public void setDeployUnit(int deployUnit) {
            final int maxIpCount = 17;
            if (deployUnit < 0 || deployUnit > maxIpCount) {
                throw new IllegalArgumentException("Deploy unit out of range [0, 17]: " + deployUnit);
            }
            this.deployUnit = deployUnit;
        }
    }
}
