package com.github.stazxr.zblog.util.math;

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
     * 将字符串小数转换为百分比，默认保留两位小数
     *
     * @param doubleNum 小数
     * @return 六位随机验证码
     */
    public static String parsePercent(double doubleNum) {
        return parsePercent(doubleNum, 2);
    }

    /**
     * 将字符串小数转换为百分比，默认保留两位小数
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
}
