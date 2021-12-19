package com.github.stazxr.zblog.core.config;

import com.github.stazxr.zblog.core.base.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置
 *
 * @author SunTao
 * @since 2021-12-05
 */
@Slf4j
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class SwaggerConfig {
    private static final String API_VERSION = "4.0.0";

    @Bean
    public Docket createRestApi() {
        log.info("init swagger...");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(Const.BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 访问地址：http://项目地址/swagger-ui.html
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("zblog 接口文档")
                .description("一个前后端分离的个人博客框架，Git链接：https://github.com/stazxr/zblog")
                .contact(contact())
                .version(API_VERSION)
                .build();
    }

    private Contact contact() {
        String name = "SunTao";
        String url = "https://www.suntaoblog.com";
        String email = "stazxr@qq.com";
        return new Contact(name, url, email);
    }
}
