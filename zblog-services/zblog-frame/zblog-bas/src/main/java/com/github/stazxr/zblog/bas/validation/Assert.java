package com.github.stazxr.zblog.bas.validation;

import com.github.stazxr.zblog.bas.exception.ExpMessageCode;

import java.util.Objects;

/**
 * 参数校验工具类
 *
 * @author SunTao
 * @since 2025-08-16
 */
public class Assert {
    public static void isEquals(Object a, Object b, String message) {
        if (!Objects.equals(a, b)) {
            throw new AssertException(message);
        }
    }

    public static void isEquals(Object a, Object b, ExpMessageCode expMessageCode) {
        if (!Objects.equals(a, b)) {
            throw new AssertException(expMessageCode);
        }
    }

    public static void isNoEquals(Object a, Object b, String message) {
        if (Objects.equals(a, b)) {
            throw new AssertException(message);
        }
    }

    public static void isNoEquals(Object a, Object b, ExpMessageCode expMessageCode) {
        if (Objects.equals(a, b)) {
            throw new AssertException(expMessageCode);
        }
    }

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

    public static void isBlank(String str, String message) {
        if (!isBlankInternal(str)) {
            throw new AssertException(message);
        }
    }

    public static void isBlank(String str, ExpMessageCode expMessageCode) {
        if (!isBlankInternal(str)) {
            throw new AssertException(expMessageCode);
        }
    }

    public static void notBlank(String str) {
        notBlank(str, ExpMessageCode.PARAM_BLANK);
    }

    public static void notBlank(String str, String message) {
        if (isBlankInternal(str)) {
            throw new AssertException(message);
        }
    }

    public static void notBlank(String str, ExpMessageCode expMessageCode) {
        if (isBlankInternal(str)) {
            throw new AssertException(expMessageCode);
        }
    }

    public static void failIfTrue(boolean condition, String message) {
        if (condition) {
            throw new AssertException(message);
        }
    }

    public static void failIfTrue(boolean condition, ExpMessageCode code) {
        if (condition) {
            throw new AssertException(code);
        }
    }

    public static void failIfFalse(boolean condition, String message) {
        if (!condition) {
            throw new AssertException(message);
        }
    }

    public static void failIfFalse(boolean condition, ExpMessageCode code) {
        if (!condition) {
            throw new AssertException(code);
        }
    }

    public static void affectOneRow(int rows, String message) {
        if (rows != 1) {
            throw new AssertException(message);
        }
    }

    public static void affectOneRow(int rows, ExpMessageCode messageCode) {
        if (rows != 1) {
            throw new AssertException(messageCode);
        }
    }

    /**
     * Assert flag is true
     *
     * @param flag flag
     * @param callBack callback function
     */
    public static void doIfTrue(boolean flag, CallBack callBack) {
        if (flag) {
            callBack.call();
        }
    }

    /**
     * Assert flag is true
     *
     * @param flag flag
     * @param trueBack true callback function
     * @param falseBack false callback function
     */
    public static void doIfElse(boolean flag, CallBack trueBack, CallBack falseBack) {
        if (flag) {
            trueBack.call();
        } else {
            falseBack.call();
        }
    }

    /**
     * Assert flag is true
     *
     * @param flag flag
     * @param trueBack true callback function
     * @param message false throw exception
     */
    public static void doIfTrueElseThrow(boolean flag, CallBack trueBack, String message) {
        if (flag) {
            trueBack.call();
        } else {
            throw new AssertException(message);
        }
    }

    /**
     * Assert flag is true
     *
     * @param flag flag
     * @param trueBack true callback function
     * @param expMessageCode false throw exception
     */
    public static void doIfTrueElseThrow(boolean flag, CallBack trueBack, ExpMessageCode expMessageCode) {
        if (flag) {
            trueBack.call();
        } else {
            throw new AssertException(expMessageCode);
        }
    }

    public interface CallBack {
        void call();
    }

    private static boolean isBlankInternal(String str) {
        return str == null || str.trim().isEmpty();
    }
}
