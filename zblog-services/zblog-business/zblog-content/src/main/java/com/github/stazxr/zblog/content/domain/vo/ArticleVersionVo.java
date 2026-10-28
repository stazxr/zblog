package com.github.stazxr.zblog.content.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章发布信息
 *
 * @author SunTao
 * @since 2026-10-27
 */
@Getter
@Setter
@ApiModel(value = "ArticleVersionVo", description = "文章发布信息")
public class ArticleVersionVo implements Serializable {
    private static final long serialVersionUID = -8970244640576138977L;

    /**
     * 文章版本id
     */
    @ApiModelProperty(value = "文章版本id")
    private Long id;

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private Long articleId;

    /**
     * 发布版本
     */
    @ApiModelProperty(value = "发布版本")
    private Integer version;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String title;

    /**
     * 路径标识
     */
    @ApiModelProperty(value = "路径标识")
    private String slug;

    /**
     * 文章概要
     */
    @ApiModelProperty(value = "文章概要")
    private String summary;

    /**
     * 文章内容（Markdown）
     */
    @ApiModelProperty(value = "文章内容（Markdown）")
    private String contentMd;

    /**
     * 文章内容（HTML）
     */
    @ApiModelProperty(value = "文章内容（HTML）")
    private String contentHtml;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    /**
     * 标签名称列表
     */
    @ApiModelProperty(value = "标签名称列表")
    private List<String> tagNames;

    /**
     * SEO标题
     */
    @ApiModelProperty(value = "SEO标题")
    private String seoTitle;

    /**
     * SEO关键词
     */
    @ApiModelProperty(value = "SEO关键词")
    private String seoKeywords;

    /**
     * SEO描述
     */
    @ApiModelProperty(value = "SEO描述")
    private String seoDescription;

    /**
     * 文章类型
     */
    @ApiModelProperty(value = "文章类型")
    private Integer articleType;

    /**
     * 文章权限
     */
    @ApiModelProperty(value = "文章权限")
    private Integer articlePerm;

    /**
     * 文章来源名称
     */
    @ApiModelProperty(value = "文章来源名称")
    private String sourceName;

    /**
     * 原作者
     */
    @ApiModelProperty(value = "原作者")
    private String sourceAuthor;

    /**
     * 原文地址
     */
    @ApiModelProperty(value = "原文地址")
    private String sourceUrl;

    /**
     * 是否允许评论
     */
    @ApiModelProperty(value = "是否允许评论")
    private Boolean commentFlag;

    /**
     * 封面类型
     */
    @ApiModelProperty(value = "封面类型")
    private Integer coverImageType;

    /**
     * 封面图片列表
     */
    @ApiModelProperty(value = "封面图片")
    private List<String> coverImageList;

    /**
     * 总字数
     */
    @ApiModelProperty(value = "总字数")
    private Integer wordsCount;

    /**
     * 版本发布时间
     */
    @ApiModelProperty(value = "版本发布时间")
    private LocalDateTime publishTime;

    /**
     * 扩展字段
     */
    @ApiModelProperty(value = "扩展字段")
    private String extraInfo;
}
