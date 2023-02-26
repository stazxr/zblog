package com.github.stazxr.zblog.domain.bo;

import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * 文章数量统计
 *
 * @author SunTao
 * @since 2023-01-03
 */
@Getter
@Setter
@ToString
public class GitCalendarData implements Serializable {
    /**
     * 数据来源
     */
    private String githubUrl;

    /**
     * 数据所属用户
     */
    private String githubUser;

    /**
     * 年度贡献量列表
     */
    private List<List<CountData>> contributions;

    /**
     * 近一年的数据
     */
    private int lastYearCount;

    /**
     * 开始日期
     */
    private String lastYearStart;

    /**
     * 结束日期
     */
    private String lastYearEnd;

    /**
     * 近一月的数据
     */
    private int lastMonthCount;

    /**
     * 开始日期
     */
    private String lastMonthStart;

    /**
     * 结束日期
     */
    private String lastMonthEnd;

    /**
     * 近一周的数据
     */
    private int lastWeekCount;
    /**
     * 开始日期
     */
    private String lastWeekStart;

    /**
     * 结束日期
     */
    private String lastWeekEnd;

    /**
     * 月份列表
     */
    private List<String> months;

    /**
     * 初始化数据
     */
    public void initData(List<Map<String, Object>> data) throws ParseException {
        if (data != null && data.size() > 0) {
            Date lastDate = DateUtils.parseDate((String) data.get(data.size() - 1).get("date"), DateUtils.YMD_PATTERN);
            Date lastMonth = DateUtils.getLastDate(lastDate, 1, Calendar.MONTH);
            this.lastMonthStart = DateUtils.format(lastMonth, DateUtils.YMD_PATTERN);
            Date lastWeek = DateUtils.getLastDate(lastDate, 6, Calendar.DATE);
            this.lastWeekStart = DateUtils.format(lastWeek, DateUtils.YMD_PATTERN);

            // 计算贡献量
            for (int i = 0; i < data.size(); i++) {
                Map<String, Object> datum = data.get(i);
                String date = (String) datum.get("date");
                int count = (int) datum.get("count");

                this.lastYearCount += count;
                if (i == 0) {
                    // 一年的开始
                    this.lastYearStart = date;
                }

                if (i == data.size() - 1) {
                    // 一年的结束
                    this.lastYearEnd = date;
                    this.lastMonthEnd = date;
                    this.lastWeekEnd = date;
                }

                // 近一月判断
                Date now = DateUtils.parseDate(date.concat(" 23:59:59"), DateUtils.YMD_HMS_PATTERN);
                if (now.after(lastMonth)) {
                    this.lastMonthCount += count;
                }

                // 近一周判断
                if (now.after(lastWeek)) {
                    this.lastWeekCount += count;
                }
            }

            this.months = this.getTitleMonths(lastDate);
            this.contributions = this.splitDataList(data);
        }
    }

    private List<String> getTitleMonths(Date date) {
        List<String> months = Arrays.asList(DateUtils.MONTH_ZH);
        String month = DateUtils.getMonthZh(date);
        int index = months.indexOf(month) + 1;
        List<String> preList = months.subList(index, months.size());
        List<String> sufList = months.subList(0, index);
        List<String> result = new ArrayList<>();
        result.addAll(preList);
        result.addAll(sufList);
        return result;
    }

    private List<List<CountData>> splitDataList(List<Map<String, Object>> dataList) {
        List<List<CountData>> result = new ArrayList<>();

        int itemSize = 7;
        List<CountData> item = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            CountData countData = new CountData();
            countData.setDate((String) dataList.get(i).get("date"));
            countData.setCount((int) dataList.get(i).get("count"));
            item.add(countData);

            // 是否满一周
            if (item.size() == itemSize || i == dataList.size() - 1) {
                result.add(item);
                item = new ArrayList<>();
            }
        }

        return result;
    }

    @Getter
    @Setter
    @ToString
    static class CountData {
        /**
         * 日期
         */
        private String date;

        /**
         * 贡献量
         */
        private int count;
    }
}
