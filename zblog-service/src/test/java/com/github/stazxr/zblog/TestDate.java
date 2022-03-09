package com.github.stazxr.zblog;

import com.github.stazxr.zblog.util.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestDate {

    @Test
    public void testCalWorkDayCount() throws ParseException {
        String startDateStr = "2022-03-08";
        String endDateStr = "2022-03-13";

        Set<String> workDays = new HashSet<>();
        workDays.add("2022-03-12");
        workDays.add("2022-03-13");

        Set<String> restDays = new HashSet<>();
        restDays.add("2022-03-10");

        Date startDate = DateUtils.parseDate(startDateStr, DateUtils.YMD_PATTERN);
        Date endDate = DateUtils.parseDate(endDateStr, DateUtils.YMD_PATTERN);
        int i = DateUtils.calWorkDayCount(startDate, endDate, workDays, restDays);
        Assert.assertEquals(5, i);
    }
}
