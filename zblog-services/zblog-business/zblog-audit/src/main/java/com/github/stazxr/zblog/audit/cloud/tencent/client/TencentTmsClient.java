package com.github.stazxr.zblog.audit.cloud.tencent.client;

import com.github.stazxr.zblog.audit.config.properties.TencentTmsProcessorProperties;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tms.v20201229.TmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 腾讯云 TMS Client 统一管理类
 *
 * @author SunTao
 * @since 2026-07-03
 */
@Component
public class TencentTmsClient {
    private static final Logger log = LoggerFactory.getLogger(TencentTmsClient.class);

    private final TencentTmsProcessorProperties properties;

    /**
     * SDK Client（线程安全，可复用）
     */
    private volatile TmsClient client;

    public TencentTmsClient(TencentTmsProcessorProperties properties) {
        this.properties = properties;
    }

    /**
     * 初始化 Tencent SDK Client
     */
    @PostConstruct
    public void init() {
        if (!properties.isEnabled()) {
            log.info("TMS 内容安全审核开关关闭，跳过初始化");
            return;
        }

        try {
            // Tencent Cloud Credential
            Credential credential = new Credential(properties.getSecretId(), properties.getSecretKey());

            // HttpProfile
            HttpProfile httpProfile = new HttpProfile();
            if (properties.getEndpoint() != null) {
                // endpoint 可配置化
                httpProfile.setEndpoint(properties.getEndpoint());
            } else {
                httpProfile.setEndpoint("tms.tencentcloudapi.com");
            }

            // 超时控制
            httpProfile.setConnTimeout(properties.getConnectTimeout());
            httpProfile.setReadTimeout(properties.getReadTimeout());

            // ClientProfile
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            this.client = new TmsClient(credential, properties.getRegion(), clientProfile);
            log.info("TmsClient 初始化成功，region={}, endpoint={}", properties.getRegion(), httpProfile.getEndpoint());
        } catch (Exception e) {
            log.error("region={}", properties.getRegion());
            log.error("endpoint={}", properties.getEndpoint());
            log.error("TmsClient 初始化失败，会影响后续的 TMS 内容安全审核", e);
        }
    }

    /**
     * 是否可用
     */
    public boolean available() {
        return properties.isEnabled() && client != null;
    }

    public TmsClient getClient() {
        if (available()) {
            return client;
        }

        throw new IllegalStateException("TmsClient 不可用");
    }
}
