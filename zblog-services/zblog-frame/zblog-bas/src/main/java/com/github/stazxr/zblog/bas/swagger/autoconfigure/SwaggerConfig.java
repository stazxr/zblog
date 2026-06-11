package com.github.stazxr.zblog.bas.swagger.autoconfigure;

import com.github.stazxr.zblog.bas.swagger.autoconfigure.properties.SwaggerProperties;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置，
 * 文档访问地址：${baseUrl}/doc.html#/home
 *
 * @author SunTao
 * @since 2023-03-19
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "zblog.base.swagger.enable", havingValue = "true")
public class SwaggerConfig {
}
