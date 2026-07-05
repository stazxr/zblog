package com.github.stazxr.zblog.audit.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 腾讯云 TMS 配置属性类。
 *
 * @author SunTao
 * @since 2026-07-03
 */
@ConfigurationProperties(prefix = "audit.processors.config.tencent-tms-processor")
public class TencentTmsProcessorProperties {
    /**
     * 是否启用腾讯云内容审核
     */
    private boolean enabled = true;

    /**
     * 腾讯云 SecretId（访问密钥 ID）
     */
    private String secretId;

    /**
     * 腾讯云 SecretKey（访问密钥）
     */
    private String secretKey;

    /**
     * 腾讯云 TMS 所在地域
     */
    private String region = "ap-beijing";

    /**
     * 腾讯云 TMS 接口地址
     *
     * <p>默认：
     * tms.tencentcloudapi.com
     */
    private String endpoint = "tms.tencentcloudapi.com";

    /**
     * 识别策略编号（默认）
     */
    private String bizType;

    /**
     * 识别策略编号（场景定制）
     */
    private Map<String, String> bizTypeMap;

    /**
     * 连接超时时间（毫秒）
     */
    private int connectTimeout = 1000;

    /**
     * 读取超时时间（毫秒）
     */
    private int readTimeout = 2000;

    /**
     * 是否开启失败降级
     */
    private boolean degradeOnFailure = true;

    /**
     * 最大文本长度限制
     */
    private int maxTextLength = 5000;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public Map<String, String> getBizTypeMap() {
        return bizTypeMap;
    }

    public void setBizTypeMap(Map<String, String> bizTypeMap) {
        this.bizTypeMap = bizTypeMap;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isDegradeOnFailure() {
        return degradeOnFailure;
    }

    public void setDegradeOnFailure(boolean degradeOnFailure) {
        this.degradeOnFailure = degradeOnFailure;
    }

    public int getMaxTextLength() {
        return maxTextLength;
    }

    public void setMaxTextLength(int maxTextLength) {
        this.maxTextLength = maxTextLength;
    }
}