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
     * 构建包含根异常信息的完整异常消息。
     *
     * @param message 基础消息
     * @param cause 根异常
     * @return 完整的异常消息
     */
    public static String buildMessage(String message, Throwable cause) {
        if (cause == null) {
            return message;
        }
        StringBuilder sb = new StringBuilder();
        if (message != null) {
            sb.append(message).append("; ");
        }
        sb.append("nested exception is ").append(cause);
        return sb.toString();
    }

    /**
     * 获取异常的堆栈信息字符串。
     *
     * @param throwable 异常对象
     * @return 包含堆栈信息的字符串
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    /**
     * 将异常对象转换为包含异常名、消息和堆栈跟踪信息的字符串。
     *
     * @param throwable 异常对象
     * @return 包含异常名、消息和堆栈跟踪信息的字符串
     */
    public static String stackTraceToString(Throwable throwable) {
        String expName = throwable.getClass().getName();
        String message = throwable.getMessage();
        StackTraceElement[] elements = throwable.getStackTrace();
        StringBuilder buffer = new StringBuilder();
        for (StackTraceElement stet : elements) {
            buffer.append(stet).append("\n");
        }
        return expName + ":" + message + "\n\t" + buffer;
    }
}
