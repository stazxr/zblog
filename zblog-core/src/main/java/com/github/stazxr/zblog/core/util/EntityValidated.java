package com.github.stazxr.zblog.core.util;

import com.github.stazxr.zblog.core.exception.EntityValidatedException;
import com.github.stazxr.zblog.util.Assert;

/**
 * 实体校验工具类
 *
 * @author SunTao
 * @since 2022-08-27
 */
public class EntityValidated extends Assert {
    /**
     * Assert that an object is not null
     *
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws EntityValidatedException if the object is null
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new EntityValidatedException(message);
        }
    }

    /**
     * Assert that a string is not
     *
     * @param str the string to check
     * @param message the exception message to use if the assertion fails
     * @throws EntityValidatedException if the string is blank
     */
    public static void notBlank(String str, String message) {
        if (str == null || "".equals(str.trim())) {
            throw new EntityValidatedException(message);
        }
    }

    /**
     * Assert flag is true
     *
     * @param flag flag
     * @param message the exception message to use if the flag is not true
     * @throws EntityValidatedException if the flag is not true
     */
    public static void isTrue(boolean flag, String message) {
        if (flag) {
            throw new EntityValidatedException(message);
        }
    }
}
