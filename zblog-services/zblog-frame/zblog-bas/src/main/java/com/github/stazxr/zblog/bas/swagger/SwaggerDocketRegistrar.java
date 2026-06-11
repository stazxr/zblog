package com.github.stazxr.zblog.bas.swagger;

import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.swagger.autoconfigure.properties.SwaggerProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Swagger Docket 动态注册器
 *
 * <p>用于在 Spring 容器启动阶段，自动扫描系统中所有 {@link com.github.stazxr.zblog.bas.router.ApiVersion}
 * 注解标记的接口版本信息，并基于扫描结果动态注册 Swagger Docket 分组。</p>
 *
 * @author SunTao
 * @since 2026-06-12
 */
@Component
@RequiredArgsConstructor
public class SwaggerDocketRegistrar implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(SwaggerDocketRegistrar.class);

    private final SwaggerProperties swaggerProperties;

    private final RequestMappingHandlerMapping handlerMapping;

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) {
        this.beanFactory = ((ConfigurableApplicationContext) context).getBeanFactory();
    }

    @PostConstruct
    public void init() {
        if (Boolean.FALSE.equals(swaggerProperties.getEnable())) {
            return;
        }

        // 默认分组
        beanFactory.registerSingleton("swagger-default", defaultDocket());

        // 自动扫描版本
        Set<String> versions = scanApiVersions();
        log.info("registrar docket list: {}", versions);
        versions.forEach(v -> beanFactory.registerSingleton("swagger-" + v, createDocket(v)));
    }

    /**
     * 扫描所有 @ApiVersion
     */
    private Set<String> scanApiVersions() {
        TreeSet<String> collect = handlerMapping.getHandlerMethods()
            .values()
            .stream()
            .map(this::extractVersion)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(TreeSet::new));
        collect.remove("default");
        return collect;
    }

    /**
     * 提取版本号（类 + 方法）
     */
    private String extractVersion(HandlerMethod handlerMethod) {
        // 方法优先
        ApiVersion methodAnno = handlerMethod.getMethodAnnotation(ApiVersion.class);
        if (methodAnno != null) {
            return methodAnno.value();
        }

        return null;
    }

    public Docket defaultDocket() {
        return new Docket(DocumentationType.OAS_30)
            .enable(swaggerProperties.getEnable())
            .apiInfo(apiInfo())
            .groupName("default")
            .select()
            .apis(RequestHandlerSelectors.withMethodAnnotation(Router.class))
            .paths(PathSelectors.any())
            .build();
    }

    private Docket createDocket(String version) {
        return new Docket(DocumentationType.OAS_30)
            .enable(swaggerProperties.getEnable())
            .apiInfo(apiInfo())
            .groupName(version)
            .select()
            .apis(input -> {
                if (input.isAnnotatedWith(ApiVersion.class)) {
                    Optional<ApiVersion> annotation = input.findAnnotation(ApiVersion.class);
                    if (annotation.isPresent()) {
                        return annotation.get().value().equals(version);
                    }
                }
                return false;
            })
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title(swaggerProperties.getTitle())
            .description(swaggerProperties.getDescription())
            .version(swaggerProperties.getVersion())
            .termsOfServiceUrl(swaggerProperties.getServerUrl())
            .license(swaggerProperties.getLicense())
            .licenseUrl(swaggerProperties.getLicenseUrl())
            .contact(contact())
            .build();
    }

    private Contact contact() {
        SwaggerProperties.Contact contact = swaggerProperties.getContact();
        String name = contact.getName();
        String url = contact.getUrl();
        String email = contact.getEmail();
        return new Contact(name, url, email);
    }
}