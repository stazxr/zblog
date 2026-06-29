package com.github.stazxr.zblog.audit.service;

import com.github.stazxr.zblog.audit.AuditContext;
import com.github.stazxr.zblog.audit.AuditResult;
import com.github.stazxr.zblog.audit.engine.AuditEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 内容审核服务。
 *
 * 对外提供统一审核入口，业务模块通过该服务完成内容审核。
 *
 * @author SunTao
 * @since 2026-06-29
 */
@Service
@RequiredArgsConstructor
public class AuditService {
    /**
     * 审核引擎
     */
    private final AuditEngine auditEngine;

    /**
     * 执行审核。
     *
     * @param context 审核上下文
     * @return 审核结果
     */
    public AuditResult audit(AuditContext context) {
        return auditEngine.audit(context);
    }
}
