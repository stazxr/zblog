package com.github.stazxr.zblog.base.domain.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 节假日维护页面展示的数据
 *
 * @author SunTao
 * @since 2022-04-12
 */
@Data
@ToString
public class CalendarVo {
    private String id;

    private String date;

    private String type;

    private String typeName;

    private String remark;

    private String createUser;

    private String createTime;
}
