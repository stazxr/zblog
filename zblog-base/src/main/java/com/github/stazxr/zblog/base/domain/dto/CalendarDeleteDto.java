package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 删除工作日节假日信息
 *
 * @author SunTao
 * @since 2022-04-12
 */
@Data
@ToString
public class CalendarDeleteDto {
    private String date;
}
