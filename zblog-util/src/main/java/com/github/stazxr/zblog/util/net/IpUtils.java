package com.github.stazxr.zblog.util.net;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.stazxr.zblog.util.Constants;
import com.github.stazxr.zblog.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

/**
 * IP工具类
 *
 * @author SunTao
 * @since 2022-05-31
 */
@Slf4j
public class IpUtils {
    private static final String UNKNOWN = "unknown";

    private static final Ip2regionSearcher IP_SEARCHER = SpringContextHolder.getBean(Ip2regionSearcher.class);

    private static final UserAgentAnalyzer USER_AGENT_ANALYZER = UserAgentAnalyzer.newBuilder()
            .hideMatcherLoadStats().withCache(10000).withField(UserAgent.AGENT_NAME_VERSION).build();

    /**
     * 根据请求获取IP地址
     *
     * @param request request
     * @return ip
     */
    public static String getIp(HttpServletRequest request) {
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

        if (Constants.LOCAL_HOST_V4.equals(ip) || Constants.LOCAL_HOST_V6.equals(ip)) {
            try {
                // 获取本机真正的ip地址
                ip = LocalHostUtils.getFirstLocalIp();
            } catch (UnknownHostException e) {
                log.error("获取本机真正的ip地址发生异常", e);
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        final int ipSingleLength = 15;
        final char ipSplitSymbol = ',';
        if (ip != null && ip.length() > ipSingleLength) {
            if (ip.indexOf(ipSplitSymbol) > -1) {
                ip = ip.substring(0, ip.indexOf(ipSplitSymbol));
            }
        }

        return ip;
    }

    /**
     * 根据ip获取详细地址(http)
     *
     * @param ip IP 地址
     * @return 详细地址
     */
    public static String getHttpCityInfo(String ip) {
        String api = String.format(Constants.Url.IP_URL, ip);
        JSONObject object = JSONUtil.parseObj(HttpUtil.get(api));
        return object.get("addr", String.class);
    }

    /**
     * 根据ip获取详细地址(本地)
     *
     * @param ip IP 地址
     * @return 详细地址
     */
    public static String getLocalCityInfo(String ip) {
        if (IP_SEARCHER == null) {
            throw new IllegalStateException("bean Ip2regionSearcher not exist");
        }
        IpInfo ipInfo = IP_SEARCHER.memorySearch(ip);
        if (ipInfo != null) {
            return ipInfo.getAddress();
        }
        return "";
    }

    /**
     * 获取浏览器信息
     *
     * @param request 请求信息
     * @return 浏览器信息
     */
    public static String getBrowser(HttpServletRequest request) {
        UserAgent.ImmutableUserAgent userAgent = USER_AGENT_ANALYZER.parse(request.getHeader("User-Agent"));
        return userAgent.get(UserAgent.AGENT_NAME_VERSION).getValue();
    }
}
