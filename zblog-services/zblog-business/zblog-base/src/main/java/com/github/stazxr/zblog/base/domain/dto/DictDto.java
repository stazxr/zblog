package com.github.stazxr.zblog.base.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 字典信息
 *
 * @author SunTao
 * @since 2022-09-21
 */
@Data
@ApiModel("字典信息")
public class DictDto {
    /**
     * 字典序列（黑白名单用）
     */
    @ApiModelProperty(name = "dictId", value = "字典id（路由黑白名单相关接口使用）")
    private Long dictId;

    /**
     * 字典id
     */
    @ApiModelProperty(name = "id", value = "字典id")
    private Long id;

    /**
     * 字典pid
     */
    @ApiModelProperty(name = "pid", value = "字典pid")
    private Long pid;

    /**
     * 字典名称
     */
    @ApiModelProperty(name = "name", value = "字典名称")
    private String name;

    /**
     * 字典类型：1，组、2，项
     */
    @ApiModelProperty(name = "type", value = "字典类型，1：组、2：项", example = "1")
    private Integer type;

    /**
     * 字典key
     */
    @ApiModelProperty(name = "key", value = "字典key")
    private String key;

    /**
     * 字典value
     */
    @ApiModelProperty(name = "value", value = "字典value")
    private String value;

    /**
     * 字典描述
     */
    @ApiModelProperty(name = "desc", value = "字典描述")
    private String desc;

    /**
     * 字典排序
     */
    @ApiModelProperty(name = "sort", value = "字典排序", example = "99999")
    private Integer sort;

    /**
     * 字典状态
     */
    @ApiModelProperty(name = "enabled", value = "字典状态", example = "true")
    private Boolean enabled;
}
