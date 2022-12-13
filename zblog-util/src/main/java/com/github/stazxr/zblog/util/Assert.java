package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.util.exception.AssertionViolatedException;

/**
 * 验证工具类
 *
 * @author SunTao
 * @since 2021-11-30
 */
public class Assert {
    /**
     * Assert that an object is not null
     *
     * @param object the object to check
     * @throws AssertionViolatedException if the object is null
     */
    public static void notNull(Object object) {
        notNull(object, "参数不能为空");
    }

    /**
     * Assert that an object is not null
     *
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws AssertionViolatedException if the object is null
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new AssertionViolatedException(message);
        }
    }

    /**
     * Assert that an object is not null
     *
     * @param object the object to check
     * @param callBack callback function
     */
    public static void notNull(Object object, CallBack callBack) {
        if (object == null) {
            callBack.execute();
        }
    }

    /**
     * Assert that a string is not
     *
     * @param str the string to check
     * @param message the exception message to use if the assertion fails
     * @throws AssertionViolatedException if the string is blank
     */
    public static void notBlank(String str, String message) {
        if (str == null || "".equals(str.trim())) {
            throw new AssertionViolatedException(message);
        }
    }

    /**
     * Assert flag is true
     *
     * @param flag flag
     * @param message the exception message to use if the flag is true
     * @throws AssertionViolatedException if the flag is true
     */
    public static void isTrue(boolean flag, String message) {
        if (flag) {
            throw new AssertionViolatedException(message);
        }
    }

    /**
     * Assert flag is not true
     *
     * @param flag flag
     * @param message the exception message to use if the flag is not true
     * @throws AssertionViolatedException if the flag is not true
     */
    public static void nonTrue(boolean flag, String message) {
        if (!flag) {
            throw new AssertionViolatedException(message);
        }
    }

    /**
     * Assert flag is true
     *
     * @param flag flag
     * @param callBack callback function
     */
    public static void isTrue(boolean flag, CallBack callBack) {
        if (flag) {
            callBack.execute();
        }
    }

    /**
     * Assert flag is true
     *
     * @param flag flag
     * @param trueBack true callback function
     * @param falseBack false callback function
     */
    public static void isTrue(boolean flag, CallBack trueBack, CallBack falseBack) {
        if (flag) {
            trueBack.execute();
        } else {
            falseBack.execute();
        }
    }

    public interface CallBack {
        /**
         * 回调方法
         */
        void execute();
    }
}
