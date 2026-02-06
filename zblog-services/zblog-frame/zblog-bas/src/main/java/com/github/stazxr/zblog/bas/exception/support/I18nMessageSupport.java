package com.github.stazxr.zblog.bas.exception.support;

import com.github.stazxr.zblog.bas.i18n.I18nUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 基于 I18nUtils 的 MessageSupport SPI 默认实现
 *
 * <p>作为系统默认的国际化消息解析器，优先级设置为 100。</p>
 *
 * <p>该类通过 SPI 加载，不依赖 Spring Bean。</p>
 *
 * @author SunTao
 * @since 2026-01-25
 */
public class I18nMessageSupport implements MessageSupport {
    /**
     * 通过 I18nUtils 获取国际化消息内容
     *
     * @param i18nKey 国际化 key
     * @param args    消息参数
     * @return 解析后的消息文本
     */
    @Override
    public String getMessage(@NonNull String i18nKey, @Nullable Object... args) {
        try {
            String message = I18nUtils.getMessage(i18nKey, args);
            return message != null ? message : i18nKey;
        } catch (Exception ignored) {
            return i18nKey;
        }
    }

    /**
     * SPI 优先级
     *
     * @return 优先级，默认 100
     */
    @Override
    public int order() {
        return 100;
    }
}
