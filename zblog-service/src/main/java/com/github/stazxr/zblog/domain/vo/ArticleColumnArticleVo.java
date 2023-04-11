package com.github.stazxr.zblog.domain.vo;

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
public class ArticleColumnArticleVo {
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

    /**
     * 文章状态
     */
    @ApiModelProperty("文章状态")
    private String articleStatus;

    /**
     * 创建用户
     */
    @ApiModelProperty("创建用户")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createTime;
}
