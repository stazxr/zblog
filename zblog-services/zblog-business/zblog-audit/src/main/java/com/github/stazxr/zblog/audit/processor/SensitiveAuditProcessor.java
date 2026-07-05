package com.github.stazxr.zblog.audit.processor;

import com.github.stazxr.zblog.audit.config.properties.SensitiveProcessorConfig;
import com.github.stazxr.zblog.audit.enums.SensitiveStrategy;
import com.github.stazxr.zblog.audit.model.AuditContext;
import com.github.stazxr.zblog.audit.model.ProcessorResult;
import com.github.stazxr.zblog.audit.provider.SensitiveProvider;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 敏感词审核处理器
 *
 * @author Sun Tao
 * @since 2026-07-05
 */
@Component
public class SensitiveAuditProcessor extends AbstractAuditProcessor {
    private static final SensitiveStrategy DEFAULT_STRATEGY = SensitiveStrategy.MANUAL;

    private final SensitiveProvider sensitiveProvider;

    private final SensitiveProcessorConfig sensitiveProcessorConfig;

    public SensitiveAuditProcessor(SensitiveProvider sensitiveProvider, SensitiveProcessorConfig sensitiveProcessorConfig) {
        this.sensitiveProvider = sensitiveProvider;
        this.sensitiveProcessorConfig = sensitiveProcessorConfig;
    }

    @Override
    public String name() {
        return "sensitiveProcessor";
    }

    @Override
    public ProcessorResult process(AuditContext context) {
        String content = context.getContent();
        if (content == null || content.isEmpty()) {
            return pass();
        }

        // =========================
        // 1. 是否包含敏感词
        // =========================
        boolean contains = sensitiveProvider.contains(content);
        if (!contains) {
            return pass();
        }

        // =========================
        // 2. 获取命中的敏感词
        // =========================
        List<String> hitWords = sensitiveProvider.findAll(content);
        if (hitWords == null) {
            hitWords = Collections.emptyList();
        }

        // =========================
        // 3. 获取处理策略
        // =========================
        Map<String, SensitiveStrategy> strategyMap = sensitiveProcessorConfig.getStrategyMap();
        SensitiveStrategy strategy = sensitiveProcessorConfig.getStrategy();
        String sceneKey = context.getScene().name() + ".strategy";
        if (strategyMap != null && strategyMap.containsKey(sceneKey)) {
            strategy = strategyMap.get(sceneKey);
        }
        if (strategy == null) {
            strategy = DEFAULT_STRATEGY;
        }

        // =========================
        // 4. 根据策略配置返回对应的结果
        // =========================
        switch (strategy) {
            case REPLACE:
                String replaced = sensitiveProvider.replace(content);
                return modify(replaced, hitWords);
            case REJECT:
                return reject("敏感词检测命中", hitWords);
            case MANUAL:
                return manual("敏感词检测命中", hitWords);
        }

        // 默认拒绝
        return reject("敏感词检测命中", hitWords);
    }

    @Override
    public boolean support(AuditContext context) {
        return true;
    }
}
