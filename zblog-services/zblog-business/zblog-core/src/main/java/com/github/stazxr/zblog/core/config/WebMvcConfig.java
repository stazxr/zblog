package com.github.stazxr.zblog.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfigurer
 *
 * @author SunTao
 * @since 2020-11-14
 */
@Slf4j
@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
        "classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/"
    };

    /**
     * 本地文件的访问地址
     */
    @Value("${zblog.file-domain}")
    private String fileDomain;

    /**
     * 本地文件上传目录
     */
    @Value("${zblog.file-upload-path}")
    private String fileUploadPath;



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // local file
        log.info("Local File Config ===> fileDomain: {}, fileUploadPath: {}", fileDomain, fileUploadPath);
        registry.addResourceHandler(fileDomain).addResourceLocations("file:" + fileUploadPath);

        // resource
        final String fullPattern = "/**";
        if (!registry.hasMappingForPattern(fullPattern)) {
            registry.addResourceHandler(fullPattern).addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
        }

        // swagger
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
