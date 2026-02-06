package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 用户操作日志查询
 *
 * @author SunTao
 * @since 2022-08-25
 */
@Getter
@Setter
@ApiModel("用户操作日志查询")
public class UserLogQueryDto extends PageParam {
    private static final long serialVersionUID = -4377003561600825464L;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 操作描述
     */
    @ApiModelProperty(value = "操作描述", notes = "模糊查询")
    private String description;

    /**
     * 操作结果
     */
    @ApiModelProperty(value = "操作结果", example = "true")
    private Boolean execResult;

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
