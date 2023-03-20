package com.github.stazxr.zblog.core.config;

import com.alibaba.fastjson.JSON;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.config.properties.SwaggerConfigProperties;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Optional;

/**
 * Swagger配置，
 * 文档访问地址：${baseUrl}/doc.html#/home
 *
 * @author SunTao
 * @since 2023-03-19
 */
@Slf4j
@Configuration
@EnableSwagger2
@EnableKnife4j
@EnableConfigurationProperties(SwaggerConfigProperties.class)
@ConditionalOnProperty(name = "swagger.config.enable", havingValue = "true")
public class SwaggerConfig {
    private static final String DEFAULT_GROUP = "默认分组";

    @Bean
    public SwaggerConfigProperties swaggerConfigProperties() {
        SwaggerConfigProperties properties = new SwaggerConfigProperties();
        log.info("init swagger: {}", JSON.toJSONString(properties));
        return properties;
    }

    @Bean
    public Docket defaultApi() {
        return new Docket(DocumentationType.OAS_30)
            .enable(swaggerConfigProperties().getEnable())
            .apiInfo(apiInfo())
            .groupName(DEFAULT_GROUP)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .paths(PathSelectors.any())
            .build();
    }

    @Bean
    public Docket v4g0l0() {
        return new Docket(DocumentationType.OAS_30)
            .enable(swaggerConfigProperties().getEnable())
            .apiInfo(apiInfo())
            .groupName(BaseConst.ApiVersion.V_4_0_0)
            .select()
            .apis((input) -> {
                if (input.isAnnotatedWith(ApiVersion.class)) {
                    Optional<ApiVersion> annotation = input.findAnnotation(ApiVersion.class);
                    if (annotation.isPresent()) {
                        return Arrays.asList(annotation.get().group()).contains(BaseConst.ApiVersion.V_4_0_0);
                    }
                }
                return false;
            })
            .paths(PathSelectors.any())
            .build();
    }

    @Bean
    public Docket v4g1l0() {
        return new Docket(DocumentationType.OAS_30)
            .enable(swaggerConfigProperties().getEnable())
            .apiInfo(apiInfo())
            .groupName(BaseConst.ApiVersion.V_4_1_0)
            .select()
            .apis(input -> {
                if (input.isAnnotatedWith(ApiVersion.class)) {
                    Optional<ApiVersion> annotation = input.findAnnotation(ApiVersion.class);
                    if (annotation.isPresent()) {
                        return Arrays.asList(annotation.get().group()).contains(BaseConst.ApiVersion.V_4_1_0);
                    }
                }
                return false;
            })
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title(swaggerConfigProperties().getTitle())
            .description(swaggerConfigProperties().getDescription())
            .termsOfServiceUrl(swaggerConfigProperties().getServerUrl())
            .version(swaggerConfigProperties().getVersion())
            .contact(contact())
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
