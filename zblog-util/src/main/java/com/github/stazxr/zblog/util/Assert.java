package com.github.stazxr.zblog.util;

/**
 * 验证工具类
 *
 * @author SunTao
 * @since 2021-11-30
 */
public abstract class Assert {
    /**
     * Assert that an object is not
     *
     * @param object the object to check
     * @throws IllegalArgumentException if the object is null
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - the object argument is null");
    }

    /**
     * Assert that an object is not
     *
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is null
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
