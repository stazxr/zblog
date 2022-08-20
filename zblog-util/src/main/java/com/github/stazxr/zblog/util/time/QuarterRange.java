package com.github.stazxr.zblog.util.time;

import lombok.Data;

import java.util.Date;

/**
 * 季度起止时间
 *
 * @author Sun Tao
 * @since 2022-08-18
 */
@Data
public class QuarterRange {
    private Date startDate;

    private Date endDate;

    private String startDateStr;

    private String endDateStr;
}
