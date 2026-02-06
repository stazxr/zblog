package com.github.stazxr.zblog.bas.context.util;

import com.github.stazxr.zblog.bas.context.autoconfigure.properties.HeaderProperties;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.net.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Application header global context.
 *
 * <p>
 * Provides static access to system code, application code, and deployment code.
 * Must be initialized once at application startup via {@link #init(HeaderProperties)}.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 *     String sysCode = ApplicationHeaderContext.getSysCode();
 *     String appCode = ApplicationHeaderContext.getAppCode();
 *     String deployCode = ApplicationHeaderContext.getDeployCode();
 * </pre>
 * </p>
 *
 * @author SunTao
 * @since 2024-05-05
 */
public final class HeaderContextHolder {

    private static final Logger log = LoggerFactory.getLogger(HeaderContextHolder.class);

    /** Header properties instance */
    private static HeaderProperties headerProperties;

    /** Generated deployment code */
    private static String deployCode;

    /** Ensure init only once */
    private static final AtomicBoolean initialized = new AtomicBoolean(false);

    private HeaderContextHolder() {
        // Prevent instantiation
    }

    /**
     * Initialize ApplicationHeaderContext.
     * <p>
     * Must be called once during application startup after HeaderProperties are loaded and validated.
     * </p>
     *
     * @param properties HeaderProperties bean
     */
    public static void init(HeaderProperties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("HeaderProperties must not be null.");
        }

        if (!initialized.compareAndSet(false, true)) {
            throw new IllegalStateException("ApplicationHeaderContext has already been initialized.");
        }

        headerProperties = properties;

        HeaderProperties.Deploy deploy = properties.getDeploy();
        if (deploy != null && StringUtils.isNotBlank(deploy.getDeployIp())) {
            deployCode = IpUtils.get7CharFromIpString(deploy.getDeployIp(), deploy.getDeployUnit());
            log.info("Application deploy code initialized: {}", deployCode);
        } else {
            log.warn("Deploy information is incomplete, deployCode not generated.");
        }
    }

    private static void checkInitialized() {
        if (!initialized.get()) {
            throw new IllegalStateException(
                "ApplicationHeaderContext is not initialized. Call init(HeaderProperties) first."
            );
        }
    }

    /** Get system code */
    public static String getSysCode() {
        checkInitialized();
        return headerProperties.getSysCode();
    }

    /** Get application code */
    public static String getAppCode() {
        checkInitialized();
        return headerProperties.getAppCode();
    }

    /** Get deployment code */
    public static String getDeployCode() {
        checkInitialized();
        return deployCode;
    }
}