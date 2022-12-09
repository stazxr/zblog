package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.util.net.IpUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 测试日期工具类
 *
 * @author Thomas Sun
 * @since 2022-08-19
 */
public class IpUtilsTest {
    @Test
    @Ignore
    public void test1() throws Exception {
        String str = "113.247.47.171";
        System.out.println(IpUtils.getHttpCityInfo(str));
        System.out.println(IpUtils.getLocalCityInfo(str));
    }
}
