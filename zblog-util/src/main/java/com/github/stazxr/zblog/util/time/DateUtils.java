package com.github.stazxr.zblog.util.time;

import com.github.stazxr.zblog.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author SunTao
 * @since 2020-11-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    /**
     * 默认时间格式
     */
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String YMD_HMS_PATTERN = DEFAULT_PATTERN;

    /**
     * yyyy-MM-dd
     */
    public static final String YMD_PATTERN = "yyyy-MM-dd";

    /**
     * 格式化现在
     *
     * @return 当前时间的字符串 "yyyy-MM-dd HH:mm:ss"
     */
    public static String formatNow() {
        return formatTime();
    }

    /**
     * 格式化现在
     *
     * @return 当前时间的字符串 "yyyy-MM-dd HH:mm:ss"
     */
    public static String formatTime() {
        return format(new Date(), YMD_HMS_PATTERN);
    }

    /**
     * 格式化现在
     *
     * @return 当前时间的字符串 "yyyy-MM-dd HH:mm:ss"
     */
    public static String formatDate() {
        return format(new Date(), YMD_PATTERN);
    }

    /**
     * 格式化当前时间
     *
     * @param pattern 表达式
     * @return String
     */
    public static String formatNow(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 格式化日期为字符串，默认格式为"yyyy-MM-dd HH:mm:ss"
     *
     * @param date 日期
     * @return "yyyy-MM-dd HH:mm:ss"
     */
    public static String format(Date date) {
        return format(date, DEFAULT_PATTERN);
    }

    /**
     * 格式化日期为字符串
     *
     * @param date    日期，为空，则为初始日期
     * @param pattern 格式
     * @return String 格式化后的日期
     */
    public static String format(Date date, String pattern) {
        if (null == date) {
            date = new Date(0);
        }
        String pattern2 = StringUtils.isEmpty(pattern) ? DEFAULT_PATTERN : pattern;
        return new SimpleDateFormat(pattern2).format(date);
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return 当前时间的时间戳
     */
    public static long getLongTime() {
        return System.currentTimeMillis();
    }

    /**
     * 格式话Long时间
     *
     * @param longTime mills
     * @return String
     */
    public static String formatLongTime(long longTime) {
        return format(new Date(longTime));
    }

    /**
     * 格式话Long时间
     *
     * @param longTime mills
     * @param pattern 输出格式
     * @return String
     */
    public static String formatLongTime(long longTime, String pattern) {
        return format(new Date(longTime), pattern);
    }

    /**
     * 格式话时间
     *
     * @param dateStr 日期
     * @return Date
     */
    public static Date parse(String dateStr) throws ParseException {
        return parseDate(dateStr, DEFAULT_PATTERN);
    }

    /**
     * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parseLocalDateTimeFormat(String localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.from(dateTimeFormatter.parse(localDateTime));
    }

    /**
     * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd
     *
     * @param localDateTime /
     * @return /
     */
    public static LocalDateTime parseLocalDateTimeFormat(String localDateTime, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.from(dateTimeFormatter.parse(localDateTime));
    }

    /**
     * 根据两个时间，返回时间差的字符串表示
     *
     * @param endDate 结束时间
     * @param nowDate 当前时间
     * @return String
     */
    public static String getDateDiff(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;

        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();

        // 计算差多少天
        long day = diff / nd;

        // 计算差多少小时
        long hour = diff % nd / nh;

        // 计算差多少分钟
        long min = diff % nd % nh / nm;

        // 计算差多少秒
        long sec = diff % nd % nh % nm / ns;

        return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
    }

    /**
     * 格式化时间为字符串数组
     *
     * @param dateStr 时间字符串 yyyy-MM-dd HH:mm:ss or yyyy-MM-dd
     * @return 时间字符数组
     */
    public static String[] parseDateStrToAry(String dateStr) {
        String[] result = new String[6];
        if (StringUtils.isBlank(dateStr)) {
            return result;
        }

        int i = 0;
        String[] tmp1 = dateStr.contains(" ") ? dateStr.split(" ") : new String[]{dateStr};
        for (String tmp : tmp1) {
            // get item
            String[] tmp2 = tmp.contains("-") ? tmp.split("-") :
                    tmp.contains(":") ? tmp.split(":") : new String[]{tmp};

            // set item
            for (String item : tmp2) {
                result[i++] = item;
            }
        }

        // result
        return result;
    }
}
