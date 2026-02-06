package com.github.stazxr.zblog.bas.i18n.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.NonNull;

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
public class CompositeMessageSource implements MessageSource {
    private static final Logger log = LoggerFactory.getLogger(CompositeMessageSource.class);

    /**
     * 主数据源（通常是 DB）
     */
    private MessageSource primaryMessageSource;

    /**
     * 备数据源（通常是 ResourceBundleMessageSource）
     */
    private MessageSource fallbackMessageSource;

    public CompositeMessageSource(MessageSource primaryMessageSource, MessageSource fallbackMessageSource) {
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
    public String getMessage(@NonNull String code, Object[] args, String defaultMessage, @NonNull Locale locale) {
        try {
            // 先查主数据源
            return primaryMessageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException ignored) {
        } catch (Exception e) {
            log.error("[i18n] 主消息源异常: code={}, locale={}", code, locale, e);
        }

        try {
            // 再查 fallback
            return fallbackMessageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException ignored) {
        } catch (Exception e) {
            log.error("[i18n] 备消息源异常: code={}, locale={}", code, locale, e);
        }

        // 兜底
        return defaultMessage != null ? defaultMessage : code;
    }

    /**
     * 获取国际化消息（不带默认值版本）
     *
     * @param code           消息编码
     * @param args           占位符参数
     * @param locale         区域
     * @return 国际化消息文本
     * @throws NoSuchMessageException 消息未配置
     */
    @NonNull
    @Override
    public String getMessage(@NonNull String code, Object[] args, @NonNull Locale locale) throws NoSuchMessageException {
        try {
            // 先查主数据源
            return primaryMessageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException ignored) {
        } catch (Exception e) {
            log.error("[i18n] 主消息源异常: code={}, locale={}", code, locale, e);
        }

        try {
            // 再查 fallback
            return fallbackMessageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            // 没有 key 抛出异常
            throw e;
        } catch (Exception e) {
            log.error("[i18n] 备消息源异常: code={}, locale={}", code, locale, e);
        }

        // 异常返回 code
        return code;
    }

    /**
     * 通过 MessageSourceResolvable 获取国际化消息
     *
     * @throws org.springframework.context.NoSuchMessageException 如果找不到消息
     */
    @NonNull
    @Override
    public String getMessage(@NonNull MessageSourceResolvable resolvable, @NonNull Locale locale) {
        try {
            return primaryMessageSource.getMessage(resolvable, locale);
        } catch (NoSuchMessageException e) {
            return fallbackMessageSource.getMessage(resolvable, locale);
        } catch (Exception e) {
            log.error("[i18n] 获取主消息源失败: resolvable={}, locale={}, error={}", resolvable, locale, e.getMessage(), e);
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
