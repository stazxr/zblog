package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 标签信息
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ToString
@ApiModel("标签信息")
public class ArticleTagDto {
    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private Long id;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String name;

    /**
     * 标签类型
     */
    @ApiModelProperty("标签类型，1：公共、2：定制")
    private Integer type;

    /**
     * 标签状态
     */
    @ApiModelProperty(value = "标签状态", example = "true")
    private Boolean enabled;
}
