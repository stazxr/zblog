package com.github.stazxr.zblog.encryption;
/**
 * 加解密上下文管理类
 *
 * @author SunTao
 * @since 2024-07-28
 */
public class EncryptorContext {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    /**
     * 获取当前线程的上下文信息
     *
     * @return 当前线程的上下文信息，如果没有设置则返回 null
     */
    public static String get() {
        return CONTEXT.get();
    }

    /**
     * 设置当前线程的上下文信息
     *
     * @param context 要设置的上下文信息
     */
    public static void set(String context) {
        CONTEXT.set(context);
    }

    /**
     * 移除当前线程的上下文信息
     */
    public static void remove() {
        CONTEXT.remove();
    }
}
