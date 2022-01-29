package com.github.stazxr.zblog.util.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求相关工具类
 *
 * @author SunTao
 * @since 2022-01-28
 */
public class RequestUtils {
    private RequestUtils() {
    }

    private static final String UNKNOWN = "unknown";

    /**
     * 根据请求获取用户登录IP
     *
     * @param request request
     * @return ip
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        final int ipSingleLength = 15;
        final char ipSplitSymbol = ',';
        if (ip != null && ip.length() > ipSingleLength) {
            if (ip.indexOf(ipSplitSymbol) > 0) {
                ip = ip.substring(0, ip.indexOf(ipSplitSymbol));
            }
        }
        return ip;
    }

    /**
     * 判断一个请求是否是Ajax请求
     *
     * @param request 请求信息
     * @return boolean
     */
    public static boolean isAjax(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.contains("application/json")) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest")) {
            return true;
        }

        String uri = request.getRequestURI();
        if (uri.toLowerCase().contains("json") || uri.toLowerCase().contains("xml")) {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        if (null != ajax) {
            return ajax.toLowerCase().contains("json") || ajax.toLowerCase().contains("xml");
        }

        return false;
    }
}
