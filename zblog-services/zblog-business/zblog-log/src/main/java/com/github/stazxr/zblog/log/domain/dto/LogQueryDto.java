package com.github.stazxr.zblog.log.domain.dto;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 日志查询参数
 *
 * @author SunTao
 * @since 2022-08-05
 */
@Getter
@Setter
@ApiModel("日志查询参数")
public class LogQueryDto extends PageParam {
    private static final long serialVersionUID = -7041770535475955047L;

    /**
     * 日志类型
     */
    @ApiModelProperty("日志类型")
    private Integer logType;

    /**
     * 流水号
     */
    @ApiModelProperty("流水号")
    private String traceId;

    /**
     * 操作描述
     */
    @ApiModelProperty(value = "操作描述", notes = "模糊查询")
    private String description;

    /**
     * 接口编码
     */
    @ApiModelProperty(value = "接口编码")
    private String interfaceCode;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String username;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventStartTime;

    /**
     * 操作结束时间
     */
    @ApiModelProperty(value = "操作结束时间", example = "2023-02-16 23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventEndTime;
}
