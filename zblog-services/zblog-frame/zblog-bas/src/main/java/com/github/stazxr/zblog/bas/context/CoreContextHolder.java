package com.github.stazxr.zblog.bas.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.github.stazxr.zblog.bas.context.constant.TagConstants;
import com.github.stazxr.zblog.bas.context.entity.ContextTag;
import com.github.stazxr.zblog.bas.context.autoconfigure.properties.ContextProperties;
import com.github.stazxr.zblog.bas.context.autoconfigure.properties.HeaderProperties;
import com.github.stazxr.zblog.bas.context.util.HeaderContextHolder;
import com.github.stazxr.zblog.bas.context.util.SpringContextHolder;

/**
 * Thread-local holder for CoreContext instances.
 *
 * <p>
 * Ensures each thread has its own CoreContext instance.
 * Automatically injects system-level tags on creation.
 * </p>
 *
 * @author SunTao
 * @since 2024-07-02
 */
class CoreContextHolder {
    private static final TransmittableThreadLocal<CoreContext> CONTEXT = new TransmittableThreadLocal<>();

    /** 初始化 CoreContext */
    public static void init() {
        CoreContext coreContext = CONTEXT.get();
        if (coreContext == null) {
            ContextProperties contextProperties = SpringContextHolder.getBean(ContextProperties.class);
            coreContext = new CoreContext(contextProperties);
            addSystemTags(coreContext);
            CONTEXT.set(coreContext);
        }
    }

    /** 获取 CoreContext */
    public static CoreContext get() {
        CoreContext coreContext = CONTEXT.get();
        if (coreContext == null) {
            init();
        }
        return CONTEXT.get();
    }

    /** 复制 CoreContext */
    public static CoreContext copy() {
        return CONTEXT.get().copy();
    }

    /** 清理 CoreContext */
    public static void clear() {
        CoreContext coreContext = CONTEXT.get();
        if (coreContext != null) {
            coreContext.clear();
        }
        CONTEXT.remove();
    }

    /** Inject system-level tags into CoreContext */
    private static void addSystemTags(CoreContext coreContext) {
        HeaderProperties headerProperties = SpringContextHolder.getBean(HeaderProperties.class);

        coreContext.put(new ContextTag(TagConstants.SYS_CODE_TAG, HeaderContextHolder.getSysCode()));
        coreContext.put(new ContextTag(TagConstants.APP_CODE_TAG, HeaderContextHolder.getAppCode()));
        String deployCode = HeaderContextHolder.getDeployCode();
        if (deployCode != null) {
            coreContext.put(new ContextTag(TagConstants.DEPLOY_CODE_TAG, deployCode));
        }

        HeaderProperties.Deploy deploy = headerProperties.getDeploy();
        coreContext.put(new ContextTag(TagConstants.DEPLOY_AREA_TAG, deploy.getDeployArea()));
        coreContext.put(new ContextTag(TagConstants.DEPLOY_CENTER_TAG, deploy.getDeployCenter()));
        coreContext.put(new ContextTag(TagConstants.DEPLOY_UNIT_TAG, String.valueOf(deploy.getDeployUnit())));
        coreContext.put(new ContextTag(TagConstants.DEPLOY_IP_TAG, deploy.getDeployIp()));
    }
}
