package com.github.stazxr.zblog.bas.i18n.autoconfigure;

import com.github.stazxr.zblog.bas.i18n.I18nUtils;
import com.github.stazxr.zblog.bas.i18n.autoconfigure.properties.I18nProperties;
import com.github.stazxr.zblog.bas.i18n.source.CompositeMessageSource;
import com.github.stazxr.zblog.bas.i18n.source.DatabaseMessageSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.sql.DataSource;

/**
 * 国际化（i18n）自动配置类
 *
 * <p>该类自动装配一个支持“数据库 + 资源文件”双源的 {@link MessageSource}，
 * 以便应用可以同时从数据库和本地资源文件加载多语言内容。
 *
 * <ul>
 *   <li>数据库来源：通过 {@link DatabaseMessageSource} 从数据表加载</li>
 *   <li>资源文件来源：通过 {@link ResourceBundleMessageSource} 从 classpath 加载</li>
 *   <li>合并策略：使用 {@link CompositeMessageSource} 优先读取数据库内容，缺失时回退到文件</li>
 * </ul>
 *
 * <p>该配置依赖 {@link DataSourceAutoConfiguration}，因此需要先配置好 DataSource。
 *
 * @author SunTao
 * @since 2025-08-10
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(I18nProperties.class)
public class I18nAutoConfiguration {
    /**
     * 创建基于数据库的 MessageSource
     *
     * @param dataSource 数据源
     * @param properties 国际化配置属性
     * @return 数据库 MessageSource
     */
    @Bean
    @DependsOn("dataSource")
    public MessageSource dbMessageSource(DataSource dataSource, I18nProperties properties) {
        return new DatabaseMessageSource(dataSource, properties);
    }

    /**
     * 创建基于 ResourceBundle 的 MessageSource
     *
     * <p>优先从配置文件 {@link I18nProperties} 中读取 basename，否则使用默认值。
     *
     * @param properties 国际化配置属性
     * @return 资源文件 MessageSource
     */
    @Bean
    public MessageSource bundleMessageSource(I18nProperties properties) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(properties.getBasenames().toArray(new String[0]));
        messageSource.setDefaultEncoding(properties.getEncode());
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(true); // code 兜底
        return messageSource;
    }

    /**
     * 创建组合型 MessageSource
     *
     * <p>将数据库和文件两种 MessageSource 合并为一个，并注册到 {@link I18nUtils} 供全局调用。
     *
     * @param dbMessageSource      数据库 MessageSource（主）
     * @param bundleMessageSource  资源文件 MessageSource（备）
     * @return 组合型 MessageSource
     */
    @Bean("messageSource")
    @DependsOn({"dbMessageSource", "bundleMessageSource"})
    public MessageSource messageSource(
            @Qualifier("dbMessageSource") MessageSource dbMessageSource,
            @Qualifier("bundleMessageSource") MessageSource bundleMessageSource) {
        CompositeMessageSource compositeMessageSource = new CompositeMessageSource(dbMessageSource, bundleMessageSource);

        // 将组合消息源注册到工具类，方便全局静态调用
        I18nUtils.setMessageSource(compositeMessageSource);

        return compositeMessageSource;
    }
}
