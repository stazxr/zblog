package com.github.stazxr.zblog.audit.config.properties;

import com.github.stazxr.zblog.audit.enums.XssStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * XssProcessor 配置
 *
 * @author Sun Tao
 * @since 2026-07-05
 */
@ConfigurationProperties(prefix = "audit.processors.config.xss-processor")
public class XssProcessorConfig {
    /**
     * 执行策略（默认）
     */
    private XssStrategy strategy;

    /**
     * 执行策略（场景定制）
     */
    private Map<String, XssStrategy> strategyMap;

    public XssStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(XssStrategy strategy) {
        this.strategy = strategy;
    }

    public Map<String, XssStrategy> getStrategyMap() {
        return strategyMap;
    }

    public void setStrategyMap(Map<String, XssStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }
}