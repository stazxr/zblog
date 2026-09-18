package com.github.stazxr.zblog.content.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 文章新增/编辑信息
 *
 * @author SunTao
 * @since 2026-09-15
 */
@Getter
@Setter
@ApiModel(value = "文章DTO", description = "文章新增/编辑参数")
public class ArticleDto implements Serializable {
    private static final long serialVersionUID = 5062863709675309457L;

    /**
     * 文章ID
     */
    @ApiModelProperty(value = "文章ID")
    private Long id;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题", required = true)
    private String title;

    /**
     * 路径标识
     */
    @ApiModelProperty(value = "路径标识", required = true)
    private String slug;

    /**
     * 文章概要
     */
    @ApiModelProperty(value = "文章概要")
    private String summary;

    /**
     * 文章内容（Markdown）
     */
    @ApiModelProperty(value = "文章内容（Markdown）", required = true)
    private String contentMd;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID", required = true)
    private Long categoryId;

    /**
     * 封面类型
     */
    @ApiModelProperty(value = "封面类型", required = true)
    private Integer coverImageType;

    /**
     * 封面图片列表
     */
    @ApiModelProperty(value = "封面图片")
    private List<String> articleImages;

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
     * 作者ID
     */
    @ApiModelProperty(value = "作者ID", example = "1")
    private Long authorId;

    /**
     * 文章类型
     */
    @ApiModelProperty(value = "文章类型", required = true)
    private Integer articleType;

    /**
     * 文章权限
     * <p>
     * 1-公开；2-私密；3-密码
     */
    @ApiModelProperty(value = "文章权限：1-公开；2-私密；3-密码", required = true)
    private Integer articlePerm;

    /**
     * 访问密码
     */
    @ApiModelProperty(value = "访问密码")
    private String accessPassword;

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
    @ApiModelProperty(value = "是否允许评论", example = "true")
    private Boolean commentFlag;

    /**
     * 是否置顶
     */
    @ApiModelProperty(value = "是否置顶", example = "false")
    private Boolean topFlag;

    /**
     * 是否推荐
     */
    @ApiModelProperty(value = "是否推荐", example = "false")
    private Boolean recommendFlag;

    /**
     * 发布时间
     * <p>
     * 定时发布时使用。
     */
    @ApiModelProperty(value = "发布时间", example = "2026-09-15 10:00:00")
    private String publishTime;

    /**
     * 扩展字段
     */
    @ApiModelProperty(value = "扩展字段")
    private String extraInfo;
}