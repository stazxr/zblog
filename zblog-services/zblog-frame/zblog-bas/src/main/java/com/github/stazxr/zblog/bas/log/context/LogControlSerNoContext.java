package com.github.stazxr.zblog.bas.log.context;

import com.github.stazxr.zblog.util.StringUtils;

/**
 * 日志流水号配置
 *
 * @author SunTao
 * @since 2024-05-19
 */
public class LogControlSerNoContext {
    private static final ThreadLocal<String> LOG_CONTROL_SER_NO = new ThreadLocal<>();

    /**
     * 设置日志流水号
     *
     * @param logControlSerNo 日志流水号
     */
    public static void set(String logControlSerNo) {
        LOG_CONTROL_SER_NO.set(logControlSerNo);
    }

    /**
     * 获取日志流水号
     *
     * @return 日志流水号
     */
    public static String get() {
        return LOG_CONTROL_SER_NO.get();
    }

    /**
     * 日志流水号是否设置
     *
     * @return boolean
     */
    public static boolean exist() {
        return StringUtils.isNotBlank(get());
    }

    /**
     * 清除日志流水号
     */
    public static void clear() {
        LOG_CONTROL_SER_NO.remove();
    }
}
