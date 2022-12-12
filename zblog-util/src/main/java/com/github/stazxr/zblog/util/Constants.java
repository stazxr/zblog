package com.github.stazxr.zblog.util;

/**
 * 常用工具常量
 *
 * @author SunTao
 * @since 2022-06-07
 */
public class Constants {
    /**
     * 本机IP，IPV4
     */
    public static final String LOCAL_HOST_V4 = "127.0.0.1";

    /**
     * 本机IP，IPV6
     */
    public static final String LOCAL_HOST_V6 = "0:0:0:0:0:0:0:1";

    /**
     * URL和参数的分隔符
     */
    public static final String URL_SPLIT_LABEL = "?";

    /**
     * 常用链接
     */
    public static class Url {
        /**
         * IP 归属地查询
         */
        public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";

        /**
         * QQ 跳转链接
         */
        public static final String QQ_URL = "http://wpa.qq.com/msgrd?v=3&uin=%s&site=qq&menu=yes";
    }
}
