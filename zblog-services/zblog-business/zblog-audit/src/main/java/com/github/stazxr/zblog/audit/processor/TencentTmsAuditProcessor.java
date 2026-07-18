package com.github.stazxr.zblog.audit.processor;

import com.github.stazxr.zblog.audit.cloud.tencent.provider.TencentTmsProvider;
import com.github.stazxr.zblog.audit.config.properties.TencentTmsProcessorProperties;
import com.github.stazxr.zblog.audit.model.AuditContext;
import com.github.stazxr.zblog.audit.model.ProcessorResult;
import com.github.stazxr.zblog.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯云 TMS 审核处理器
 *
 * <p>
 * see <a href="https://console.cloud.tencent.com/cms/clouds/statistics/overview">审核结果可以在这里查看</a>”
 *
 * @author Sun Tao
 * @since 2026-07-05
 */
@Component
public class TencentTmsAuditProcessor extends AbstractAuditProcessor {
    private final TencentTmsProvider tencentTmsProvider;

    private final TencentTmsProcessorProperties tencentTmsProcessorProperties;

    public TencentTmsAuditProcessor(TencentTmsProvider tencentTmsProvider, TencentTmsProcessorProperties tencentTmsProcessorProperties) {
        this.tencentTmsProvider = tencentTmsProvider;
        this.tencentTmsProcessorProperties = tencentTmsProcessorProperties;
    }

    @Override
    public String name() {
        return "tencentTmsProcessor";
    }

    @Override
    public ProcessorResult process(AuditContext context) {
        // 1️⃣ 开关控制（修复：逻辑必须是 !enabled 才跳过）
        if (!tencentTmsProcessorProperties.isEnabled()) {
            return pass("TMS 未启用");
        }

        // 2️⃣ 内容校验
        String content = context.getContent();
        if (StringUtils.isBlank(content)) {
            return pass();
        }

        // 3️⃣ 解析 bizType（默认值兜底）
        String bizType = resolveBizType(context);

        // 4️⃣ 构造参数（扩展预留）
        Map<String, Object> params = buildParams(bizType);

        // 5️⃣ 调用云审核
        return tencentTmsProvider.checkText(content, params);
    }

    @Override
    public boolean support(AuditContext context) {
        return true;
    }

    /**
     * 解析 bizType
     */
    private String resolveBizType(AuditContext context) {
        String defaultBizType = tencentTmsProcessorProperties.getBizType();
        Map<String, String> bizTypeMap = tencentTmsProcessorProperties.getBizTypeMap();
        if (bizTypeMap == null || context.getScene() == null) {
            return defaultBizType;
        }
        String key = context.getScene().name() + ".bizType";
        return bizTypeMap.getOrDefault(key, defaultBizType);
    }

    /**
     * 构建扩展参数
     */
    private Map<String, Object> buildParams(String bizType) {
        Map<String, Object> params = new HashMap<>(2);
        if (StringUtils.isNotBlank(bizType)) {
            params.put(TencentTmsProvider.PARAM_BIZ_TYPE, bizType);
        }
        return params;
    }
}
