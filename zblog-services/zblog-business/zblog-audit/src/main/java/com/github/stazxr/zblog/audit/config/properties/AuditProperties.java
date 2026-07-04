package com.github.stazxr.zblog.audit.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 审核流程配置
 *
 * @author Sun Tao
 * @since 2026-07-04
 */
@ConfigurationProperties(prefix = "audit")
public class AuditProperties {
    /**
     * key = scene（COMMENT / MESSAGE）
     */
    private Map<String, AuditPolicyProperties> policies;

    public Map<String, AuditPolicyProperties> getPolicies() {
        return policies;
    }

    public void setPolicies(Map<String, AuditPolicyProperties> policies) {
        this.policies = policies;
    }
}
