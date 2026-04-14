package com.github.stazxr.zblog.content.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.stazxr.zblog.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 文章信息
 *
 * @author SunTao
 * @since 2022-12-03
 */
@Getter
@Setter
@ApiModel("文章DTO")
public class ArticleDto extends BaseDto {
    private static final long serialVersionUID = -5043752428070824230L;

    /**
     * 文章ID
     */
    @ApiModelProperty("文章ID")
    private Long id;

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    private String title;

    /**
     * URL标识
     */
    @ApiModelProperty("URL标识")
    private String slug;

    /**
     * 文章概要
     */
    @ApiModelProperty("文章概要")
    private String summary;

    /**
     * 文章内容（Markdown）
     */
    @ApiModelProperty("文章内容（Markdown）")
    private String contentMd;

    /**
     * 文章内容（HTML）
     */
    @ApiModelProperty("文章内容（HTML）")
    private String contentHtml;

    /**
     * 分类ID
     */
    @ApiModelProperty("分类ID")
    private Long categoryId;

    /**
     * 作者ID
     */
    @ApiModelProperty("作者ID")
    private Long authorId;

    /**
     * SEO标题
     */
    @ApiModelProperty("SEO标题")
    private String seoTitle;

    /**
     * SEO关键词
     */
    @ApiModelProperty("SEO关键词")
    private String seoKeywords;

    /**
     * SEO描述
     */
    @ApiModelProperty("SEO描述")
    private String seoDescription;

    /**
     * 文章类型
     *
     * @see com.github.stazxr.zblog.content.domain.enums.ArticleType
     */
    @ApiModelProperty("文章类型")
    private Integer articleType;

    /**
     * 文章权限
     *
     * @see com.github.stazxr.zblog.content.domain.enums.ArticlePerm
     */
    @ApiModelProperty("文章权限")
    private Integer articlePerm;

    /**
     * 访问密码
     */
    @ApiModelProperty("访问密码")
    private String password;

    /**
     * 原文地址
     */
    @ApiModelProperty("原文地址")
    private String reprintLink;

    /**
     * 转载说明
     */
    @ApiModelProperty("转载说明")
    private String reprintDesc;

    /**
     * 是否允许评论
     */
    @ApiModelProperty("是否允许评论")
    private Boolean commentFlag;

    /**
     * 封面类型
     *
     * @see com.github.stazxr.zblog.content.domain.enums.ArticleCoverImgType
     */
    @ApiModelProperty("封面类型")
    private Integer coverImageType;

    /**
     * 文章发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("文章发布时间")
    private LocalDateTime publishTime;
}
