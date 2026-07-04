package com.github.stazxr.zblog.audit.config.properties;

/**
 * Processor 配置
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
public class AuditProcessorProperties {
    /**
     * Processor 注册名
     */
    private String name;

    /**
     * 执行顺序
     */
    private int order;

    /**
     * 是否启用
     */
    private boolean enabled = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}