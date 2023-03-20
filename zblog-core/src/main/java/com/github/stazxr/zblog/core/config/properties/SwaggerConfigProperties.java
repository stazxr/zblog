package com.github.stazxr.zblog.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SwaggerProperties
 *
 * @author SunTao
 * @since 2023-03-19
 */
@Data
@ConfigurationProperties(prefix= SwaggerConfigProperties.SWAGGER_PREFIX)
public class SwaggerConfigProperties {
    static final String SWAGGER_PREFIX= "swagger.config";

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
    private String version = "V_0.0.1";

    /**
     * 服务地址
     */
    private String serverUrl = "https://www.suntaoblog.com";

    /**
     * 接口扫描路径
     */
    private String basePackage = "com.github.stazxr.zblog";

    /**
     * 作者联系方式
     */
    private Contact contact = new Contact();

    @Data
    public static class Contact {
        /**
         * contact name
         */
        private String name = "孙涛";

        /**
         * contact url
         */
        private String url = "https://www.suntaoblog.com";

        /**
         * contact email
         */
        private String email = "stazxr@qq.com";
    }
}