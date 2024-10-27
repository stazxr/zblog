package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 类别信息
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ToString
@ApiModel("类别信息")
public class ArticleCategoryDto {
    /**
     * 类别id
     */
    @ApiModelProperty("类别id")
    private Long id;

    /**
     * 类别pid
     */
    @ApiModelProperty("类别pid")
    private Long pid;

    /**
     * 类别名称
     */
    @ApiModelProperty("类别名称")
    private String name;

    /**
     * 类别预览图
     */
    @ApiModelProperty("类别预览图")
    private String imageUrl;

    /**
     * 分类描述
     */
    @ApiModelProperty("分类描述")
    private String desc;

    /**
     * 分类排序
     */
    @ApiModelProperty("分类排序")
    private Integer sort;

    /**
     * 分类状态
     */
    @ApiModelProperty("分类状态")
    private Boolean enabled;
}
