package com.github.stazxr.zblog.bas.i18n;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * 国际化（I18n）相关配置属性类。
 *
 * @author SunTao
 * @since 2025-08-10
 */
@Setter
@Getter
@ConfigurationProperties(prefix = I18nProperties.HEADER_PREFIX)
public class I18nProperties {
    static final String HEADER_PREFIX = "zblog.base.i18n";

    /**
     * 缓存刷新周期，单位毫秒，默认 60 分钟。
     * <p>
     * 说明：当达到周期时，会自动刷新数据库中的国际化缓存数据。
     */
    private long cacheTtl = 60 * 60 * 1000;

    /**
     * 从数据库加载国际化数据的 SQL 语句。
     * 默认结构为：SELECT code, locale, message FROM i18n_messages
     * <p>
     * 注意：SQL 语句必须包含三个字段，分别对应 code、locale、message。
     */
    private String loadSql = "SELECT code, locale, message FROM i18n_message";

    /**
     * 数据库中 "code" 列名。
     * <p>
     * 说明：如果数据库字段不是 "code"，可以在配置文件中修改此项。
     */
    private String codeColumn = "code";

    /**
     * 数据库中 "locale"（语言）列名。
     * <p>
     * 说明：值通常是 "en_US"、"zh_CN" 等。
     */
    private String localeColumn = "locale";

    /**
     * 数据库中 "message"（消息内容）列名。
     */
    private String messageColumn = "message";

    /**
     * 国际化文件的编码方式，默认 UTF-8。
     */
    private String encode = "UTF-8";

    /**
     * 本地资源文件的基础名列表（无扩展名）。
     * <p>
     * 说明：当数据库中未找到对应的消息时，会从这些资源文件中查找。
     */
    private List<String> basenames = Collections.singletonList("messages/messages");
}
