package com.github.stazxr.zblog.util.net;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.stazxr.zblog.util.Constants;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.*;

/**
 * IP工具类
 *
 * @author SunTao
 * @since 2022-05-31
 */
@Slf4j
public class IpUtils {
    /**
     * 省
     */
    public static final int IP_LOCATION_PRO = 1;

    /**
     * 省市
     */
    public static final int IP_LOCATION_PRO_CITY = 2;

    /**
     * 省市区（县）
     */
    public static final int IP_LOCATION_PRO_CITY_REGION = 3;

    private static final String LOCAL_IP_LOCATION = "局域网";
    private static final List<String> LOCAL_LIST = Arrays.asList("局域网", "本地局域网", "内网IP");
    private static final String IP_SEARCH_URL = "https://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";

    /**
     * IP地理位置缓存，保证性能，不需要关注并发
     */
    private static final Map<Integer, Map<String, String>> CACHE = new HashMap<>();
    private static final Map<String, String> CACHE1 = new HashMap<>();
    private static final Map<String, String> CACHE2 = new HashMap<>();
    private static final Map<String, String> CACHE3 = new HashMap<>();
    private static final int MAX_CACHE_SIZE = 4096;
    private static final int RESIZE_CACHE_SIZE = MAX_CACHE_SIZE / 4;
    private static final Object RESIZE_LOCK = new Object();

    static {
        CACHE.put(IP_LOCATION_PRO, CACHE1);
        CACHE.put(IP_LOCATION_PRO_CITY, CACHE2);
        CACHE.put(IP_LOCATION_PRO_CITY_REGION, CACHE3);
    }

    /**
     * 获取浏览器信息
     *
     * @param request 请求信息
     * @return 浏览器信息
     */
    public static String getBrowser(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * 根据请求获取IP地址
     *
     * @param request request
     * @return ip
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ipIsEmpty(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ipIsEmpty(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ipIsEmpty(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipIsEmpty(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipIsEmpty(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipIsEmpty(ip)) {
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
     * 根据IP地址获取地理位置详细信息
     *
     * @param ip IP地址
     * @return 地理位置详细信息 省市县
     */
    public static String getIpLocation(String ip) {
        return getIpLocation(ip, IP_LOCATION_PRO_CITY_REGION);
    }

    /**
     * 根据IP地址获取地理位置信息
     *
     * @param ip IP地址
     * @param ipLocationType 地理位置类型
     * @return 地理位置信息
     */
    public static String getIpLocation(String ip, int ipLocationType) {
        try {
            if (ipLocationType < IP_LOCATION_PRO || ipLocationType > IP_LOCATION_PRO_CITY_REGION) {
                return "";
            }

            // read from cache
            if (CACHE.get(ipLocationType).containsKey(ip)) {
                return CACHE.get(ipLocationType).get(ip);
            }

            final String noProvinceCode = "999999";
            String url = String.format(IP_SEARCH_URL, ip);
            JSONObject resultJson = JSONUtil.parseObj(HttpUtil.get(url));

            String proCode = resultJson.getStr("proCode");
            if (proCode == null || noProvinceCode.equals(proCode.trim())) {
                String addr = resultJson.getStr("addr");
                String address = (addr != null && LOCAL_LIST.contains(addr.trim())) ? LOCAL_IP_LOCATION : "";
                putCache(ip, ipLocationType, address);
                return address;
            }

            String address = resultJson.getStr("pro").trim();
            if (ipLocationType >= IP_LOCATION_PRO_CITY) {
                address = address + resultJson.getStr("city").trim();
                if (ipLocationType == IP_LOCATION_PRO_CITY_REGION) {
                    address = address + resultJson.getStr("region").trim();
                }
            }
            return address;
        } catch (Exception e) {
            log.error("根据IP获取地理位置信息发生异常: {}", IP_SEARCH_URL, e);
            return "";
        }
    }

    private static void putCache(String ip, int ipLocationType, String address) {
        Map<String, String> cacheMap = CACHE.get(ipLocationType);
        if (cacheMap.size() > MAX_CACHE_SIZE) {
            synchronized (RESIZE_LOCK) {
                if (cacheMap.size() > MAX_CACHE_SIZE) {
                    Iterator<String> iterator = cacheMap.keySet().iterator();
                    while (iterator.hasNext() && cacheMap.size() > RESIZE_CACHE_SIZE) {
                        iterator.next();
                        iterator.remove();
                    }
                }
            }
        }

        CACHE.get(ipLocationType).put(ip, address.trim());
    }

    private static boolean ipIsEmpty(String ip) {
        final String unknown = "unknown";
        return ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip);
    }
}
