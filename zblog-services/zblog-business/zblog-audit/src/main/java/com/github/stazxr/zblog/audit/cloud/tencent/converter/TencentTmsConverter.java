package com.github.stazxr.zblog.audit.cloud.tencent.converter;

import com.github.stazxr.zblog.audit.cloud.CloudAuditResult;
import com.tencentcloudapi.tms.v20201229.models.TextModerationResponse;
import org.springframework.stereotype.Component;

@Component
public class TencentTmsConverter {
    public CloudAuditResult convert(TextModerationResponse resp) {
        CloudAuditResult result = new CloudAuditResult();
        String suggestion = resp.getSuggestion();
        if ("PASS".equalsIgnoreCase(suggestion)) {
            result.setPass(true);
        }
        if ("REVIEW".equalsIgnoreCase(suggestion)) {
            result.setPending(true);
        }
        if ("BLOCK".equalsIgnoreCase(suggestion)) {
            result.setReject(true);
            result.setReason("腾讯TMS命中违规内容");
        }
        return result;
    }
}
