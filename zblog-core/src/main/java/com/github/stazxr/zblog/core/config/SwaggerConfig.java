package com.github.stazxr.zblog.core.config;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.core.config.properties.SwaggerConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(SwaggerConfigProperties.class)
@ConditionalOnProperty(name = "swagger.config.enable", havingValue = "true")
public class SwaggerConfig {
    @Bean
    public SwaggerConfigProperties swaggerConfigProperties() {
        return new SwaggerConfigProperties();
    }

    @Bean
    public Docket createRestApi() {
        log.info("init swagger: {}", JSON.toJSONString(swaggerConfigProperties()));
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerConfigProperties().getBasePackage()))
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
                .title(swaggerConfigProperties().getTitle())
                .description(swaggerConfigProperties().getDescription())
                .contact(contact())
                .version(swaggerConfigProperties().getVersion())
                .build();
    }

    private Contact contact() {
        SwaggerConfigProperties.Contact contact = swaggerConfigProperties().getContact();
        String name = contact.getName();
        String url = contact.getUrl();
        String email = contact.getEmail();
        return new Contact(name, url, email);
    }
}
