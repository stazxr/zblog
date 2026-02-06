package com.github.stazxr.zblog.bas.router;

/**
 * 扩展路由访问级别
 *
 * @author SunTao
 * @since 2024-11-24
 * @version 5.0
 */
public class RouterExtLevel extends RouterLevel {
    /**
     * 禁止访问
     */
    public static final int FORBIDDEN = 8;

    /**
     * 404
     */
    public static final int NULL = 16;
}
