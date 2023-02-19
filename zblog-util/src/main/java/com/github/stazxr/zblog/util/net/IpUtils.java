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
import java.util.Arrays;
import java.util.List;

/**
 * IP工具类
 *
 * @author SunTao
 * @since 2022-05-31
 */
@Slf4j
public class IpUtils {
    private static final String LOCAL_IP_SOURCE = "局域网";

    private static final String OUT_IP_SOURCE = "国外";

    private static final String[] LOCAL_AREA_NET = {"局域网", "本地局域网", "内网IP"};

    private static final Ip2regionSearcher IP_SEARCHER = SpringContextHolder.getBean(Ip2regionSearcher.class);

    private static final UserAgentAnalyzer USER_AGENT_ANALYZER = UserAgentAnalyzer.newBuilder()
            .hideMatcherLoadStats().withCache(10000).withField(UserAgent.AGENT_NAME_VERSION).build();

    /**
     * 本地网络
     */
    public static final List<String> LOCAL_LIST = Arrays.asList(LOCAL_AREA_NET);

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

    /**
     * 根据请求获取IP地址
     *
     * @param request request
     * @return ip
     */
    public static String getIp(HttpServletRequest request) {
        final String unknown = "unknown";
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
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
     * @param ip 请求地址
     * @return {
     * 	"ip": "113.247.47.171",
     * 	"pro": "湖南省",
     * 	"proCode": "430000",
     * 	"city": "长沙市",
     * 	"cityCode": "430100",
     * 	"region": "",
     * 	"regionCode": "0",
     * 	"addr": "湖南省长沙市 电信",
     * 	"regionNames": "",
     * 	"err": ""
     * }
     */
    public static JSONObject getHttpCityDetailInfo(String ip) {
        String api = String.format(Constants.Url.IP_URL, ip);
        return JSONUtil.parseObj(HttpUtil.get(api));
    }

    /**
     * 根据ip获取详细地址(http)
     *
     * @param ip IP 地址
     * 中国：省份
     * 内网：返回《局域网》
     * 外国：国外
     */
    public static String getIpSourceByHttp(String ip) {
        final String outOfChinaCode = "999999";
        String api = String.format(Constants.Url.IP_URL, ip);
        JSONObject object = JSONUtil.parseObj(HttpUtil.get(api));
        String address = object.get("addr", String.class).trim();
        String proCode = object.get("proCode", String.class).trim();
        if (LOCAL_LIST.contains(address)) {
            return LOCAL_IP_SOURCE;
        } else if (outOfChinaCode.equals(proCode)) {
            return OUT_IP_SOURCE;
        } else {
            return address.length() > 2 ? address.substring(0, 2) : address;
        }
    }

    /**
     * 根据ip获取详细地址(本地)
     *
     * IpInfo(cityId=null, country=中国, region=null, province=上海, city=上海, isp=电信, dataPtr=131121)
     * IpInfo(cityId=null, country=中国, region=null, province=台湾, city=台北, isp=null, dataPtr=22135)
     * IpInfo(cityId=null, country=加拿大, region=null, province=不列颠哥伦比亚, city=温哥华, isp=null, dataPtr=111162)
     * IpInfo(cityId=null, country=null, region=null, province=null, city=内网IP, isp=内网IP, dataPtr=16392)
     *
     * @param ip IP 地址
     * @return
     * 中国：返回省份
     * 内网：返回《局域网》
     * 外国：返回国家
     */
    public static String getIpSourceByLocal(String ip) {
        if (IP_SEARCHER == null) {
            throw new IllegalStateException("bean Ip2regionSearcher not exist");
        }
        IpInfo ipInfo = IP_SEARCHER.memorySearch(ip);
        return getAddressFromIpInfo(ipInfo);
    }

    /**
     * 从IpInfo对象中获取IP来源
     *
     * @param ipInfo IP信息
     * @return IP来源
     */
    protected static String getAddressFromIpInfo(IpInfo ipInfo) {
        if (ipInfo == null) {
            return "";
        }

        final String china = "中国";
        final String local = "内网IP";
        String country = ipInfo.getCountry();
        if (country == null && local.equals(ipInfo.getCity())) {
            return LOCAL_IP_SOURCE;
        } else if (china.equals(country)) {
            return ipInfo.getProvince();
        } else {
            return country;
        }
    }
}
