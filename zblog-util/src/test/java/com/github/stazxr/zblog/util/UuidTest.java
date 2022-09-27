package com.github.stazxr.zblog.util;

import org.junit.Ignore;
import org.junit.Test;

import java.util.IntSummaryStatistics;

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

    @Test
    @Ignore
    public void test2() throws Exception {
        // 空对象默认值为0
        IntSummaryStatistics intSummaryStatistics = new IntSummaryStatistics();
        long sum = intSummaryStatistics.getSum();
        System.out.println(sum);
    }
}
