package com.github.stazxr.zblog.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 *
 * @author SunTao
 * @since 2024-07-25
 */
public class ThrowableUtils {
    /**
     * 获取异常的堆栈信息的字符串表示。
     *
     * @param throwable 异常对象
     * @return 堆栈信息的字符串表示
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            return throwable == null ? "" : throwable.toString();
        }
    }
}
