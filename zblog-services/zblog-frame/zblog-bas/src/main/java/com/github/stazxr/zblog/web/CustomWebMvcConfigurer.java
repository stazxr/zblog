package com.github.stazxr.zblog.web;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.github.stazxr.zblog.bas.reqsinglepost.SingleParamHandlerMethodArgumentResolver;
import com.github.stazxr.zblog.bas.validation.autoconfigure.ValidationAutoConfiguration;
import com.github.stazxr.zblog.web.serializer.LongToStringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

/**
 *
 *
 * @author SunTao
 * @since 2025-08-13
 */
@EnableWebMvc
@Configuration
@AutoConfigureAfter(ValidationAutoConfiguration.class)
public class CustomWebMvcConfigurer implements WebMvcConfigurer {
    private LocalValidatorFactoryBean validatorFactoryBean;

    /**
     * 跨域支持
     *
     * @return CorsFilter
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
        // 创建 fastJson 消息转换器
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
        serializeConfig.put(Long.class , LongToStringSerializer.INSTANCE);
        serializeConfig.put(Long.TYPE , LongToStringSerializer.INSTANCE);
        fastJsonConfig.setSerializeConfig(serializeConfig);

        // 修改配置返回内容的过滤
        // WriteBigDecimalAsPlain：把大数字转换为 String
        // WriteNullListAsEmpty：List 字段如果为 null, 输出为 [], 而非 null
        // WriteNullStringAsEmpty：字符类型字段如果为 null, 输出为 "", 而非 null
        // DisableCircularReferenceDetect：消除对同一对象循环引用的问题，默认为 false（如果不配置有可能会进入死循环）
        // WriteNullBooleanAsFalse：Boolean 字段如果为 null, 输出为 false, 而非 null
        // WriteMapNullValue：是否输出值为 null 的字段, 默认为 false
        fastJsonConfig.setSerializerFeatures(
                DisableCircularReferenceDetect,
                WriteMapNullValue
        );

        // 将fastjson添加到视图消息转换器列表内
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SingleParamHandlerMethodArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    @Override
    public Validator getValidator() {
        return validatorFactoryBean;
    }

    @Autowired
    public void setValidatorFactoryBean(@Nullable LocalValidatorFactoryBean validatorFactoryBean) {
        this.validatorFactoryBean = validatorFactoryBean;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }
}
