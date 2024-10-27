package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 专栏信息
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ToString
@ApiModel("专栏信息")
public class ArticleColumnDto {
    /**
     * 专栏id
     */
    @ApiModelProperty("专栏id")
    private Long id;

    /**
     * 专栏名称
     */
    @ApiModelProperty("专栏名称")
    private String name;

    /**
     * 专栏预览图
     */
    @ApiModelProperty("专栏预览图")
    private String imageUrl;

    /**
     * 专栏描述
     */
    @ApiModelProperty("专栏描述")
    private String desc;

    /**
     * 专栏排序
     */
    @ApiModelProperty("专栏排序")
    private Integer sort;

    /**
     * 是否首页展示
     */
    @ApiModelProperty("是否首页展示")
    private Boolean pageShow;

    /**
     * 专栏状态
     */
    @ApiModelProperty("专栏状态")
    private Boolean enabled;

    /**
     * 专栏文章列表
     */
    @ApiModelProperty("专栏文章列表")
    private List<ArticleColumnArticleDto> articleList;
}
