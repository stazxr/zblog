package com.github.stazxr.zblog.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 专栏文章信息
 *
 * @author SunTao
 * @since 2023-04-06
 */
@Getter
@Setter
@ToString
@ApiModel("专栏文章信息")
public class ArticleColumnArticleDto {
    /**
     * 文章序列
     */
    @ApiModelProperty("文章序列")
    private Long articleId;

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    private String articleTitle;

    /**
     * 排序
     */
    @ApiModelProperty("文章排序")
    private Integer sort;
}
