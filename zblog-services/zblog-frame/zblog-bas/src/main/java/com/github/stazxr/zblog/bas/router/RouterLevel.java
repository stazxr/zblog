package com.github.stazxr.zblog.bas.router;

/**
 * 路由访问级别
 *
 * @author SunTao
 * @since 2024-11-24
 * @version 5.0
 */
public abstract class RouterLevel {
    /**
     * 直接访问
     */
    public static final int OPEN = 1;

    /**
     * 登录访问
     */
    public static final int PUBLIC = 1 << 1;

    /**
     * 授权访问
     */
    public static final int PERM = 1 << 2;
}

