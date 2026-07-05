package com.github.stazxr.zblog.audit.cloud.tencent.provider;

import com.github.stazxr.zblog.audit.cloud.provider.ContentSecurityProvider;
import com.github.stazxr.zblog.audit.cloud.tencent.converter.TencentTmsConverter;
import com.github.stazxr.zblog.audit.cloud.tencent.service.TencentTmsService;
import com.github.stazxr.zblog.audit.enums.AuditDecision;
import com.github.stazxr.zblog.audit.model.ProcessorResult;
import com.tencentcloudapi.tms.v20201229.models.TextModerationResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 腾讯云 TMS 内容安全 Provider
 *
 * @author SunTao
 * @since 2026-07-05
 */
@Component
public class TencentTmsProvider implements ContentSecurityProvider {
    public static final String PARAM_BIZ_TYPE = "bizType";

    private final TencentTmsService tencentTmsService;

    private final TencentTmsConverter tencentTmsConverter;

    public TencentTmsProvider(TencentTmsService tencentTmsService, TencentTmsConverter tencentTmsConverter) {
        this.tencentTmsService = tencentTmsService;
        this.tencentTmsConverter = tencentTmsConverter;
    }

    @Override
    public ProcessorResult checkText(String content, Map<String, Object> params) {
        try {
            // 1️⃣ 安全获取 bizType
            String bizType = resolveBizType(params);

            // 2️⃣ 调用云审核
            TextModerationResponse resp = tencentTmsService.moderateText(content, bizType);

            // 3️⃣ 云结果转换为统一模型
            return tencentTmsConverter.convert(resp);
        } catch (Exception e) {
            // 4️⃣ 异常降级（进入人工审核）
            ProcessorResult result = new ProcessorResult();
            result.setDecision(AuditDecision.MANUAL);
            result.setReason(buildErrorReason(e));
            return result;
        }
    }

    /**
     * 安全解析 bizType
     */
    private String resolveBizType(Map<String, Object> params) {
        if (params == null) {
            return null;
        }
        Object bizType = params.get(PARAM_BIZ_TYPE);
        return bizType == null ? null : String.valueOf(bizType);
    }

    /**
     * 统一异常信息（避免泄露堆栈细节）
     */
    private String buildErrorReason(Exception e) {
        return "TENCENT_TMS_ERROR: " + e.getClass().getSimpleName() + " - " + e.getMessage();
    }
}
