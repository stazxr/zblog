package com.github.stazxr.zblog.bas.log.util;

import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import org.springframework.core.MethodParameter;

/**
 * 日志工具类.
 *
 * @author SunTao
 * @since 2026-02-03
 */
public class LogUtil {
    /**
     * 获取接口编码
     */
    public static String getApiCode(MethodParameter parameter) {
        Router router = parameter.getMethodAnnotation(Router.class);
        return router == null ? "" : router.code();
    }

    /**
     * 获取接口版本
     */
    public static String getApiVersion(MethodParameter parameter) {
        ApiVersion version = parameter.getMethodAnnotation(ApiVersion.class);
        if (version == null) {
            return "";
        }
        return version.value();
    }
}
