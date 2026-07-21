package com.github.stazxr.zblog.content.ext.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 访客日志查询参数
 *
 * @author SunTao
 * @since 2026-07-21
 */
@Getter
@Setter
@ApiModel("访客日志查询参数")
public class VisitorLogQueryDto extends PageParam {
    private static final long serialVersionUID = -3367529808053518423L;

    /**
     * 访客id
     */
    @ApiModelProperty("访客id")
    private String visitorId;

    /**
     * IP
     */
    @ApiModelProperty("IP")
    private String ip;
}
