package com.github.stazxr.zblog.audit.cloud.provider;

import com.github.stazxr.zblog.audit.cloud.CloudAuditResult;

public interface ContentSecurityProvider {
    CloudAuditResult checkText(String content);
}
