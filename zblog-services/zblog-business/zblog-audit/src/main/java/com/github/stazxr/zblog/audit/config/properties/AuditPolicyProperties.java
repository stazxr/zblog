package com.github.stazxr.zblog.audit.config.properties;

import java.util.List;

/**
 * 单个场景策略
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
public class AuditPolicyProperties {


    private List<AuditProcessorProperties> processors;

    public List<AuditProcessorProperties> getProcessors() {
        return processors;
    }

    public void setProcessors(List<AuditProcessorProperties> processors) {
        this.processors = processors;
    }
}
