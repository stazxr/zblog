package com.github.stazxr.zblog.context.properties;

import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.LocalHostUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.io.Serializable;

/**
 * Header parameters for configuring caller system code, application code, and deployment information.
 * <p>
 * This class provides functionality to configure caller system code, application code, and deployment information,
 * including deployment region, deployment center/room, deployment server address, and deployment unit.
 * </p>
 *
 * @since 2024-05-05
 * @author SunTao
 */
@ConfigurationProperties(prefix = HeaderProperties.HEADER_PREFIX)
public class HeaderProperties implements Serializable, InitializingBean, EnvironmentAware {
    private static final long serialVersionUID = 6460641327160614388L;

    static final String HEADER_PREFIX = "zblog.base.header";

    private static final String LOG_PREFIX = "ZCXT[MusesHeaderProperties]: ";

    private Environment environment;

    /**
     * Caller system code.
     */
    private String sysCode;

    /**
     * Caller application code.
     */
    private String appCode;

    /**
     * Deployment information.
     */
    private Deploy deploy = new Deploy();

    public String getSysCode() {
        return sysCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public Deploy getDeploy() {
        return deploy;
    }

    public void setDeploy(Deploy deploy) {
        if (deploy == null) {
            deploy = new Deploy();
        }
        this.deploy = deploy;
    }

    /**
     * Initializes deployment information.
     *
     * @throws Exception if an exception occurs
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isBlank(sysCode)) {
            throw new IllegalArgumentException("Properties [" + HEADER_PREFIX + ".sysCode] must be set.");
        }
        String customDeployIp = environment.resolvePlaceholders("${zblog.deploy-ip:}");
        String customDeployCount = environment.resolvePlaceholders("${zblog.deploy-count:0}");
        if (StringUtils.isNotBlank(customDeployIp)) {
            deploy.setDeployIp(customDeployIp);
            deploy.setDeployUnit(Integer.parseInt(customDeployCount));
        } else {
            if (StringUtils.isBlank(deploy.getDeployIp())) {
                // Get local IP address
                deploy.setDeployIp(LocalHostUtils.getLocalIp());
            }
        }
    }

    /**
     * Sets the environment.
     *
     * @param environment The environment object
     */
    @Override
    public void setEnvironment(@NotNull Environment environment) {
        this.environment = environment;
    }

    /**
     * Sets the caller system code.
     *
     * @param sysCode The caller system code
     */
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    /**
     * Sets the caller application code.
     * If appCode is null, it defaults to the spring.application.name property.
     *
     * @param appCode The caller application code
     */
    public void setAppCode(String appCode) {
        if (appCode == null) {
            this.appCode = environment.getProperty("spring.application.name");
        } else {
            this.appCode = appCode;
        }
    }

    /**
     * Deployment information.
     */
    public static class Deploy {
        /**
         * Deployment region.
         */
        private String deployArea;

        /**
         * Deployment center/room.
         */
        private String deployCenter;

        /**
         * Deployment server address.
         */
        private String deployIp;

        /**
         * Deployment unit [0, 17].
         */
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

        /**
         * Sets the deployment unit.
         * If deployUnit is less than 0 or greater than 17, an IllegalArgumentException will be thrown.
         *
         * @param deployUnit The deployment unit
         * @throws IllegalArgumentException if the deployment unit is out of range
         */
        public void setDeployUnit(int deployUnit) {
            final int maxIpCount = 17;
            if (deployUnit < 0 || deployUnit > maxIpCount) {
                throw new IllegalArgumentException(LOG_PREFIX + "Deploy unit out of range [0, 17]: " + deployUnit);
            }
            this.deployUnit = deployUnit;
        }

        public int getDeployUnit() {
            return deployUnit;
        }
    }
}
