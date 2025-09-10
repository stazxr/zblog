package com.github.stazxr.zblog.bas.i18n.source;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Locale;

/**
 * CompositeMessageSource
 *
 * 用于多数据源国际化消息加载：
 * 1. 优先从主数据源(数据库)获取国际化消息
 * 2. 如果主数据源中不存在对应消息，则回退到 ResourceBundle 等备数据源
 *
 * 线程安全：只读访问的情况下线程安全，动态设置数据源时需确保外部同步。
 *
 * @author SunTao
 * @since 2025-08-10
 */
@Slf4j
public class CompositeMessageSource implements MessageSource {
    /**
     * 主数据源（通常是 DB）
     */
    private MessageSource primaryMessageSource;

    /**
     * 备数据源（通常是 ResourceBundleMessageSource）
     */
    private MessageSource fallbackMessageSource;

    public CompositeMessageSource(@NonNull MessageSource primaryMessageSource,
                                  @NonNull MessageSource fallbackMessageSource) {
        setPrimaryMessageSource(primaryMessageSource);
        setFallbackMessageSource(fallbackMessageSource);
    }

    /**
     * 获取国际化消息（带默认值版本）
     *
     * @param code           消息编码
     * @param args           占位符参数
     * @param defaultMessage 当找不到消息时返回的默认值
     * @param locale         区域
     * @return 国际化消息文本
     */
    @Override
    public String getMessage(@NonNull String code,
                             @Nullable Object[] args,
                             @Nullable String defaultMessage,
                             @NonNull Locale locale) {
        String message = primaryMessageSource.getMessage(code, args, null, locale);
        if (message != null) {
            return message;
        }
        return fallbackMessageSource.getMessage(code, args, defaultMessage, locale);
    }

    /**
     * 获取国际化消息（不带默认值版本）
     *
     * @throws org.springframework.context.NoSuchMessageException 如果找不到消息
     */
    @NonNull
    @Override
    public String getMessage(@NonNull String code,
                             @Nullable Object[] args,
                             @NonNull Locale locale) {
        try {
            return primaryMessageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            return fallbackMessageSource.getMessage(code, args, locale);
        }
    }

    /**
     * 通过 MessageSourceResolvable 获取国际化消息
     *
     * @throws org.springframework.context.NoSuchMessageException 如果找不到消息
     */
    @Override
    @NonNull
    public String getMessage(@NonNull MessageSourceResolvable resolvable,
                             @NonNull Locale locale) {
        try {
            return primaryMessageSource.getMessage(resolvable, locale);
        } catch (NoSuchMessageException e) {
            return fallbackMessageSource.getMessage(resolvable, locale);
        }
    }

    /**
     * 设置主数据源
     */
    public void setPrimaryMessageSource(@NonNull MessageSource primaryMessageSource) {
        this.primaryMessageSource = primaryMessageSource;
        log.info("Set PrimaryMessageSource: {}", primaryMessageSource);
    }

    /**
     * 设置备数据源
     */
    public void setFallbackMessageSource(@NonNull MessageSource fallbackMessageSource) {
        this.fallbackMessageSource = fallbackMessageSource;
        log.info("set FallbackMessageSource: {}", fallbackMessageSource);
    }
}
