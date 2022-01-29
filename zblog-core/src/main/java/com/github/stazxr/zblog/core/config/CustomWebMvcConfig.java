package com.github.stazxr.zblog.core.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * WebMvcConfigurer
 *
 * @author SunTao
 * @since 2020-11-14
 */
@EnableWebMvc
@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {
    /**
     * 配置使用 fastjson 进行 json 解析
     *
     * @param converters HttpMessageConverters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 定义一个 convert 转换消息的对象
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        // 添加 fastJson 的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                PrettyFormat, WriteNullListAsEmpty, WriteNullStringAsEmpty
        );
        converter.setFastJsonConfig(fastJsonConfig);

        // 将 convert 添加到 converters 当中
        converters.add(converter);
    }
}
