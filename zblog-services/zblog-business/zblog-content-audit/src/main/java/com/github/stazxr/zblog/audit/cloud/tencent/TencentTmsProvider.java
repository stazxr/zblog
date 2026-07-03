package com.github.stazxr.zblog.audit.cloud.tencent;

import com.github.stazxr.zblog.audit.cloud.CloudAuditResult;
import com.github.stazxr.zblog.audit.cloud.provider.ContentSecurityProvider;
import com.github.stazxr.zblog.audit.cloud.tencent.converter.TencentTmsConverter;
import com.github.stazxr.zblog.audit.cloud.tencent.service.TencentTmsService;
import com.tencentcloudapi.tms.v20201229.models.TextModerationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TencentTmsProvider implements ContentSecurityProvider {
    private final TencentTmsService service;
    private final TencentTmsConverter converter;

    @Override
    public CloudAuditResult checkText(String content) {
        TextModerationResponse resp = service.moderateText(content);
        return converter.convert(resp);
    }
}
