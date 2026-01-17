package com.github.stazxr.zblog.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 站点配置信息
 *
 * @author SunTao
 * @since 2026-01-14
 */
@ConfigurationProperties(prefix= WebsiteProperties.WEBSITE_PREFIX)
public class WebsiteProperties {
    static final String WEBSITE_PREFIX= "zblog.website";

    /**
     * 站点名称
     */
    private String name;

    /**
     * 站点地址
     */
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
