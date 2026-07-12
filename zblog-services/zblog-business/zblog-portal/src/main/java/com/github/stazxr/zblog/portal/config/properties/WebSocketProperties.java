package com.github.stazxr.zblog.portal.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebSocket配置
 *
 * @author SunTao
 * @since 2027-07-12
 */
@ConfigurationProperties(prefix = WebSocketProperties.CONFIG_PREFIX)
public class WebSocketProperties {
    static final String CONFIG_PREFIX = "zblog.websocket";

    /**
     * WebSocket Endpoint
     */
    private String endpoint = "/ws";

    /**
     * Broker 前缀
     */
    private String brokerPrefix = "/topic";

    /**
     * 允许跨域
     */
    private String[] allowedOriginPatterns = {"*"};

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBrokerPrefix() {
        return brokerPrefix;
    }

    public void setBrokerPrefix(String brokerPrefix) {
        this.brokerPrefix = brokerPrefix;
    }

    public String[] getAllowedOriginPatterns() {
        return allowedOriginPatterns;
    }

    public void setAllowedOriginPatterns(String[] allowedOriginPatterns) {
        this.allowedOriginPatterns = allowedOriginPatterns;
    }
}