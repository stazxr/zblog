package com.github.stazxr.zblog.util.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 请求相关工具类
 *
 * @author SunTao
 * @since 2022-01-28
 */
@Slf4j
public class RequestUtils {
    private static final String APPLICATION_JSON = "application/json";

    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    private static final String PATTERN_JSON = "json";

    private static final String PATTERN_XML = "xml";

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
        if (accept != null && accept.contains(APPLICATION_JSON)) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.contains(XML_HTTP_REQUEST)) {
            return true;
        }

        String uri = request.getRequestURI();
        if (uri.toLowerCase().contains(PATTERN_JSON) || uri.toLowerCase().contains(PATTERN_XML)) {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        if (null != ajax) {
            return ajax.toLowerCase().contains(PATTERN_JSON) || ajax.toLowerCase().contains(PATTERN_XML);
        }

        return false;
    }

    /**
     * 获取 request body
     *
     * @param request req
     * @return body
     */
    public static String obtainBody(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = request.getReader()) {
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (IOException e) {
            log.error("requestBody read error: [{}]", e.getMessage());
            throw new IllegalStateException("obtainBody failed");
        }
    }
}
