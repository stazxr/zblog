package com.github.stazxr.zblog.bas.props.database;

import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

/**
 * 自定义的 ApplicationContextInitializer 实现类，用于在 Spring Boot 应用启动时
 * 初始化配置环境，并将数据库读取的属性源添加到环境中。
 *
 * @author SunTao
 * @since 2024-08-04
 */
public class DatabasePropertySourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
    /**
     * 初始化方法，会在 Spring Boot 应用上下文启动时调用。
     *
     * @param applicationContext 传入的应用上下文，包含当前的环境配置。
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // 获取当前应用上下文的环境配置
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        // 将自定义的属性源附加到环境中
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addAfter("configurationProperties", new DatabasePropertySource("databaseProperties"));
        ConfigurationPropertySources.attach(environment);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
