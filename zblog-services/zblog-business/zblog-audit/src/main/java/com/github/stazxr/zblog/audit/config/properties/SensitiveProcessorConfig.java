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
     * 默认策略
     */
    private SensitiveStrategy strategy;

    /**
     * 按场景覆盖策略
     */
    private Map<String, SensitiveStrategy> scenes;

    public SensitiveStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(SensitiveStrategy strategy) {
        this.strategy = strategy;
    }

    public Map<String, SensitiveStrategy> getScenes() {
        return scenes;
    }

    public void setScenes(Map<String, SensitiveStrategy> scenes) {
        this.scenes = scenes;
    }
}