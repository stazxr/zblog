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
     * 默认策略
     */
    private XssStrategy strategy;

    /**
     * 按场景覆盖策略
     */
    private Map<String, XssStrategy> scenes;

    public XssStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(XssStrategy strategy) {
        this.strategy = strategy;
    }

    public Map<String, XssStrategy> getScenes() {
        return scenes;
    }

    public void setScenes(Map<String, XssStrategy> scenes) {
        this.scenes = scenes;
    }
}