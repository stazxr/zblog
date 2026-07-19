package com.github.stazxr.zblog.util.net;

import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * IP地域解析工具
 *
 * @author SunTao
 * @since 2026-07-19
 */
public class IpRegionUtils {
    private static final Logger log = LoggerFactory.getLogger(IpRegionUtils.class);

    private static final String REGION_FILE = "ip2region/ip2region_v4.xdb";

    private static Searcher searcher;

    static {
        try {
            byte[] cBuff;
            try (InputStream is = IpRegionUtils.class.getClassLoader().getResourceAsStream(REGION_FILE)) {
                if (is == null) {
                    throw new IllegalStateException("ip2region file not found:" + REGION_FILE);
                }

                cBuff = readAllBytes(is);
            }
            searcher = Searcher.newWithBuffer(cBuff);
            log.info("ip2region init success");
        } catch (Exception e) {
            log.error("init ip2region error", e);
        }
    }

    private static byte[] readAllBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
        return output.toByteArray();
    }

    /**
     * 查询IP
     */
    public static IpRegion search(String ip) {
        if (ip == null || ip.length() == 0) {
            return new IpRegion();
        }

        try {
            String region = searcher.search(ip);

            /*
             * 国家|区域|省份|城市|ISP
             *
             * 中国|0|广东省|深圳市|电信
             */
            String[] arr = region.split("\\|");
            IpRegion ipRegion = new IpRegion();
            ipRegion.setCountry(format(arr, 0));
            ipRegion.setProvince(format(arr, 2));
            ipRegion.setCity(format(arr, 3));
            ipRegion.setIsp(format(arr, 4));
            return ipRegion;
        } catch (Exception e) {
            log.warn("ip region search error:{}", ip, e);
            return new IpRegion();
        }
    }

    /**
     * 查询IP归属地字符串
     *
     * @param ip IP地址
     * @return 国家省份城市区县
     */
    public static String getRegion(String ip) {
        IpRegion region = search(ip);
        StringBuilder builder = new StringBuilder();
        append(builder, region.getCountry());
        append(builder, region.getProvince());
        append(builder, region.getCity());
        append(builder, region.getDistrict());
        return builder.toString().trim();
    }

    private static void append(StringBuilder builder, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        builder.append(value);
    }

    private static String format(String[] arr, int index) {
        if (arr.length <= index) {
            return "";
        }

        String value = arr[index];
        if ("0".equals(value) || "Reserved".equalsIgnoreCase(value) || "Unknown".equalsIgnoreCase(value)) {
            return "";
        }

        return value;
    }
}
