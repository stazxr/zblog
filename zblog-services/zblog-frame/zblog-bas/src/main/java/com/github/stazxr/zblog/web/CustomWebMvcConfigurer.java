package com.github.stazxr.zblog.web;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import com.github.stazxr.zblog.bas.file.autoconfigure.FileAutoConfiguration;
import com.github.stazxr.zblog.bas.file.autoconfigure.properties.FileProperties;
import com.github.stazxr.zblog.bas.log.advice.ReqLogControlAdvice;
import com.github.stazxr.zblog.bas.validation.autoconfigure.ValidationAutoConfiguration;
import com.github.stazxr.zblog.web.serializer.LongToStringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * WebMvcConfigurer
 *
 * "@EnableWebMvc" 会导致：
 * 默认 CORS 配置失效
 * 默认 MessageConverter 失效
 * 自动配置关闭
 * WebMvcAutoConfiguration 失效
 *
 * @author SunTao
 * @since 2025-08-13
 */
@EnableWebMvc
@Configuration
@AutoConfigureAfter({ValidationAutoConfiguration.class, FileAutoConfiguration.class})
public class CustomWebMvcConfigurer implements WebMvcConfigurer {
    private static final Logger log = LoggerFactory.getLogger(CustomWebMvcConfigurer.class);

    private LocalValidatorFactoryBean validatorFactoryBean;

    @Resource
    private FileProperties fileProperties;

    @Resource
    private ReqLogControlAdvice reqLogControlAdvice;

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
        "classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/"
    };

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建 fastJson 消息转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        // 配置支持的 MediaType
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(supportedMediaTypes);

        // Long 类型序列化处理
        ObjectWriterProvider provider = JSONFactory.getDefaultObjectWriterProvider();
        provider.register(Long.class, LongToStringSerializer.INSTANCE);
        provider.register(Long.TYPE, LongToStringSerializer.INSTANCE);

        FastJsonConfig config = new FastJsonConfig();

        // fastjson2 序列化特性
        config.setWriterFeatures(
            JSONWriter.Feature.WriteMapNullValue
        );

        // 应用 FastJson 配置
        converter.setFastJsonConfig(config);

        // 加入 Spring MVC Converter 列表
        converters.add(0, converter);
    }

    @Override
    public Validator getValidator() {
        return validatorFactoryBean;
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // 本地文件上传
        String accessPath = fileProperties.getLocal().getFileAccessUrl();
        if (!accessPath.endsWith("/")) {
            accessPath += "/";
        }
        String storagePath = fileProperties.getLocal().getStoragePathPrefix();
        if (!storagePath.endsWith("/")) {
            storagePath += "/";
        }
        String fileUrlPathPrefix = accessPath.replaceFirst("^.+?://[^/]+", "") + "**";
        String fileLocalPathPrefix = "file:" + storagePath;
        log.info("[LocalFileConfiguration] {} >>> {}", fileLocalPathPrefix, fileUrlPathPrefix);
        registry.addResourceHandler(fileUrlPathPrefix).addResourceLocations(fileLocalPathPrefix).setCachePeriod(3600);

        // 静态资源
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);

        // Swagger
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(reqLogControlAdvice).addPathPatterns("/**");
    }

    @Autowired
    public void setValidatorFactoryBean(@Nullable LocalValidatorFactoryBean validatorFactoryBean) {
        this.validatorFactoryBean = validatorFactoryBean;
    }
}
