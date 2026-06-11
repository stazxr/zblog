package com.github.stazxr.zblog.bas.swagger.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger配置项
 *
 * @author SunTao
 * @since 2023-03-19
 */
@ConfigurationProperties(prefix= SwaggerProperties.SWAGGER)
public class SwaggerProperties {
    static final String SWAGGER = "zblog.base.swagger";

    /**
     * 是否启用接口文档
     */
    private Boolean enable = Boolean.FALSE;

    /**
     * 接口文档标题
     */
    private String title = "Z-BLOG 接口文档";

    /**
     * 接口文档描述
     */
    private String description = "Z-BLOG 是一款基于 MIT 协议的前后端分离博客框架，同时也可用作为前后端分离脚手架，快速进行项目搭建，Git链接：https://github.com/stazxr/zblog";

    /**
     * 接口文档版本
     */
    private String version = "1.0";

    /**
     * 服务地址
     */
    private String serverUrl = "http://localhost:8081";

    /**
     * 证书
     */
    private String license = "MIT License";

    /**
     * 证书地址
     */
    private String licenseUrl = "https://github.com/stazxr/zblog/blob/master/LICENSE";

    /**
     * 作者联系方式
     */
    private Contact contact = new Contact();

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public static class Contact {
        /**
         * contact name
         */
        private String name = "孙涛";

        /**
         * contact url
         */
        private String url = "https://github.com/stazxr";

        /**
         * contact email
         */
        private String email = "stazxr@qq.com";

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}