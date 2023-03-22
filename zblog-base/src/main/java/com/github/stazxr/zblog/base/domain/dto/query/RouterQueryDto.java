package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 路由查询
 *
 * @author SunTao
 * @since 2022-09-16
 */
@Getter
@Setter
@ToString
@ApiModel("路由查询参数")
public class RouterQueryDto extends PageParam {
    /**
     * 接口名称
     */
    @ApiModelProperty(value = "接口名称", notes = "模糊查询")
    private String name;

    /**
     * 接口编码
     */
    @ApiModelProperty(value = "接口编码", notes = "模糊查询")
    private String code;

    /**
     * 接口地址
     */
    @ApiModelProperty(value = "接口地址", notes = "模糊查询")
    private String uri;

    /**
     * 访问级别
     */
    @ApiModelProperty(value = "访问级别")
    private String level;

    /**
     * 接口状态
     */
    @ApiModelProperty(value = "接口状态")
    private String status;

    /**
     * 日志状态
     */
    @ApiModelProperty(value = "日志状态")
    private Boolean logShowed;

    /**
     * 字典key
     */
    @ApiModelProperty(value = "字典key")
    private String dictKey;
}
