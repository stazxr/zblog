package com.github.stazxr.zblog.core.util;

import com.github.stazxr.zblog.core.exception.DataValidatedException;
import com.github.stazxr.zblog.util.Assert;

/**
 * 数据校验工具类，基于 Assert 新增业务的校验
 *
 * @author SunTao
 * @since 2022-08-27
 */
public class DataValidated extends Assert {
    /**
     * Assert that an object is not null
     *
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws DataValidatedException if the object is null
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new DataValidatedException(message);
        }
    }

    /**
     * Assert that a string is not
     *
     * @param str the string to check
     * @param message the exception message to use if the assertion fails
     * @throws DataValidatedException if the string is blank
     */
    public static void notBlank(String str, String message) {
        if (str == null || "".equals(str.trim())) {
            throw new DataValidatedException(message);
        }
    }

    /**
     * Assert flag is true
     *
     * @param flag flag
     * @param message the exception message to use if the flag is not true
     * @throws DataValidatedException if the flag is not true
     */
    public static void isTrue(boolean flag, String message) {
        if (flag) {
            throw new DataValidatedException(message);
        }
    }
}
