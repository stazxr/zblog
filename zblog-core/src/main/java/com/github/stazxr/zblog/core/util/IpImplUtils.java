package com.github.stazxr.zblog.core.util;

import com.github.stazxr.zblog.core.config.properties.ZblogProperties;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.SpringContextHolder;
import com.github.stazxr.zblog.util.net.IpUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * IP工具类的实现，增加 getCityInfoImpl 方法
 *
 * @author SunTao
 * @since 2022-06-07
 */
@Slf4j
public class IpImplUtils extends IpUtils {
    private static final ZblogProperties PROPERTIES;

    static {
        PROPERTIES = SpringContextHolder.getBean(ZblogProperties.class);
        if (PROPERTIES == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST, "Bean ZblogProperties not exist.");
        }

        log.info("IP Parse Model [{}]", PROPERTIES.getIsLocalIpParsing() ? "LOCAL" : "HTTP");
    }

    /**
     * 根据ip地址获取IP来源
     *
     * @param ip IP地址
     * @return 详细地址
     */
    public static String getIpSource(String ip) {
        try {
            return PROPERTIES.getIsLocalIpParsing() ? getIpSourceByLocal(ip) : getIpSourceByHttp(ip);
        } catch (Exception e) {
            log.error("getCityInfo catch eor: {}", e.getMessage());
            return "";
        }
    }
}
