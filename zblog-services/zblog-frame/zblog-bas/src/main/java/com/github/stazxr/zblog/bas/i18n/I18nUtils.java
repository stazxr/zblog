package com.github.stazxr.zblog.bas.i18n;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;

import java.util.Locale;

/**
 * 国际化工具类，全局静态调用，无需每处都注入 MessageSource
 *
 * @author SunTao
 * @since 2025-08-10
 */
@Slf4j
public class I18nUtils {
    private static MessageSource messageSource;

    /**
     * 在 Spring 容器初始化时注入 MessageSource
     *
     * @param source Spring 提供的 MessageSource 实例
     * @throws IllegalArgumentException 如果 source 为 null
     */
    public static void setMessageSource(MessageSource source) {
        if (I18nUtils.messageSource == null) {
            if (source == null) {
                throw new IllegalArgumentException("MessageSource 不能为 null");
            }
            log.debug("I18nUtils 已注入 MessageSource 实例: {}", source.getClass().getName());
            I18nUtils.messageSource = source;
        }
    }

    private I18nUtils() { }

    /**
     * 根据当前请求的 Locale 获取国际化消息
     *
     * @param code 消息编码
     * @return 国际化消息文本，如果未找到则返回 code
     */
    public static String getMessage(String code) {
        return getMessage(code, resolveLocale());
    }

    /**
     * 根据指定 Locale 获取国际化消息
     *
     * @param code   消息编码
     * @param locale 指定语言环境
     * @return 国际化消息文本，如果未找到则返回 code
     */
    public static String getMessage(String code, Locale locale) {
        return getMessage(code, null, locale);
    }

    /**
     * 根据当前请求的 Locale 获取国际化消息，并进行参数填充
     *
     * @param code 消息编码
     * @param args 消息参数，用于替换消息中的 {0}、{1} 占位符
     * @return 国际化消息文本，如果未找到则返回 code
     */
    public static String getMessage(String code, Object[] args) {
        return getMessage(code, args, resolveLocale());
    }

    /**
     * 根据指定 Locale 获取国际化消息，并进行参数填充
     *
     * @param code   消息编码
     * @param args   消息参数，用于替换消息中的 {0}、{1} 占位符
     * @param locale 指定语言环境
     * @return 国际化消息文本，如果未找到则返回 code
     */
    public static String getMessage(String code, @Nullable Object[] args, Locale locale) {
        try {
            return getMessageSource().getMessage(code, args, locale);
        } catch (Exception e) {
            log.error("获取国际化消息失败: code={}, args={}, locale={}, error={}", code, args, locale, e.getMessage(), e);
            return code;
        }
    }

    /**
     * 根据当前请求的 Locale 获取国际化消息（带默认消息）
     *
     * @param code           消息编码
     * @param defaultMessage 默认消息，如果未找到对应编码则返回此值
     * @return 国际化消息文本
     */
    public static String getMessage(String code, @Nullable String defaultMessage) {
        return getMessage(code, null, defaultMessage);
    }

    /**
     * 根据当前请求的 Locale 获取国际化消息（带参数和默认消息）
     *
     * @param code           消息编码
     * @param args           消息参数，用于替换消息中的 {0}、{1} 占位符
     * @param defaultMessage 默认消息，如果未找到对应编码则返回此值
     * @return 国际化消息文本
     */
    public static String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage) {
        return getMessage(code, args, defaultMessage, resolveLocale());
    }

    /**
     * 根据指定 Locale 获取国际化消息（带参数和默认消息）
     *
     * @param code           消息编码
     * @param args           消息参数，用于替换消息中的 {0}、{1} 占位符
     * @param defaultMessage 默认消息，如果未找到对应编码则返回此值
     * @param locale         指定语言环境
     * @return 国际化消息文本
     */
    public static String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale) {
        try {
            return getMessageSource().getMessage(code, args, defaultMessage, locale);
        } catch (Exception e) {
            log.error("获取国际化消息失败: code={}, args={}, defaultMessage={}, locale={}, error={}", code, args, defaultMessage, locale, e.getMessage(), e);
            return code;
        }
    }

    /**
     * 获取当前请求绑定的 Locale
     *
     * @return 当前语言环境
     */
    protected static Locale resolveLocale() {
        return LocaleContextHolder.getLocale();
    }

    /**
     * 获取已注入的 MessageSource
     *
     * @return MessageSource 实例
     * @throws IllegalStateException 如果未初始化
     */
    private static MessageSource getMessageSource() {
        if (messageSource == null) {
            throw new IllegalStateException("MessageSource 未初始化，请在 Spring 启动时调用 I18nUtils.setMessageSource()");
        }
        return messageSource;
    }
}
