package com.github.stazxr.zblog.bas.notify.mail;

import java.util.HashMap;
import java.util.Map;

/**
 * 状态码
 *
 * @author SunTao
 * @since 2024-08-15
 */
public enum MailExceptionCode {
    /**
     * 发送普通文本邮件失败
     */
    ZNTFM01("send simple email failed"),

    /**
     * 发送普通HTML邮件失败
     */
    ZNTFM02("send html email failed"),

    /**
     * 发送带图片的Html邮件失败
     */
    ZNTFM03("send image email failed"),

    /**
     * 发送带附件的邮件失败
     */
    ZNTFM04("send attachment email failed");

    private final String message;

    /**
     * 用于缓存状态码和错误信息的映射
     */
    private static final Map<String, String> CACHE_MAP = new HashMap<>();

    /**
     * 构造函数，初始化状态码及其对应的错误信息
     *
     * @param message 错误信息
     */
    MailExceptionCode(String message) {
        this.message = message;
    }

    /**
     * 根据状态码获取对应的错误信息
     *
     * @param code 状态码
     * @return 状态码对应的错误信息，如果状态码不存在则返回null
     */
    public static String of(String code) {
        // 如果缓存中存在该状态码，则直接返回缓存中的错误信息
        if (CACHE_MAP.containsKey(code)) {
            return CACHE_MAP.get(code);
        } else {
            // 遍历所有状态码，查找匹配的状态码并缓存其错误信息
            for (MailExceptionCode value : MailExceptionCode.values()) {
                if (value.name().equals(code)) {
                    CACHE_MAP.put(value.name(), value.message);
                    return value.message;
                }
            }
        }

        // 如果状态码不存在，则返回 null
        return null;
    }
}

