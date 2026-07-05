package com.github.stazxr.zblog.audit.cloud.tencent.converter;

import com.github.stazxr.zblog.audit.enums.AuditDecision;
import com.github.stazxr.zblog.audit.model.ProcessorResult;
import com.tencentcloudapi.tms.v20201229.models.TextModerationResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 腾讯云 TMS 响应转换器
 *
 * <p>职责：
 * <ul>
 *     <li>将腾讯云 TMS 响应转换为系统统一审核结果 ProcessorResult</li>
 *     <li>屏蔽云厂商差异（Suggestion / Label / Score 等）</li>
 *     <li>提供结构化审核结果</li>
 * </ul>
 *
 * @author SunTao
 * @since 2026-07-05
 */
@Component
public class TencentTmsConverter {
    public ProcessorResult convert(TextModerationResponse resp) {
        ProcessorResult result = new ProcessorResult();
        if (resp == null) {
            result.setDecision(AuditDecision.MANUAL);
            result.setReason("TMS_RESPONSE_NULL");
            result.setHitWords(Collections.emptyList());
            return result;
        }

        String suggestion = resp.getSuggestion();
        switch (safeUpper(suggestion)) {
            case "PASS":
                result.setDecision(AuditDecision.PASS);
                result.setHitWords(Collections.emptyList());
                result.setReason(buildReason("PASS", resp));
                break;
            case "REVIEW":
                result.setDecision(AuditDecision.MANUAL);
                result.setHitWords(safeList(resp.getKeywords()));
                result.setReason(buildReason("REVIEW", resp));
                break;
            case "BLOCK":
                result.setDecision(AuditDecision.REJECT);
                result.setHitWords(safeList(resp.getKeywords()));
                result.setReason(buildReason("BLOCK", resp));
                break;
            default:
                // 未知结果 → 默认人工审核（安全策略）
                result.setDecision(AuditDecision.MANUAL);
                result.setHitWords(safeList(resp.getKeywords()));
                result.setReason("UNKNOWN_SUGGESTION: " + suggestion);
        }

        return result;
    }

    /**
     * 安全处理 suggestion（避免 null / 大小写问题）
     */
    private String safeUpper(String str) {
        return str == null ? "" : str.toUpperCase();
    }

    /**
     * 安全处理关键词列表（防 null）
     */
    private List<String> safeList(String[] keywords) {
        if (keywords == null || keywords.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(keywords);
    }

    /**
     * 构建统一 reason（结构化日志）
     */
    private String buildReason(String type, TextModerationResponse resp) {
        return String.format(
            "TMS_%s RequestId=%s Label=%s SubLabel=%s Score=%s",
            type,
            resp.getRequestId(),
            resp.getLabel(),
            resp.getSubLabel(),
            resp.getScore()
        );
    }
}
