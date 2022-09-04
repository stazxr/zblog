package com.github.stazxr.zblog.util.math;

import com.github.stazxr.zblog.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * 算术工具类
 *
 * @author SunTao
 * @since 2022-04-01
 */
public class MathUtils {
    /**
     * 将字符串小数转换为百分比，默认保留两位小数
     *
     * @param doubleNum 小数字符串
     * @return 六位随机验证码
     */
    public static String parsePercent(String doubleNum) {
        return parsePercent(Double.parseDouble(doubleNum));
    }

    /**
     * 将小数转换为百分比，默认保留两位小数
     *
     * @param doubleNum 小数
     * @return 六位随机验证码
     */
    public static String parsePercent(double doubleNum) {
        return parsePercent(doubleNum, 2);
    }

    /**
     * 将字符串小数转换为百分比
     *
     * @param doubleNum 小数
     * @param maxDigits 小数位
     * @return 六位随机验证码
     */
    public static String parsePercent(double doubleNum, int maxDigits) {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(maxDigits);
        return format.format(doubleNum);
    }

    /**
     * 计算同比/环比
     *
     * @param preNum 原数据
     * @param nowNum 新数据
     * @param scale  小数位
     * @return 百分比
     */
    public static String calCompareData(double preNum, double nowNum, int scale) {
        return calCompareData(new BigDecimal(preNum), new BigDecimal(nowNum), scale);
    }

    /**
     * 计算同比/环比
     *
     * @param preNum 原数据
     * @param nowNum 新数据
     * @param scale  小数位
     * @return 百分比
     */
    public static String calCompareData(String preNum, String nowNum, int scale) {
        if (StringUtils.isBlank(preNum)) {
            return "-";
        }
        return calCompareData(new BigDecimal(preNum), new BigDecimal(nowNum), scale);
    }

    /**
     * 计算同比/环比
     *
     * @param preNum 原数据
     * @param nowNum 新数据
     * @param scale  小数位
     * @return 百分比
     */
    public static String calCompareData(BigDecimal preNum, BigDecimal nowNum, int scale) {
        if (preNum == null || BigDecimal.ZERO.compareTo(preNum) == 0) {
            return "-";
        }

        BigDecimal subtract = nowNum.subtract(preNum);
        BigDecimal divide = subtract.divide(preNum, 4, RoundingMode.HALF_UP);
        return parsePercent(divide.doubleValue(), scale);
    }
}
