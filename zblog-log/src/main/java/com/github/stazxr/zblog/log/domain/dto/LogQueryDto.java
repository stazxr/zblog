package com.github.stazxr.zblog.log.domain.dto;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 日志列表查询参数
 *
 * @author SunTao
 * @since 2022-08-05
 */
@Getter
@Setter
@ToString
public class LogQueryDto extends PageParam {
    /**
     * 操作人
     */
    private String username;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 日志类型
     */
    private Integer logType;

    /**
     * 权限ID
     */
    private Long permId;

    /**
     * 操作结果
     */
    private Integer execResult;

    /**
     * 请求耗时
     */
    private Double costTime;

    /**
     * 操作开始时间
     */
    private String eventStartTime;

    /**
     * 操作结束时间
     */
    private String eventEndTime;
}
