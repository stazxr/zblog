package com.github.stazxr.zblog.content.ext.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 访客查询参数
 *
 * @author SunTao
 * @since 2026-07-21
 */
@Getter
@Setter
@ApiModel("访客查询参数")
public class VisitorQueryDto extends PageParam {
    private static final long serialVersionUID = -2180533203749616803L;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * IP
     */
    @ApiModelProperty("IP")
    private String ip;
}
