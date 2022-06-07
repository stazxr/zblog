package com.github.stazxr.zblog.core.util;

import com.github.stazxr.zblog.core.config.properties.ZblogProperties;
import com.github.stazxr.zblog.util.SpringContextHolder;
import com.github.stazxr.zblog.util.net.IpUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 * IP工具类的实现，增加 getCityInfoImpl 方法
 *
 * @author SunTao
 * @since 2022-06-07
 */
@Slf4j
public class IpImplUtils extends IpUtils {
    private static ZblogProperties zblogProperties;

    @PostConstruct
    private void initObj() {
        // init regionSearcher
        zblogProperties = SpringContextHolder.getBean(ZblogProperties.class);
        if (zblogProperties == null) {
            log.error("can't find class ZblogProperties.");
            System.exit(1);
        }
    }

    /**
     * 根据ip获取详细地址
     *
     * @param ip IP 地址
     * @return 详细地址
     */
    public static String getCityInfo(String ip) {
        return zblogProperties.getIsLocalIpParsing() ? getLocalCityInfo(ip) : getHttpCityInfo(ip);
    }
}
