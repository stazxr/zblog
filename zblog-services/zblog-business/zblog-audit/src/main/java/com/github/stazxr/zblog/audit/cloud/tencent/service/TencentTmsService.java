package com.github.stazxr.zblog.audit.cloud.tencent.service;

import com.github.stazxr.zblog.audit.cloud.tencent.client.TencentTmsClient;
import com.github.stazxr.zblog.audit.config.properties.TencentTmsProcessorProperties;
import com.github.stazxr.zblog.util.StringUtils;
import com.tencentcloudapi.tms.v20201229.models.TextModerationRequest;
import com.tencentcloudapi.tms.v20201229.models.TextModerationResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 腾讯云 TMS 内容审核服务
 *
 * @author SunTao
 * @since 2026-07-03
 */
@Service
@RequiredArgsConstructor
public class TencentTmsService {
    private static final Logger log = LoggerFactory.getLogger(TencentTmsService.class);

    private final TencentTmsClient tmsClient;

    private final TencentTmsProcessorProperties tmsProperties;

    /**
     * 文本审核
     */
    public TextModerationResponse moderateText(String content, String bizType) {
        // 开关控制
        if (!tmsProperties.isEnabled()) {
            log.debug("TMS已关闭，跳过审核");
            return null;
        }

        if (StringUtils.isBlank(content)) {
            return null;
        }

        // 长度控制
        if (content.length() > tmsProperties.getMaxTextLength()) {
            content = content.substring(0, tmsProperties.getMaxTextLength());
        }

        try {
            TextModerationRequest req = new TextModerationRequest();
            String encodedContent = Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
            req.setContent(encodedContent);
            if (StringUtils.isNotBlank(bizType)) {
                req.setBizType(bizType); // 识别策略编号
            }
            return tmsClient.getClient().TextModeration(req);
        } catch (Exception e) {
            log.error("腾讯TMS内容审核失败，内容：{}", content, e);
            // 降级策略（关键）
            if (tmsProperties.isDegradeOnFailure()) {
                return null;
            }
            throw new IllegalStateException("腾讯云 TMS 审核失败", e);
        }
    }
}
