package com.github.stazxr.zblog.bas.exception.support;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 消息解析支持接口（SPI）
 *
 * <p>用于将国际化 key + 参数解析为最终展示消息。
 * 支持通过 Java SPI 机制进行多实现扩展。</p>
 *
 * <p>典型实现：
 * <ul>
 *   <li>基于 ResourceBundle</li>
 *   <li>基于 Spring MessageSource</li>
 *   <li>基于数据库 / 配置中心</li>
 * </ul>
 * </p>
 *
 * @author SunTao
 * @since 2026-01-25
 */
public interface MessageSupport {
    /**
     * 根据国际化 key 获取消息内容
     *
     * @param i18nKey 国际化 key
     * @param args    消息参数
     * @return 解析后的消息文本
     */
    String getMessage(@NonNull String i18nKey, @Nullable Object... args);

    /**
     * SPI 优先级，值越大优先级越高
     *
     * <p>当存在多个 MessageSupport 实现时，
     * 将选择 order 最大的一个作为最终实现。</p>
     *
     * @return 优先级，默认 0
     */
    default int order() {
        return 0;
    }
}
