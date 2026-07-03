package com.github.stazxr.zblog.audit.handler;

import com.github.stazxr.zblog.audit.AuditContext;
import com.github.stazxr.zblog.audit.AuditResult;
import com.github.stazxr.zblog.audit.cloud.CloudAuditResult;
import com.github.stazxr.zblog.audit.cloud.provider.ContentSecurityProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TencentTmsAuditHandler implements AuditHandler {
    private final ContentSecurityProvider provider;

    @Override
    public int getOrder() {
        return 400;
    }

    @Override
    public String getName() {
        return "TencentTmsAuditHandler";
    }

    @Override
    public AuditResult handle(AuditContext context) {
        CloudAuditResult result = provider.checkText(context.getAuditContent());
        if (result.isReject()) {
            return AuditResult.reject(result.getReason());
        }
        if (result.isPending()) {
            return AuditResult.pending(context.getAuditContent());
        }
        return AuditResult.pass(context.getAuditContent());
    }
}
