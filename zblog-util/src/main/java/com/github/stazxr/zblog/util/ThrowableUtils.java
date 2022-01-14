package com.github.stazxr.zblog.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具 2019-01-06
 * @author Zheng Jie
 */
public class ThrowableUtils {
    /**
     * 获取堆栈信息
     *
     * @param throwable 异常信息
     * @return String
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
