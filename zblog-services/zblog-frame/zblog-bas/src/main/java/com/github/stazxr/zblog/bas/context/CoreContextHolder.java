package com.github.stazxr.zblog.bas.context;

import com.github.stazxr.zblog.bas.context.constant.TagConstants;
import com.github.stazxr.zblog.bas.context.entity.ContextTag;
import com.github.stazxr.zblog.bas.context.properties.ContextProperties;
import com.github.stazxr.zblog.bas.context.properties.HeaderProperties;
import com.github.stazxr.zblog.bas.context.util.EnvironmentHelper;
import com.github.stazxr.zblog.bas.context.util.SpringContextUtil;

/**
 * Manages a thread-local instance of {@link CoreContext}.
 * Provides methods to get, set, and remove the context for the current thread.
 * Uses {@link ThreadLocal} for thread safety.
 *
 * @author SunTao
 * @since 2024-07-02
 */
class CoreContextHolder {
    private static final ThreadLocal<CoreContext> CONTEXT = new ThreadLocal<>();

    /**
     * Retrieves the current thread's {@link CoreContext} instance.
     * Creates a new instance if none exists for the current thread.
     *
     * @return The current thread's {@link CoreContext} instance
     */
    public static CoreContext get() {
        CoreContext coreContext = CONTEXT.get();
        if (coreContext == null) {
            ContextProperties contextProperties = SpringContextUtil.getBean(ContextProperties.class);
            coreContext = new CoreContext(contextProperties);
            addSystemTags(coreContext);
            // Set the context for the current thread
            set(coreContext);
        }
        return coreContext;
    }

    /**
     * Sets the {@link CoreContext} instance for the current thread.
     *
     * @param coreContext The {@link CoreContext} instance to set
     */
    public static void set(CoreContext coreContext) {
        CONTEXT.set(coreContext);
    }

    /**
     * Removes the {@link CoreContext} instance for the current thread.
     */
    public static void remove() {
        CONTEXT.remove();
    }

    /**
     * Adds system-related tags to the given {@link CoreContext}.
     *
     * @param coreContext The {@link CoreContext} to add tags to
     */
    private static void addSystemTags(CoreContext coreContext) {
        String sysCode = EnvironmentHelper.getSysCode();
        coreContext.put(new ContextTag(TagConstants.SYS_CODE_TAG, sysCode));

        String appCode = EnvironmentHelper.getAppCode();
        coreContext.put(new ContextTag(TagConstants.APP_CODE_TAG, appCode));

        String deployCode = EnvironmentHelper.getDeployCode();
        if (deployCode != null) {
            coreContext.put(new ContextTag(TagConstants.DEPLOY_CODE_TAG, deployCode));
        }

        HeaderProperties headerProperties = SpringContextUtil.getBean(HeaderProperties.class);
        HeaderProperties.Deploy deploy = headerProperties.getDeploy();
        coreContext.put(new ContextTag(TagConstants.DEPLOY_AREA_TAG, deploy.getDeployArea()));
        coreContext.put(new ContextTag(TagConstants.DEPLOY_CENTER_TAG, deploy.getDeployCenter()));
        coreContext.put(new ContextTag(TagConstants.DEPLOY_UNIT_TAG, String.valueOf(deploy.getDeployUnit())));
        coreContext.put(new ContextTag(TagConstants.DEPLOY_IP_TAG, deploy.getDeployIp()));
    }
}
