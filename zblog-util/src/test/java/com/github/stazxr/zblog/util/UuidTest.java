package com.github.stazxr.zblog.util;

import org.junit.Ignore;
import org.junit.Test;

/**
 * UUID测试工具类
 *
 * @author Thomas Sun
 * @since 2022-09-21
 */
public class UuidTest {
    @Test
    @Ignore
    public void test1() throws Exception {
        System.out.println(UuidUtils.uuid());
        System.out.println(UuidUtils.generateShortUuid());
        System.out.println(UuidUtils.generateMiddleUuid());
    }
}
