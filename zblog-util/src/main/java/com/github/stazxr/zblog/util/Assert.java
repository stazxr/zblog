package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.util.exception.ValidParamException;

/**
 * 验证工具类
 *
 * @author SunTao
 * @since 2021-11-30
 */
public final class Assert {
    /**
     * Assert that an object is not null
     *
     * @param object the object to check
     * @throws IllegalArgumentException if the object is null
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - param is null");
    }

    /**
     * Assert that an object is not null
     *
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is null
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new ValidParamException(message);
        }
    }

    /**
     * Assert that a string is not
     *
     * @param str the string to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the string is blank
     */
    public static void notBlank(String str, String message) {
        if (str == null || "".equals(str.trim())) {
            throw new ValidParamException(message);
        }
    }

    /**
     * Assert flag is true
     *
     * @param flag flag
     * @param message the exception message to use if the flag is not true
     * @throws IllegalArgumentException if the flag is not true
     */
    public static void isTrue(boolean flag, String message) {
        if (flag) {
            throw new ValidParamException(message);
        }
    }
}
