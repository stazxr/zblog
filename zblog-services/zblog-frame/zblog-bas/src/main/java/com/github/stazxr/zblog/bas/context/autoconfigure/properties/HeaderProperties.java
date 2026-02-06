package com.github.stazxr.zblog.bas.context.autoconfigure.properties;

import com.github.stazxr.zblog.bas.context.util.HeaderContextHolder;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.LocalHostUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Header properties configuration class.
 *
 * <p>
 * Holds system code, application code, and deployment information.
 * Provides lifecycle initialization and validation.
 * </p>
 *
 * <p>
 * After properties are set, this class initializes the {@link HeaderContextHolder}
 * for global static access to header-related information.
 * </p>
 *
 * @author SunTao
 * @since 2024-05-05
 */
@ConfigurationProperties(prefix = HeaderProperties.CONFIG_PREFIX)
public class HeaderProperties implements Serializable, InitializingBean, EnvironmentAware {
    private static final long serialVersionUID = 6460641327160614388L;

    /** Configuration prefix */
    public static final String CONFIG_PREFIX = "zblog.base.header";

    /** Spring environment, injected by Spring */
    private Environment environment;

    /** Caller system code (required) */
    private String sysCode;

    /** Caller application code, defaults to spring.application.name */
    private String appCode;

    /** Deployment information */
    private Deploy deploy = new Deploy();

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

    public Deploy getDeploy() {
        return deploy;
    }

    public void setDeploy(Deploy deploy) {
        this.deploy = deploy;
    }

    // ====================== Lifecycle Methods ======================

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    /**
     * Validates and initializes properties.
     * <p>
     * This method is invoked automatically by Spring during container startup.
     * After validation, it initializes the {@link HeaderContextHolder} for global access.
     * </p>
     *
     * @throws SocketException      if IP retrieval fails
     * @throws UnknownHostException if host is unknown
     */
    @Override
    public void afterPropertiesSet() throws SocketException, UnknownHostException {
        // Validate mandatory sysCode
        if (StringUtils.isBlank(sysCode)) {
            throw new IllegalArgumentException("Properties [" + CONFIG_PREFIX + ".sysCode] must be set.");
        }

        // Default application code
        if (StringUtils.isBlank(appCode)) {
            this.appCode = environment.getProperty("spring.application.name");
        }

        // Deployment override (global)
        String customDeployIp = environment.getProperty("zblog.deploy-ip");
        Integer customDeployCount = environment.getProperty("zblog.deploy-count", Integer.class, 0);

        if (StringUtils.isNotBlank(customDeployIp)) {
            deploy.setDeployIp(customDeployIp);
            deploy.setDeployUnit(customDeployCount);
        } else if (StringUtils.isBlank(deploy.getDeployIp())) {
            String localIp = LocalHostUtils.getLocalIp();
            if (StringUtils.isBlank(localIp)) {
                throw new IllegalStateException("Cannot determine local deploy IP.");
            }
            deploy.setDeployIp(localIp);
        }

        // Initialize global static access
        HeaderContextHolder.init(this);
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
