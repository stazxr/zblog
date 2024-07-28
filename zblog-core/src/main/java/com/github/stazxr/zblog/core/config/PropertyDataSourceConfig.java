package com.github.stazxr.zblog.core.config;

import com.github.stazxr.zblog.core.config.props.DatabasePropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

/**
 * PropertyDataSourceConfig 类是用于配置从数据库获取属性数据源的配置类。
 * 该类提供了一个 bean 来配置 PropertySourcesPlaceholderConfigurer，
 * 允许从数据库使用 DatabasePropertySource 获取属性。
 *
 * @author SunTao
 * @since 2024-07-21
 */
@Configuration
public class PropertyDataSourceConfig {
    /**
     * 提供一个 PropertySourcesPlaceholderConfigurer bean 配置。
     * 该 bean 解析 bean 属性值和 @Value 注解中的占位符，
     * 根据当前的 Spring Environment 和其 PropertySource 集合进行解析。
     * 添加名为 "databaseProperties" 的 DatabasePropertySource 作为第一个属性源。
     *
     * @param environment 用于设置配置器的 Spring ConfigurableEnvironment。
     * @return 配置好的 PropertySourcesPlaceholderConfigurer bean。
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(ConfigurableEnvironment environment) {
        // add databaseProperties at first
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new DatabasePropertySource("databaseProperties"));

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setEnvironment(environment);
        configurer.setIgnoreUnresolvablePlaceholders(false);
        return configurer;
    }
}
