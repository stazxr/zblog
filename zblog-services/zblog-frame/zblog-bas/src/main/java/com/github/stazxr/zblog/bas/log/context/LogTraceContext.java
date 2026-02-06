package com.github.stazxr.zblog.bas.log.context;

/**
 * 日志链路上下文, 保存 TraceId 和请求开始时间.
 *
 * @author SunTao
 * @since 2024-05-19
 */
public class LogTraceContext {
    private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    public static void setTraceId(String id) {
        TRACE_ID.set(id);
    }

    public static String getTraceId() {
        return TRACE_ID.get();
    }

    public static void setStartTime(long time) {
        START_TIME.set(time);
    }

    public static long getStartTime() {
        Long t = START_TIME.get();
        return t == null ? 0 : t;
    }

    public static void clear() {
        TRACE_ID.remove();
        START_TIME.remove();
    }
}
