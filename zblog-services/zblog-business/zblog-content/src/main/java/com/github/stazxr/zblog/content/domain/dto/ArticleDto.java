package com.github.stazxr.zblog.content.domain.dto;

import com.github.stazxr.zblog.bas.validation.group.Create;
import com.github.stazxr.zblog.bas.validation.group.Update;
import com.github.stazxr.zblog.base.domain.dto.UploadFileDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 文章信息
 *
 * @author SunTao
 * @since 2026-09-15
 */
@Getter
@Setter
@ApiModel(value = "文章DTO")
public class ArticleDto implements Serializable {
    private static final long serialVersionUID = 5062863709675309457L;

    /**
     * 文章ID
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{TECH_PARAM_MISS}")
    @ApiModelProperty(value = "文章ID")
    private Long id;

    /**
     * 文章标题
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{ARTICLE_TITLE_REQUIRED}")
    @ApiModelProperty(value = "文章标题", required = true)
    private String title;

    /**
     * 路径标识
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{ARTICLE_SLUG_REQUIRED}")
    @ApiModelProperty(value = "路径标识", required = true)
    private String slug;

    /**
     * 文章摘要
     */
    @NotBlank(groups = {Create.class, Update.class}, message = "{ARTICLE_SUMMARY_REQUIRED}")
    @ApiModelProperty(value = "文章摘要")
    private String summary;

    /**
     * 文章内容（Markdown）
     */
    @ApiModelProperty(value = "文章内容（Markdown）", required = true)
    private String contentMd;

    /**
     * 封面类型
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{ARTICLE_COVER_IMAGE_TYPE_REQUIRED}")
    @ApiModelProperty(value = "封面类型", required = true)
    private Integer coverImageType;

    /**
     * 封面图片列表
     */
    @ApiModelProperty(value = "封面图片")
    private List<UploadFileDto> coverImages;

    /**
     * 分类ID
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{ARTICLE_CATEGORY_ID_REQUIRED}")
    @ApiModelProperty(value = "分类ID", required = true)
    private Long categoryId;

    /**
     * 文章标签列表
     */
    @ApiModelProperty(value = "文章标签列表")
    private List<String> tags;

    /**
     * 是否允许评论
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{ARTICLE_COMMENT_FLAG_REQUIRED}")
    @ApiModelProperty(value = "是否允许评论", example = "true")
    private Boolean commentFlag;

    /**
     * 文章类型
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{ARTICLE_TYPE_REQUIRED}")
    @ApiModelProperty(value = "文章类型", required = true)
    private Integer articleType;

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
     * 文章权限
     */
    @NotNull(groups = {Create.class, Update.class}, message = "{ARTICLE_PERM_REQUIRED}")
    @ApiModelProperty(value = "文章权限", required = true)
    private Integer articlePerm;

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
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间", example = "2026-09-15 10:00:00")
    private String publishTime;
}