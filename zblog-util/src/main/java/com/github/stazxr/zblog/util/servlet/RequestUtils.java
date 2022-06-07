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
