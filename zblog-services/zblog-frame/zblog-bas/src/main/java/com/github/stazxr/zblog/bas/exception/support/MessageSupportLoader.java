package com.github.stazxr.zblog.bas.exception.support;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Comparator;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * MessageSupport SPI 加载器
 *
 * <p>负责通过 {@link ServiceLoader} 加载所有
 * MessageSupport 实现，并选择优先级最高的一个。</p>
 *
 * @author SunTao
 * @since 2026-01-25
 */
public final class MessageSupportLoader {
    /**
     * 最终生效的 MessageSupport 实例
     */
    private static final MessageSupport INSTANCE = load();

    private MessageSupportLoader() {
        // utility class
    }

    /**
     * 加载并选择优先级最高的 MessageSupport 实现
     */
    private static MessageSupport load() {
        ServiceLoader<MessageSupport> loader = ServiceLoader.load(MessageSupport.class);
        return StreamSupport.stream(loader.spliterator(), false)
                .max(Comparator.comparingInt(MessageSupport::order))
                .orElseThrow(() -> new IllegalStateException("No MessageSupport SPI found"));
    }

    /**
     * 对外统一消息获取入口
     *
     * @param i18nKey 国际化 key
     * @return 国际化消息
     */
    public static String getMessage(@NonNull String i18nKey) {
        return getMessage(i18nKey, new Object[0]);
    }

    /**
     * 对外统一消息获取入口
     *
     * @param i18nKey 国际化 key
     * @param args    参数
     * @return 国际化消息
     */
    public static String getMessage(@NonNull String i18nKey, @Nullable Object... args) {
        return INSTANCE.getMessage(i18nKey, args);
    }
}
