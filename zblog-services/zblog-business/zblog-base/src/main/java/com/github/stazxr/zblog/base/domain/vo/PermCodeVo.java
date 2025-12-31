package com.github.stazxr.zblog.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 权限编码信息
 *
 * @author SunTao
 * @since 2022-08-26
 */
@Getter
@Setter
@ApiModel("权限编码VO")
public class PermCodeVo {
    /**
     * 权限名称
     */
    @ApiModelProperty("权限名称")
    private String name;

    /**
     * 权限编码
     */
    @ApiModelProperty("权限编码")
    private String value;

    /**
     * 访问级别（默认）
     */
    @ApiModelProperty("访问级别（默认）")
    private Integer level;

    /**
     * 是否禁用（select）
     */
    @ApiModelProperty("是否禁用（select）")
    private Boolean disabled;
}
