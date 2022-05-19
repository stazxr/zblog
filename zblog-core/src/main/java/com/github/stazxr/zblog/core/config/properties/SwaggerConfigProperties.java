package com.github.stazxr.zblog.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SwaggerProperties
 *
 * @author SunTao
 * @since 2022-01-28
 */
@Data
@ConfigurationProperties(prefix= SwaggerConfigProperties.SWAGGER_PREFIX)
public class SwaggerConfigProperties {
    static final String SWAGGER_PREFIX= "swagger.config";

    /**
     * 是否启用
     */
    private Boolean enable = Boolean.FALSE;

    /**
     * 标题
     */
    private String title = "zblog API文档";

    /**
     * 描述
     */
    private String description = "一个前后端分离的个人博客框架，Git链接：https://github.com/stazxr/zblog";

    /**
     * api version
     */
    private String version = "V_0.0.1";

    /**
     * 包扫描路径
     */
    private String basePackage = "com.github.stazxr.zblog";

    /**
     * 联系方式
     */
    private Contact contact;

    @Data
    public static class Contact {
        /**
         * contact name
         */
        private String name = "孙涛";

        /**
         * contact url
         */
        private String url = "https://www.suntaoblog.com/";

        /**
         * contact email
         */
        private String email = "stazxr@qq.com";
    }
}
