package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 导入工作日节假日信息
 *
 * @author SunTao
 * @since 2022-04-12
 */
@Data
@ToString
public class CalendarImportDto {
    private String url;

    private String fileId;
}
