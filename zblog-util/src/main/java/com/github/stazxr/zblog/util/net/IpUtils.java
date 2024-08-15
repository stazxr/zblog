package com.github.stazxr.zblog.util.net;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.stazxr.zblog.util.Constants;
import com.github.stazxr.zblog.util.math.Base36Converter;
import com.github.stazxr.zblog.util.math.BigIntegerLongConversion;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.*;

/**
 * IP address utility class.
 *
 * IPv4 addresses are represented as 32-bit unsigned integers [0, 2^32-1].
 * Each octet (segment) of the IP address is represented by numbers ranging from 0 to 255.
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

    /**
     * Maximum value for IP address counter.
     */
    public static final int MAX_IP_COUNT = 17;

    private static final int IP4_PART_NUM = 4;

    /**
     * Numeric representation of IPv4 addresses ranges from 0 to 2^32-1.
     */
    private static final long IP_NUM_MAX_RANGE = (255L << 24) | (255L << 16) | (255L << 8) | 255L;

    private static final int IP4_STRING_LENGTH = 7;

    private static final long BYTE_MASK = 0xFF;

    private static final String LOG_PREFIX = "ZUTL [IpUtil]: ";

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
    private static final String LOCAL_IP_LOCATION = "局域网";
    private static final List<String> LOCAL_LIST = Arrays.asList("局域网", "本地局域网", "内网IP");
    private static final String IP_SEARCH_URL = "https://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";

    static {
        CACHE.put(IP_LOCATION_PRO, CACHE1);
        CACHE.put(IP_LOCATION_PRO_CITY, CACHE2);
        CACHE.put(IP_LOCATION_PRO_CITY_REGION, CACHE3);
    }

    /**
     * Retrieves the browser information from the request.
     *
     * @param request The HTTP request
     * @return The browser information
     */
    public static String getBrowser(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * Retrieves the IP address from the request.
     *
     * @param request The HTTP request
     * @return The IP address
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
                // Get the actual local IP address
                ip = LocalHostUtils.getLocalHostAddress();
            } catch (UnknownHostException e) {
                log.error("Exception occurred while retrieving the local IP address", e);
            }
        }

        // For cases where there are multiple proxies, the first IP is the client's real IP,
        // and multiple IPs are separated by ','
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
            log.error("根据IP获取地理位置信息发生异常: {} -> {}", IP_SEARCH_URL, e.getMessage());
            return "";
        }
    }

    /**
     * Converts a string format IPv4 address to its numeric representation.
     *
     * This method accepts an IPv4 address in dotted-decimal format (e.g., "192.168.0.1"),
     * and converts it into a long integer. The numeric representation facilitates storage
     * and comparison of IP addresses.
     *
     * @param ip string format IPv4 address
     * @return numeric representation of IPv4 address
     * @throws IllegalArgumentException if the IP address is null, blank, malformed, or contains parts out of range [0, 255]
     */
    public static long formatIpToNum(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException(LOG_PREFIX + "IP is null or blank");
        }

        String[] ipArray = ip.split("\\.");
        if (ipArray.length != IP4_PART_NUM) {
            throw new IllegalArgumentException(LOG_PREFIX + "Invalid IP format: " + ip);
        }

        long ipNum = 0L;
        try {
            for (int i = 0; i < ipArray.length; i++) {
                int part = Integer.parseInt(ipArray[i]);
                if (part < 0 || part > 255) {
                    throw new IllegalArgumentException(LOG_PREFIX + "IP part out of range [0-255]: " + ipArray[i]);
                }

                // Shift the current part left by the appropriate number of bits (24, 16, 8, 0 respectively) and merge into ipNum using bitwise OR
                ipNum |= (long) part << (8 * (3 - i));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(LOG_PREFIX + "Invalid IP part: " + ip, e);
        }

        return ipNum;
    }

    /**
     * Converts a numeric representation of IPv4 address to string format.
     *
     * This method accepts a long integer representing an IPv4 address,
     * and converts it into dotted-decimal string format.
     * For example, converts numeric value 4294967295 to string "255.255.255.255".
     *
     * @param ipNum numeric representation of IPv4 address
     * @return dotted-decimal string format of IPv4 address
     * @throws IllegalArgumentException if ipNum is out of valid range [0, 4294967295]
     */
    public static String parseNumToIp(long ipNum) {
        if (ipNum < 0 || ipNum > IP_NUM_MAX_RANGE) {
            throw new IllegalArgumentException(LOG_PREFIX + String.format("IP num out of range [0, %d]: %d", IP_NUM_MAX_RANGE, ipNum));
        }

        return String.valueOf((ipNum >> 24) & BYTE_MASK) + '.' +
                ((ipNum >> 16) & BYTE_MASK) + '.' +
                ((ipNum >> 8) & BYTE_MASK) + '.' +
                (ipNum & BYTE_MASK);
    }

    /**
     * Generates a 7-character string representation based on the provided IP address and count.
     * If count is not provided, defaults to 0.
     *
     * @param ip IP address string
     * @return generated 7-character string
     * @throws IllegalArgumentException if count is out of valid range [0, 17]
     */
    public static String get7CharFromIpString(String ip) {
        return get7CharFromIpString(ip, 0);
    }

    /**
     * Generates a 7-character string representation based on the provided IP address and count.
     *
     * @param ip IP address string
     * @param count count value, range [0, 17]
     * @return generated 7-character string
     * @throws IllegalArgumentException if count is out of valid range [0, 17]
     */
    public static String get7CharFromIpString(String ip, int count) {
        if (count < 0 || count > MAX_IP_COUNT) {
            throw new IllegalArgumentException(LOG_PREFIX + "Count out of range [0, 17]: " + count);
        }

        // Convert IP address to long numeric value and concatenate count in high bits
        long ipNum = formatIpToNum(ip);
        ipNum |= (long) count << 32;

        // Convert IP address and count to Base36 representation ensuring result length is 7 characters
        String parseStr = Base36Converter.decimalToBase36(BigIntegerLongConversion.longToBigInteger(ipNum));
        StringBuilder builder = new StringBuilder("0000000");
        builder.replace(IP4_STRING_LENGTH - parseStr.length(), IP4_STRING_LENGTH, parseStr);
        return builder.toString().toUpperCase();
    }

    /**
     * Parses a 7-character string representation back into IP address and count.
     *
     * @param ipString string representation of IP and count (generated by get7CharFromIpString method)
     * @return [ip, count]
     * @throws IllegalArgumentException if count is out of valid range [0, 17]
     */
    public static String[] parse7CharToIpCountAry(String ipString) {
        if (ipString.length() != IP4_STRING_LENGTH) {
            throw new IllegalArgumentException(LOG_PREFIX + "Ip string length out of range [7]: " + ipString);
        }

        long ipCountNum = BigIntegerLongConversion.bigIntegerToLong(Base36Converter.base36ToDecimal(ipString));
        int count = (int) ((ipCountNum >> 32) & BYTE_MASK);
        String ip = String.valueOf((ipCountNum >> 24) & BYTE_MASK) + '.' +
                ((ipCountNum >> 16) & BYTE_MASK) + '.' +
                ((ipCountNum >> 8) & BYTE_MASK) + '.' +
                (ipCountNum & BYTE_MASK);
        return new String[]{ip, String.valueOf(count)};
    }

    private static boolean ipIsEmpty(String ip) {
        final String unknown = "unknown";
        return ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip);
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
}
