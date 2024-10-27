package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 路由信息
 *
 * @author SunTao
 * @since 2022-09-20
 */
@Data
@ApiModel("路由日志状态变更参数")
public class RouterDto {
    /**
     * 请求地址
     */
    @ApiModelProperty(value = "请求地址", required = true)
    private String uri;

    /**
     * 请求方式
     */
    @ApiModelProperty(value = "请求方式", required = true)
    private String method;

    /**
     * 日志是否展示
     */
    @ApiModelProperty(value = "日志是否展示", required = true)
    private Boolean logShowed;
}
