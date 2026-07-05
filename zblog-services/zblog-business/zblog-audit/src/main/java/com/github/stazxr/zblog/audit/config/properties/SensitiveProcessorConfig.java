package com.github.stazxr.zblog.audit.config.properties;

import com.github.stazxr.zblog.audit.enums.SensitiveStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * SensitiveProcessor 配置
 *
 * @author Sun Tao
 * @since 2026-07-05
 */
@ConfigurationProperties(prefix = "audit.processors.config.sensitive-processor")
public class SensitiveProcessorConfig {
    /**
     * 执行策略（默认）
     */
    private SensitiveStrategy strategy;

    /**
     * 执行策略（场景定制）
     */
    private Map<String, SensitiveStrategy> strategyMap;

    public SensitiveStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(SensitiveStrategy strategy) {
        this.strategy = strategy;
    }

    public Map<String, SensitiveStrategy> getStrategyMap() {
        return strategyMap;
    }

    public void setStrategyMap(Map<String, SensitiveStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }
}