package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.util.math.MathUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 测试数学工具类
 *
 * @author Thomas Sun
 * @since 2022-09-02
 */
public class MathUtilsTest {
    @Test
    @Ignore
    public void test1() throws Exception {
        System.out.println(MathUtils.parsePercent("123.123123"));
        System.out.println(MathUtils.parsePercent(0.12312));
        System.out.println(MathUtils.parsePercent(3.12312, 2));
    }

    @Test
    @Ignore
    public void test2() throws Exception {
        BigDecimal preNum = new BigDecimal("10044");
        BigDecimal nowNum = new BigDecimal("10029");
        BigDecimal subtract = nowNum.subtract(preNum);
        BigDecimal divide = subtract.divide(preNum, 4, RoundingMode.HALF_UP);
        System.out.println(MathUtils.parsePercent(divide.doubleValue(), 1));
    }

    @Test
    @Ignore
    public void test3() throws Exception {
        System.out.println(MathUtils.calCompareData(14.6, 15.6, 1));
        System.out.println(MathUtils.calCompareData(0, 10029, 1));
        System.out.println(MathUtils.calCompareData(10044, 0, 1));

        System.out.println(MathUtils.calCompareData(10029, 10044, 1));
        System.out.println(MathUtils.calCompareData(0.0, 10029, 1));
        System.out.println(MathUtils.calCompareData("0.0", "10044", 1));

        BigDecimal bigDecimal = new BigDecimal("0.0");
        System.out.println(BigDecimal.ZERO.equals(bigDecimal)); // false
        System.out.println(BigDecimal.ZERO.compareTo(bigDecimal)); // 0
    }
}
