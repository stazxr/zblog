package com.github.stazxr.zblog.util;

import com.github.stazxr.zblog.util.time.DateUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.github.stazxr.zblog.util.time.DateUtils.YMD_PATTERN;

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
    public void test3() {
        System.out.println(DateUtils.getCompareDate("2022-01-01"));
        System.out.println(DateUtils.getCompareDate(DateUtils.format(DateUtils.findDate(-1, null), "yyyy-MM-dd")));
    }

    @Test
    @Ignore
    public void test4() {
        // 开始日期: 0, 3, 6, 9
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, 2021);
        start.set(Calendar.MONTH, 9);
        start.set(Calendar.DATE, start.getActualMinimum(Calendar.DATE));
        start.set(Calendar.DAY_OF_MONTH, start.getActualMinimum(Calendar.DAY_OF_MONTH));
        start.set(Calendar.HOUR_OF_DAY, start.getActualMinimum(Calendar.HOUR_OF_DAY));
        start.set(Calendar.MINUTE, start.getActualMinimum(Calendar.MINUTE));
        start.set(Calendar.SECOND, start.getActualMinimum(Calendar.SECOND));
        start.set(Calendar.MILLISECOND, start.getActualMinimum(Calendar.MILLISECOND));

        // 结束日期: 2, 5, 8, 11
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, 2021);
        end.set(Calendar.MONTH, 11);
        end.set(Calendar.DATE, end.getActualMaximum(Calendar.DATE));
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DATE));
        end.set(Calendar.HOUR_OF_DAY, end.getActualMaximum(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, end.getActualMaximum(Calendar.MINUTE));
        end.set(Calendar.SECOND, end.getActualMaximum(Calendar.SECOND));
        end.set(Calendar.MILLISECOND, end.getActualMaximum(Calendar.MILLISECOND));

        System.out.println(DateUtils.format(start.getTime()));
        System.out.println(DateUtils.format(end.getTime()));
    }

    @Test
    @Ignore
    public void test5() {
        System.out.println(Arrays.toString(DateUtils.getQuarterRange("2022", 1)));
        System.out.println(Arrays.toString(DateUtils.getQuarterRange("2022", 2)));
        System.out.println(Arrays.toString(DateUtils.getQuarterRange("2022", 3)));
        System.out.println(Arrays.toString(DateUtils.getQuarterRange("2022", 4)));
        System.out.println(Arrays.toString(DateUtils.getQuarterRange("2021", 4)));

        String[] quarterInfo = DateUtils.getQuarterOffset(0);
        System.out.println(Arrays.toString(quarterInfo));
        System.out.println(Arrays.toString(DateUtils.getQuarterRange(quarterInfo[0], Integer.parseInt(quarterInfo[1]))));

        String[] quarterInfo1 = DateUtils.getQuarterOffset(-1);
        System.out.println(Arrays.toString(quarterInfo1));
        System.out.println(Arrays.toString(DateUtils.getQuarterRange(quarterInfo1[0], Integer.parseInt(quarterInfo1[1]))));

        String[] quarterInfo2 = DateUtils.getQuarterOffset(-3);
        System.out.println(Arrays.toString(quarterInfo2));
        System.out.println(Arrays.toString(DateUtils.getQuarterRange(quarterInfo2[0], Integer.parseInt(quarterInfo2[1]))));
    }

    @Test
    @Ignore
    public void test6() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -3 * 2);

        Date offsetDate = calendar.getTime();
        String[] strings = {DateUtils.format(offsetDate, DateUtils.Y_PATTERN), String.valueOf(DateUtils.getQuarterNum(offsetDate))};
        System.out.println(Arrays.toString(strings));
    }

    @Test
    @Ignore
    public void test7() throws ParseException {
        String date = "2022-01-23";
        Date dateTime = DateUtils.parseDate(date, YMD_PATTERN);
        String[] quarterOffset = DateUtils.getQuarterOffset(dateTime, 0);
        System.out.println(Arrays.toString(DateUtils.getQuarterRange(quarterOffset[0], Integer.parseInt(quarterOffset[1]))));
    }

    @Test
    @Ignore
    public void test8() throws ParseException {
        // 开始日期
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, Integer.parseInt("2021"));
        start.set(Calendar.DAY_OF_YEAR, start.getActualMinimum(Calendar.DAY_OF_YEAR));
        start.set(Calendar.HOUR_OF_DAY, start.getActualMinimum(Calendar.HOUR_OF_DAY));
        start.set(Calendar.MINUTE, start.getActualMinimum(Calendar.MINUTE));
        start.set(Calendar.SECOND, start.getActualMinimum(Calendar.SECOND));
        start.set(Calendar.MILLISECOND, start.getActualMinimum(Calendar.MILLISECOND));

        // 结束日期
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, Integer.parseInt("2021"));
        end.set(Calendar.DAY_OF_YEAR, end.getActualMaximum(Calendar.DAY_OF_YEAR));
        end.set(Calendar.HOUR_OF_DAY, end.getActualMaximum(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, end.getActualMaximum(Calendar.MINUTE));
        end.set(Calendar.SECOND, end.getActualMaximum(Calendar.SECOND));
        end.set(Calendar.MILLISECOND, end.getActualMaximum(Calendar.MILLISECOND));

        // return
        System.out.println(DateUtils.format(start.getTime(), DateUtils.YMD_HMS_PATTERN));
        System.out.println(DateUtils.format(end.getTime(), DateUtils.YMD_HMS_PATTERN));

        System.out.println(Arrays.toString(DateUtils.getYearRange("2021")));
    }

    @Test
    @Ignore
    public void test9() {
        String hour = "";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, - Integer.parseInt(StringUtils.isBlank(hour) ? "0" : hour));
        System.out.println(DateUtils.format(calendar.getTime(), DateUtils.YMD_HMS_PATTERN));
    }

    @Test
    @Ignore
    public void test10() throws ParseException {
        Date choose = DateUtils.parseDate("2023-12-24", YMD_PATTERN);

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

        System.out.println(week);
    }

    @Test
    @Ignore
    public void test11() throws ParseException {
        Date lastDate = DateUtils.parseDate("2023-02-26", DateUtils.YMD_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastDate);
        calendar.add(Calendar.DATE, - 6);
        System.out.println(DateUtils.format(calendar.getTime(), YMD_PATTERN));

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(lastDate);
        calendar2.add(Calendar.MONTH, - 1);
        System.out.println(DateUtils.format(calendar2.getTime(), YMD_PATTERN));

        System.out.println(DateUtils.format(DateUtils.getLastDate(lastDate, 6, Calendar.DATE)));
        System.out.println(DateUtils.format(DateUtils.getLastDate(lastDate, 1, Calendar.MONTH)));
    }

    @Test
    @Ignore
    public void test12() throws ParseException {
        Date date = DateUtils.parseDate("2023-11-26", DateUtils.YMD_PATTERN);
        List<String> months = Arrays.asList(DateUtils.MONTH_ZH);
        String month = DateUtils.getMonthZh(date);
        int index = months.indexOf(month) + 1;
        List<String> preList = months.subList(index, months.size());
        List<String> sufList = months.subList(0, index);
        List<String> result = new ArrayList<>();
        result.addAll(preList);
        result.addAll(sufList);
        System.out.println(result);
    }

    @Test
    @Ignore
    public void test13() throws ParseException {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        System.out.println(DateUtils.format(localDate));

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        System.out.println(DateUtils.format(localDateTime));
    }
}
