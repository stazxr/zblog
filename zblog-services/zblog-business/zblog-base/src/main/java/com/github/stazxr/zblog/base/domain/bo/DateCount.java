package com.github.stazxr.zblog.base.domain.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * 基于日期的数据量
 *
 * @author SunTao
 * @since 2023-05-30
 */
@Getter
@Setter
public class DateCount {
    /**
     * 日期值
     */
    private String date;

    /**
     * 数量
     */
    private int count;
}
