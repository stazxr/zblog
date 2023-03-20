package com.github.stazxr.zblog.core.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.github.stazxr.zblog.core.config.rest.SingleParamHandlerMethodArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

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
     * 文件的访问地址
     */
    @Value("${zblog.file-domain}")
    private String fileDomain;

    /**
     * 文件上传目录
     */
    @Value("${zblog.file-upload-path}")
    private String fileUploadPath;

    /**
     * 跨域支持
     *
     * @return CorsFilter Config
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addExposedHeader("new-token");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * 配置使用 fastjson 进行 json 解析
     *
     * @param converters HttpMessageConverters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建fastJson消息转换器
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);

        // 创建配置类
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        // Long -> String
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(Long.class , ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE , ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);

        // 修改配置返回内容的过滤
        // WriteBigDecimalAsPlain：把大数字转换为String
        // WriteNullListAsEmpty：List字段如果为null, 输出为[],而非null
        // WriteNullStringAsEmpty： 字符类型字段如果为null,输出为"",而非null
        // DisableCircularReferenceDetect：消除对同一对象循环引用的问题，默认为false（如果不配置有可能会进入死循环）
        // WriteNullBooleanAsFalse：Boolean字段如果为null,输出为false,而非null
        // WriteMapNullValue：是否输出值为null的字段, 默认为false
        fastJsonConfig.setSerializerFeatures(
            DisableCircularReferenceDetect,
            WriteMapNullValue
        );

        // 将fastjson添加到视图消息转换器列表内
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
    }

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

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SingleParamHandlerMethodArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
