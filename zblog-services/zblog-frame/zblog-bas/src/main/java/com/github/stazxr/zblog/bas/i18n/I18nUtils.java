package com.github.stazxr.zblog.bas.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;

import java.util.Locale;

/**
 * 国际化工具类，全局静态调用，无需每处都注入 MessageSource
 *
 * @author SunTao
 * @since 2025-08-10
 */
public class I18nUtils {
    private static final Logger log = LoggerFactory.getLogger(I18nUtils.class);

    private static volatile MessageSource messageSource;

    private I18nUtils() {
        // utility class
    }

    /**
     * 在 Spring 容器初始化时注入 MessageSource
     *
     * @param source Spring 提供的 MessageSource 实例
     * @throws IllegalArgumentException 如果 source 为 null
     */
    public static void setMessageSource(MessageSource source) {
        if (messageSource != null) {
            log.warn("I18nUtils 已经注入过 MessageSource，忽略重复注入: {}", source.getClass().getName());
            return;
        }
        if (source == null) {
            throw new IllegalArgumentException("MessageSource 不能为 null");
        }
        messageSource = source;
        log.debug("I18nUtils 已注入 MessageSource: {}", source.getClass().getName());
    }

    /**
     * 获取当前请求的 Locale
     */
    private static Locale resolveLocale() {
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

    /**
     * 根据国际化消息文本
     *
     * @param code           消息编码
     * @param args           消息参数，用于替换消息中的 {0}、{1} 占位符
     * @param defaultMessage 默认消息，如果未找到对应编码则返回此值
     * @param locale         指定语言环境
     * @return 国际化消息文本
     */
    public static String getMessage(@NonNull String code, Object[] args, String defaultMessage, Locale locale) {
        Locale targetLocale = locale != null ? locale : resolveLocale();
        try {
            return getMessageSource().getMessage(code, args, defaultMessage != null ? defaultMessage : code, targetLocale);
        } catch (Exception e) {
            log.error("[i18n] 获取消息失败: code={}, locale={}, error={}", code, targetLocale, e.getMessage(), e);
            return defaultMessage != null ? defaultMessage : code;
        }
    }

    /**
     * 根据国际化消息文本
     *
     * @param code 消息编码
     * @return 国际化消息文本，如果未找到则返回 code
     */
    public static String getMessage(@NonNull String code) {
        return getMessage(code, null, null, null);
    }

    /**
     * 根据国际化消息文本
     *
     * @param code 消息编码
     * @param args 消息参数，用于替换消息中的 {0}、{1} 占位符
     * @return 国际化消息文本，如果未找到则返回 code
     */
    public static String getMessage(@NonNull String code, Object[] args) {
        return getMessage(code, args, null, null);
    }

    /**
     * 根据国际化消息文本
     *
     * @param code   消息编码
     * @param locale 指定语言环境
     * @return 国际化消息文本，如果未找到则返回 code
     */
    public static String getMessage(@NonNull String code, Locale locale) {
        return getMessage(code, null, null, locale);
    }

    /**
     * 根据国际化消息文本
     *
     * @param code   消息编码
     * @param args   消息参数，用于替换消息中的 {0}、{1} 占位符
     * @param locale 指定语言环境
     * @return 国际化消息文本，如果未找到则返回 code
     */
    public static String getMessage(@NonNull String code, Object[] args, Locale locale) {
        return getMessage(code, args, null, locale);
    }

    /**
     * 根据国际化消息文本
     *
     * @param code           消息编码
     * @param args           消息参数，用于替换消息中的 {0}、{1} 占位符
     * @param defaultMessage 默认消息，如果未找到对应编码则返回此值
     * @return 国际化消息文本
     */
    public static String getMessage(@NonNull String code, Object[] args, String defaultMessage) {
        return getMessage(code, args, defaultMessage, null);
    }

    /**
     * 根据国际化消息文本
     *
     * @param code           消息编码
     * @param defaultMessage 默认消息，如果未找到对应编码则返回此值
     * @return 国际化消息文本
     */
    public static String getMessage(@NonNull String code, String defaultMessage) {
        return getMessage(code, null, defaultMessage, null);
    }

    /**
     * 根据国际化消息文本
     *
     * @param code           消息编码
     * @param defaultMessage 默认消息，如果未找到对应编码则返回此值
     * @param locale         指定语言环境
     * @return 国际化消息文本
     */
    public static String getMessage(@NonNull String code, String defaultMessage, Locale locale) {
        return getMessage(code, null, defaultMessage, locale);
    }
}
