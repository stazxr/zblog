package com.github.stazxr.zblog.log.domain.dto;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("日志查询参数")
public class LogQueryDto extends PageParam {
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人", notes = "模糊查询")
    private String username;

    /**
     * 操作描述
     */
    @ApiModelProperty(value = "操作描述", notes = "模糊查询")
    private String description;

    /**
     * 日志类型
     */
    @ApiModelProperty("日志类型，1：操作日志、2：接口日志、3：异常日志")
    private Integer logType;

    /**
     * 权限id
     */
    @ApiModelProperty("权限id")
    private Long permId;

    /**
     * 操作结果
     */
    @ApiModelProperty(value = "操作结果", example = "true")
    private Boolean execResult;

    /**
     * 请求耗时
     */
    @ApiModelProperty(value = "请求耗时", example = "1000")
    private Double costTime;

    /**
     * 操作开始时间
     */
    @ApiModelProperty(value = "操作开始时间", example = "2023-02-16 00:00:00")
    private String eventStartTime;

    /**
     * 操作结束时间
     */
    @ApiModelProperty(value = "操作结束时间", example = "2023-02-16 23:59:59")
    private String eventEndTime;
}
