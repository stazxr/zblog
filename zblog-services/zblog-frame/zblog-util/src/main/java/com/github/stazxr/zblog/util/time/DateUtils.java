package com.github.stazxr.zblog.util.time;

import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 日期工具类
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String YMD_HMS_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd
     */
    public static final String YMD_PATTERN = "yyyy-MM-dd";

    /**
     * yyyy-MM
     */
    public static final String YM_PATTERN = "yyyy-MM";

    /**
     * yyyy
     */
    public static final String Y_PATTERN = "yyyy";

    /**
     * 一秒
     */
    public static final long ONE_SECOND_OF_MILL = 1000L;

    /**
     * 默认时间格式
     */
    private static final String DEFAULT_PATTERN = YMD_HMS_PATTERN;

    /**
     * 第一季度月份列表
     */
    private static final String[] FIRST_QUARTER = new String[]{"01", "02", "03"};

    /**
     * 第二季度月份列表
     */
    private static final String[] SECOND_QUARTER = new String[]{"04", "05", "06"};

    /**
     * 第三季度月份列表
     */
    private static final String[] THIRD_QUARTER = new String[]{"07", "08", "09"};

    /**
     * 第四季度月份列表
     */
    private static final String[] FOURTH_QUARTER = new String[]{"10", "11", "12"};

    /**
     * 月份中文名称
     */
    public static final String[] MONTH_ZH = new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

    /**
     * 获取系统默认的时间格式样式
     *
     * @return DEFAULT_PATTERN
     */
    public static String defaultPattern() {
        return DEFAULT_PATTERN;
    }

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
     * @return 当前时间的字符串 "yyyy-MM-dd"
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
     * @param date    日期
     * @param pattern 格式
     * @return String 格式化后的日期
     */
    public static String format(Date date, String pattern) {
        Assert.notNull(date, "待格式化日期不能为空");
        String pattern2 = StringUtils.isEmpty(pattern) ? DEFAULT_PATTERN : pattern;
        return new SimpleDateFormat(pattern2).format(date);
    }

    /**
     * 重新格式化字符串日期为新的格式字符串
     *
     * @param dateStr    字符串日期
     * @param oldPattern 字符串原来的格式
     * @param newPattern 字符串期望的格式
     * @return String 格式化后的日期
     */
    public static String reFormat(String dateStr, String oldPattern, String newPattern) {
        try {
            return format(parseDate(dateStr, oldPattern), newPattern);
        } catch (Exception e) {
            // 格式化失败返回空
            log.warn("reFormat data eor, return oldPattern: [{} & {} & {}]", dateStr, oldPattern, newPattern);
            return oldPattern;
        }
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
     * 检查日期格式是否正确
     *
     * @param dateStr 日期
     * @param pattern 日期格式
     * @return boolean
     */
    public static boolean checkDatePattern(String dateStr, String pattern) {
        try {
            Date date = parseDate(dateStr, pattern);
            Assert.notNull(date, "转换日期为空");
            return true;
        } catch (Exception e) {
            return false;
        }
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
     * @param localDateTime yyyy-MM-dd
     * @return LocalDateTime
     */
    public static LocalDateTime parseLocalDateTimeFormat(String localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.from(dateTimeFormatter.parse(localDateTime));
    }

    /**
     * 字符串转 LocalDateTime ，字符串格式 yyyy-MM-dd
     *
     * @param localDateTime yyyy-MM-dd
     * @return LocalDateTime
     */
    public static LocalDateTime parseLocalDateTimeFormat(String localDateTime, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.from(dateTimeFormatter.parse(localDateTime));
    }

    /**
     * 格式化 LocalDate 为字符串
     *
     * @param date    日期
     * @return String 格式化后的日期
     */
    public static String format(LocalDate date) {
        return format(date, YMD_PATTERN);
    }

    /**
     * 格式化 LocalDate 为字符串
     *
     * @param date    日期
     * @param pattern 格式
     * @return String 格式化后的日期
     */
    public static String format(LocalDate date, String pattern) {
        Assert.notNull(date, "待格式化日期不能为空");
        String pattern2 = StringUtils.isEmpty(pattern) ? YMD_PATTERN : pattern;
        return DateTimeFormatter.ofPattern(pattern2).format(date);
    }

    /**
     * 格式化 LocalDateTime 为字符串
     *
     * @param date    日期
     * @return String 格式化后的日期
     */
    public static String format(LocalDateTime date) {
        return format(date, YMD_HMS_PATTERN);
    }

    /**
     * 格式化 LocalDateTime 为字符串
     *
     * @param date    日期
     * @param pattern 格式
     * @return String 格式化后的日期
     */
    public static String format(LocalDateTime date, String pattern) {
        Assert.notNull(date, "待格式化日期不能为空");
        String pattern2 = StringUtils.isEmpty(pattern) ? YMD_HMS_PATTERN : pattern;
        return DateTimeFormatter.ofPattern(pattern2).format(date);
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
     * 获取耗时描述
     *
     * @param cost long time
     * @return ** 秒 ** 毫秒
     */
    public static String printCostTime(long cost) {
        if (cost >= ONE_SECOND_OF_MILL) {
            long s = cost / ONE_SECOND_OF_MILL;
            long ms = cost % ONE_SECOND_OF_MILL;
            return s + "秒" + ms + "毫秒";
        } else {
            return cost + "毫秒";
        }
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

    /**
     * 判断某天是否是周末（周六或周日）
     *
     * @param date 判断日期
     * @return boolean
     */
    public static boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int weekFlag = calendar.get(Calendar.DAY_OF_WEEK);
        return weekFlag == Calendar.SATURDAY || weekFlag == Calendar.SUNDAY;
    }

    /**
     * 计算两个日期之间的天数
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 天数
     */
    public static int calDayCount(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate or endDate must not be null");
        }

        return (int) ((endDate.getTime() - startDate.getTime()) / (24L * 3600L * 1000L)) + 1;
    }

    /**
     * 计算两个日期之间的工作日天数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param workDays  工作日列表，yyyy-MM-dd格式
     * @param restDays  节假日列表，yyyy-MM-dd格式
     * @return 天数
     */
    public static int calWorkDayCount(Date startDate, Date endDate, Set<String> workDays, Set<String> restDays) throws ParseException {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate or endDate must not be null");
        }

        // deal workDays and restDays
        Set<String> workDaysTmp = workDays == null ? new HashSet<>() : workDays;
        Set<String> restDaysTmp = restDays == null ? new HashSet<>() : restDays;

        // 获取日期列表
        Set<String> days = findDays(startDate, endDate);

        // 规则计算，判断是否是周六或周天，如果是，则判断当天是否在 workDays 中配置，如果不是，则判断当天是否在 restDays 中配置
        int workDayCount = 0;
        for (String day : days) {
            Date tmp = parseDate(day, YMD_PATTERN);
            if (isWeekend(tmp)) {
                if (workDaysTmp.contains(day)) {
                    workDayCount++;
                }
            } else {
                if (!restDaysTmp.contains(day)) {
                    workDayCount++;
                }
            }
        }

        return workDayCount;
    }

    /**
     * 计算两个日期之间所有的天数
     *
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return 日期的集合, 集合格式: [startTime, endTime]
     */
    public static Set<String> findDays(Date startTime, Date endTime) {
        return findDays(startTime, endTime, true, "yyyy-MM-dd");
    }

    /**
     * 计算两个日期之间所有的天数
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @param pattern   日期格式
     * @return 日期的集合, 集合格式: [startTime, endTime]
     */
    public static Set<String> findDays(Date startTime, Date endTime, String pattern) {
        return findDays(startTime, endTime, true, pattern);
    }

    /**
     * 计算两个日期之间所有的天数
     *
     * @param startTime  开始日期
     * @param endTime    结束日期
     * @param containEnd 是否包含结束
     * @return 日期的集合
     */
    public static Set<String> findDays(Date startTime, Date endTime, boolean containEnd) {
        return findDays(startTime, endTime, containEnd, "yyyy-MM-dd");
    }

    /**
     * 计算两个日期之间所有的天数
     *
     * @param startTime  开始日期
     * @param endTime    结束日期
     * @param containEnd 是否包含结束
     * @param pattern    日期格式
     * @return 日期的集合
     */
    public static Set<String> findDays(Date startTime, Date endTime, boolean containEnd, String pattern) {
        Calendar startNode = Calendar.getInstance();
        startNode.setTime(startTime);
        Calendar endNode = Calendar.getInstance();
        endNode.setTime(endTime);
        if (containEnd) {
            endNode.add(Calendar.DATE, 1);
        }

        // cal
        Set<String> days = new HashSet<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        while (startNode.before(endNode)) {
            days.add(simpleDateFormat.format(startNode.getTime()));
            startNode.add(Calendar.DAY_OF_YEAR, 1);
        }

        return days;
    }

    /**
     * 获得当天是周几
     *
     * @return "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
     */
    public static String getWeekDay() {
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * Java中获取未来或过去XXX天的日期
     *
     * @param day 天数
     * @param isFront 往后还是往前
     * @param containToday 是否包含今天
     * @return 日期列表，yyyy-MM-dd
     */
    public static String[] getDateAry(int day, boolean isFront, boolean containToday) {
        String[] result = new String[day];
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < day; i++) {
            if (containToday) {
                result[i] = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                calendar.add(Calendar.DATE, isFront ? -1 : 1);
            } else {
                calendar.add(Calendar.DATE, isFront ? -1 : 1);
                result[i] = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            }
        }

        if (isFront && result.length > 1) {
            ArrayUtils.reverse(result);
        }

        return result;
    }

    /**
     * 获取当前季度
     *
     * @return 1、2、3、4
     */
    public static int getCurQuarterNum() {
        return getQuarterNum(new Date());
    }

    /**
     * 获取指定日期所属的季度
     *
     * @param date 日期
     * @return 1、2、3、4
     */
    public static int getQuarterNum(Date date) {
        String month = format(date, "MM");
        if (Arrays.asList(FIRST_QUARTER).contains(month)) {
            return 1;
        } else if (Arrays.asList(SECOND_QUARTER).contains(month)) {
            return 2;
        } else if (Arrays.asList(THIRD_QUARTER).contains(month)) {
            return 3;
        } else if (Arrays.asList(FOURTH_QUARTER).contains(month)) {
            return 4;
        } else {
            throw new RuntimeException("get cur quarter failed: " + month);
        }
    }

    /**
     * 获取当前季度名称
     *
     * @return yyyy年x季度
     */
    public static String getCurQuarterName() {
        return getQuarterName(new Date());
    }

    /**
     * 获取指定日期所属的季度名称
     *
     * @param date 日期
     * @return yyyy年x季度
     */
    public static String getQuarterName(Date date) {
        return getQuarterName("%s年%s季度", date);
    }

    /**
     * 获取指定日期所属的季度名称
     *
     * @param template 模板
     * @param date 日期
     * @return yyyy年x季度
     */
    public static String getQuarterName(String template, Date date) {
        String year = format(date, Y_PATTERN);
        int quarter = getQuarterNum(date);
        return String.format(template, year, quarter);
    }

    /**
     * 计算季度的偏移量
     *
     * @param offset 偏移量
     * @return [年份, 季度]
     */
    public static String[] getQuarterOffset(int offset) {
        return getQuarterOffset(new Date(), offset);
    }

    /**
     * 计算季度的偏移量
     *
     * @param date 基准日期
     * @param offset 偏移量
     * @return [年份, 季度]
     */
    public static String[] getQuarterOffset(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 3 * offset);
        Date offsetDate = calendar.getTime();
        return new String[] {format(offsetDate, Y_PATTERN), String.valueOf(getQuarterNum(offsetDate))};
    }

    /**
     * 获取某年的第一天和最后一天
     *
     * @param year 年份
     * @return { 开始时间 - ${yyyy-MM-dd 00:00:00}, 结束时间 - ${yyyy-MM-dd 23:59:59} }
     */
    public static String[] getYearRange(String year) {
        return getYearRange(year, YMD_HMS_PATTERN);
    }

    /**
     * 获取某年的第一天和最后一天
     *
     * @param year 年份
     * @param formatter 返回格式
     * @return { 开始时间 - ${yyyy-MM-dd 00:00:00}, 结束时间 - ${yyyy-MM-dd 23:59:59} }
     */
    public static String[] getYearRange(String year, String formatter) {
        // 开始日期
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, Integer.parseInt(year));
        start.set(Calendar.DAY_OF_YEAR, start.getActualMinimum(Calendar.DAY_OF_YEAR));
        setActualMinimumDay(start);

        // 结束日期
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, Integer.parseInt(year));
        end.set(Calendar.DAY_OF_YEAR, end.getActualMaximum(Calendar.DAY_OF_YEAR));
        setActualMaximumDay(end);

        // return
        return new String[] {format(start.getTime(), formatter), format(end.getTime(), formatter)};
    }

    /**
     * 获取某季度的第一天和最后一天
     *
     * @param year 年份
     * @param quarter 季度，1、2、3、4
     * @return { 开始时间 - ${yyyy-MM-dd 00:00:00}, 结束时间 - ${yyyy-MM-dd 23:59:59} }
     */
    public static String[] getQuarterRange(String year, int quarter) {
        return getQuarterRange(year, quarter, YMD_HMS_PATTERN);
    }

    /**
     * 获取某季度的第一天和最后一天
     *
     * @param year 年份
     * @param quarter 季度，1、2、3、4
     * @param formatter 格式
     * @return { 开始时间, 结束时间 }
     */
    public static String[] getQuarterRange(String year, int quarter, String formatter) {
        Assert.isTrue(quarter <= 0 || quarter >= 5, "季度的有效范围为: [1, 2, 3, 4]");

        // 开始日期
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, Integer.parseInt(year));
        start.set(Calendar.MONTH, (quarter - 1) * 3);
        start.set(Calendar.DATE, start.getActualMinimum(Calendar.DATE));
        start.set(Calendar.DAY_OF_MONTH, start.getActualMinimum(Calendar.DAY_OF_MONTH));
        setActualMinimumDay(start);

        // 结束日期
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, Integer.parseInt(year));
        end.set(Calendar.MONTH, ((quarter - 1) * 3) + 2);
        end.set(Calendar.DATE, end.getActualMaximum(Calendar.DATE));
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DATE));
        setActualMaximumDay(end);

        // return
        return new String[] {format(start.getTime(), formatter), format(end.getTime(), formatter)};
    }

    /**
     * 计算对比当前前进或后退 ${偏移量} ${单位} 的日期
     *
     * @param offset 偏移量
     * @param unit   单位，eg: Calendar.SECOND，default Calendar.DAY_OF_MONTH
     * @return Date
     */
    public static Date findDate(int offset, Integer unit) {
        if (unit == null) {
            unit = Calendar.DAY_OF_MONTH;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(unit, offset);
        return calendar.getTime();
    }

    /**
     * 计算某天的同比，环比日期
     *
     * @param date 对比日期，格式要求：2022-08-31
     * @return 0: 原日期; 1: 环比日期; 2: 同比日期
     */
    public static List<String> getCompareDate(String date) {
        // eg: 2022-08-31 => return [2022-08-31, 2022-08-30, 2021-08-31]
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YMD_PATTERN);
        LocalDate localDate = LocalDate.parse(date, formatter);

        List<String> result = new ArrayList<>();
        result.add(localDate.format(formatter));
        result.add(localDate.minusDays(1).format(formatter));
        result.add(localDate.minusYears(1).format(formatter));
        return result;
    }

    /**
     * 获取日期所属的周
     *
     * @param date 日期，格式 yyyy-MM-dd
     * @return 周 [1, 12]
     * @throws ParseException 日期格式不正确
     */
    public static int getWeekCountOfYear(String date) throws ParseException {
        Date choose = parseDate(date, YMD_PATTERN);

        // 转为 Calendar 对象
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(0);
        calendar.setTime(choose);

        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        if (week == 1 && calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
            week = calendar.get(Calendar.WEEK_OF_YEAR) + 1;
        }

        return week;
    }

    private static void setActualMaximumDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
    }

    private static void setActualMinimumDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
    }

    /**
     * 计算日期 {date} 前 {amount} {type} 的日期
     *
     * @param date   日期
     * @param amount 偏移量
     * @param type   类型，日、月、年
     * @return Date
     */
    public static Date getLastDate(Date date, int amount, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setActualMinimumDay(calendar);
        calendar.add(type, - amount);
        return calendar.getTime();
    }

    /**
     * 获取月份的中文名称
     *
     * @param date 日期
     * @return 月份
     */
    public static String getMonthZh(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthIndex = calendar.get(Calendar.MONTH);
        return MONTH_ZH[monthIndex];
    }
}
