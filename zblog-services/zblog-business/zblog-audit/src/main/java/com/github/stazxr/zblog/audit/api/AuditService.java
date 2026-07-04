package com.github.stazxr.zblog.audit.api;

import com.github.stazxr.zblog.audit.engine.AuditEngine;
import com.github.stazxr.zblog.audit.model.AuditContext;
import com.github.stazxr.zblog.audit.model.AuditResult;
import org.springframework.stereotype.Component;

/**
 * 内容审核服务
 *
 * <p>对外提供统一审核入口，业务模块通过该服务完成内容自动审核。
 *
 * @author SunTao
 * @since 2026-06-29
 */
@Component
public class AuditService {
    private final AuditEngine auditEngine;

    public AuditService(AuditEngine auditEngine) {
        this.auditEngine = auditEngine;
    }

    /**
     * 执行审核
     *
     * @param context 审核上下文
     * @return 审核结果
     */
    public AuditResult audit(AuditContext context) {
        return auditEngine.audit(context);
    }
}
