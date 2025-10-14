package com.github.stazxr.zblog.bas.validation;

import com.github.stazxr.zblog.bas.exception.ExpMessageCode;

/**
 * 参数校验工具类
 *
 * @author SunTao
 * @since 2025-08-16
 */
public class Assert {
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new AssertException(message);
        }
    }

    public static void isNull(Object object, ExpMessageCode expMessageCode) {
        if (object != null) {
            throw new AssertException(expMessageCode);
        }
    }

    public static void notNull(Object object) {
        notNull(object, ExpMessageCode.PARAM_NULL);
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new AssertException(message);
        }
    }

    public static void notNull(Object object, ExpMessageCode expMessageCode) {
        if (object == null) {
            throw new AssertException(expMessageCode);
        }
    }

    public static void notBlank(String str, String message) {
        if (str == null || "".equals(str.trim())) {
            throw new AssertException(message);
        }
    }

    public static void notBlank(String str, ExpMessageCode expMessageCode) {
        if (str == null || "".equals(str.trim())) {
            throw new AssertException(expMessageCode);
        }
    }

    public static void isTrue(boolean flag, String message) {
        if (flag) {
            throw new AssertException(message);
        }
    }

    public static void isTrue(boolean flag, ExpMessageCode expMessageCode) {
        if (flag) {
            throw new AssertException(expMessageCode);
        }
    }
}
