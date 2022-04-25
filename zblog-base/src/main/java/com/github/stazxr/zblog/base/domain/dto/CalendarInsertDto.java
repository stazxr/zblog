package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 新增修改工作日节假日信息
 *
 * @author SunTao
 * @since 2022-04-11
 */
@Data
@ToString
public class CalendarInsertDto {
    private String date;

    private String remark;

    private String type;
}
