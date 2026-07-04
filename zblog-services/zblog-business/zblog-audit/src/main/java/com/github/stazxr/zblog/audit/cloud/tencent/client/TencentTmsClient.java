package com.github.stazxr.zblog.audit.cloud.tencent.client;

import com.github.stazxr.zblog.audit.cloud.tencent.config.TencentTmsProperties;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tms.v20201229.TmsClient;
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
    private final TencentTmsProperties properties;

    /**
     * SDK Client（线程安全，可复用）
     */
    private TmsClient client;

    public TencentTmsClient(TencentTmsProperties properties) {
        this.properties = properties;
    }

    /**
     * 初始化 Tencent SDK Client
     */
    @PostConstruct
    public void init() {
        if (!properties.isEnabled()) {
            return;
        }

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
    }

    /**
     * 是否可用
     */
    public boolean available() {
        return client != null && properties.isEnabled();
    }

    public TmsClient getClient() {
        return client;
    }
}
