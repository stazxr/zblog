package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.util.time.DateUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 测试日期工具类
 *
 * @author Thomas Sun
 * @since 2022-08-19
 */
public class DateUtilsTest {
    @Test
    @Ignore
    public void test1() throws Exception {
        System.out.println(DateUtils.getCurQuarterNum());
        System.out.println(DateUtils.getQuarterNum(DateUtils.parseDate("2022-06-12", "yyyy-MM-dd")));
        System.out.println(DateUtils.getQuarterNum(DateUtils.parseDate("2022-12-01", "yyyy-MM-dd")));
        System.out.println(DateUtils.getQuarterNum(DateUtils.parseDate("2022-01-28", "yyyy-MM-dd")));
    }

    @Test
    @Ignore
    public void test2() throws Exception {
        System.out.println(DateUtils.getCurQuarterName());
        System.out.println(DateUtils.getQuarterName(DateUtils.parseDate("2022-06-12", "yyyy-MM-dd")));
        System.out.println(DateUtils.getQuarterName(DateUtils.parseDate("2022-12-01", "yyyy-MM-dd")));
        System.out.println(DateUtils.getQuarterName(DateUtils.parseDate("2022-01-28", "yyyy-MM-dd")));
    }

    @Test
    @Ignore
    public void test3() throws Exception {
        System.out.println(DateUtils.getCurQuarterRange());
        System.out.println(DateUtils.getQuarterRange(DateUtils.parseDate("2022-06-12", "yyyy-MM-dd")));
        System.out.println(DateUtils.getQuarterRange(DateUtils.parseDate("2022-11-01", "yyyy-MM-dd")));
        System.out.println(DateUtils.getQuarterRange(DateUtils.parseDate("2022-01-12", "yyyy-MM-dd"), "yyyy-MM-dd HH:mm:ss"));
    }
}
