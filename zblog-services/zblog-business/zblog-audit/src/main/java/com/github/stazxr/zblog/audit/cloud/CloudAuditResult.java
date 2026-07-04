package com.github.stazxr.zblog.audit.cloud;

import lombok.Data;

@Data
public class CloudAuditResult {
    private boolean pass;

    private boolean reject;

    private boolean pending;

    private String reason;
}