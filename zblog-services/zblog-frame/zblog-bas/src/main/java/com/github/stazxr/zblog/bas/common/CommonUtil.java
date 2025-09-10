package com.github.stazxr.zblog.bas.common;

/**
 *
 *
 * @author SunTao
 * @since 2025-08-13
 */
public class CommonUtil {
    public static final String SS = "{";
    public static final String AA = "}";

    public static boolean isValidatorTemplateMessage(String message) {
        return message != null && message.startsWith("{") && message.endsWith("}");
    }

    public static boolean isResultTemplateMessage(String message) {
        return message != null && message.startsWith("{") && message.endsWith("}");
    }
}
