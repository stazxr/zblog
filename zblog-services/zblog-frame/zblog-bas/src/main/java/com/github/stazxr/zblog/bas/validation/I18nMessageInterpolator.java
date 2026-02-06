package com.github.stazxr.zblog.bas.validation;

import com.github.stazxr.zblog.bas.i18n.I18nUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.MessageInterpolator;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 国际化消息插值器
 *
 * <p>该类用于在 Bean Validation 校验失败时，将消息模板转换为国际化文本。
 * 消息模板通常是 "{code}" 格式的占位符，表示消息编码。
 * 如果是占位符，则根据指定的 Locale 从国际化工具类获取对应的文本；
 * 否则直接返回原始消息。
 *
 * <p>示例：
 * <pre>
 * {@code
 * @NotBlank(message = "{user.name.notBlank}")
 * private String name;
 * }
 * </pre>
 * 当校验失败时，会调用本类根据当前语言环境返回对应的错误信息。
 *
 * @author SunTao
 * @since 2026-01-25
 */
public class I18nMessageInterpolator implements MessageInterpolator {
    private static final Logger log = LoggerFactory.getLogger(I18nMessageInterpolator.class);

    /** 默认系统 Locale */
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    // 缓存国际化消息，Key 格式：code_locale
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    /**
     * 使用默认系统 Locale 进行消息插值
     *
     * @param messageTemplate 校验消息模板
     * @param context 上下文信息（ConstraintViolationContext）
     * @return 本地化后的消息
     */
    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolate(messageTemplate, context, DEFAULT_LOCALE);
    }

    /**
     * 使用指定 Locale 进行消息插值
     *
     * @param messageTemplate 校验消息模板
     * @param context 上下文信息（ConstraintViolationContext）
     * @param locale 指定的语言环境
     * @return 本地化后的消息
     */
    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        if (messageTemplate == null || messageTemplate.isEmpty()) {
            return messageTemplate;
        }

        if (isValidatorTemplateMessage(messageTemplate)) {
            String code = messageTemplate.substring(1, messageTemplate.length() - 1);
            String cacheKey = code + "_" + locale.toLanguageTag();
            // 从缓存中获取，如果不存在则调用 I18nUtils 获取
            String message = cache.computeIfAbsent(cacheKey, key -> I18nUtils.getMessage(code, locale));
            if (message == null) {
                log.warn("Cannot find i18n message for code: {}", code);
                return code;
            }
            Object[] args = extractArgs(context);
            if (args != null && args.length > 0) {
                return MessageFormat.format(message, args);
            }
            return message;
        }
        // 非占位符消息，直接返回原始文本
        return messageTemplate;
    }

    /**
     * 判断是否是 {code} 占位符
     */
    private static boolean isValidatorTemplateMessage(String message) {
        return message != null && message.startsWith("{") && message.endsWith("}");
    }

    /**
     * 从校验上下文中提取参数，用于 MessageFormat
     */
    private Object[] extractArgs(Context context) {
        if (context == null || context.getConstraintDescriptor() == null) {
            return null;
        }
        Object validatedValue = context.getValidatedValue();
        if (validatedValue != null) {
            return new Object[]{ validatedValue };
        }
        return null;
    }
}
