package com.github.stazxr.zblog.content.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 文章新增/编辑参数
 *
 * @author SunTao
 * @since 2026-09-15
 */
@Getter
@Setter
@ApiModel(value = "ArticleDTO", description = "文章新增/编辑参数")
public class ArticleDto implements Serializable {
    private static final long serialVersionUID = 5062863709675309457L;

    /**
     * 文章ID
     * <p>
     * 新增时为空，编辑时必填。
     */
    @ApiModelProperty(value = "文章ID", example = "10001")
    private Long id;

    /**
     * 文章标题
     */
    @NotBlank(message = "文章标题不能为空")
    @ApiModelProperty(value = "文章标题", required = true, example = "Spring Boot 开发实践")
    private String title;

    /**
     * 路径标识
     */
    @NotBlank(message = "路径标识")
    @ApiModelProperty(value = "路径标识", required = true, example = "spring-boot-development")
    private String slug;

    /**
     * 文章概要
     */
    @ApiModelProperty(value = "文章概要", example = "这是一篇关于 Spring Boot 开发实践的文章。")
    private String summary;

    /**
     * 文章内容（Markdown）
     */
    @NotBlank(message = "文章内容不能为空")
    @ApiModelProperty(value = "文章内容（Markdown）", required = true)
    private String contentMd;

    /**
     * 文章内容（HTML）
     * <p>
     * 通常由后端根据 Markdown 转换生成，前端不建议直接提交。
     */
    @ApiModelProperty(value = "文章内容（HTML）")
    private String contentHtml;

    /**
     * 分类ID
     */
    @NotNull(message = "文章分类不能为空")
    @ApiModelProperty(value = "分类ID", required = true, example = "10001")
    private Long categoryId;

    /**
     * 作者ID
     */
    @ApiModelProperty(value = "作者ID", example = "1")
    private Long authorId;

    /**
     * SEO标题
     */
    @ApiModelProperty(value = "SEO标题", example = "Spring Boot 开发实践")
    private String seoTitle;

    /**
     * SEO关键词
     */
    @ApiModelProperty(value = "SEO关键词", example = "Spring Boot,Java,Spring")
    private String seoKeywords;

    /**
     * SEO描述
     */
    @ApiModelProperty(value = "SEO描述", example = "Spring Boot 开发实践与经验总结。")
    private String seoDescription;

    /**
     * 文章类型
     * <p>
     * 1-原创；2-转载；3-翻译
     */
    @NotNull(message = "文章类型不能为空")
    @ApiModelProperty(value = "文章类型：1-原创；2-转载；3-翻译",
            required = true, example = "1")
    private Integer articleType;

    /**
     * 文章权限
     * <p>
     * 1-公开；2-私密；3-密码
     */
    @NotNull(message = "文章权限不能为空")
    @ApiModelProperty(value = "文章权限：1-公开；2-私密；3-密码",
            required = true, example = "1")
    private Integer articlePerm;

    /**
     * 访问密码
     */
    @ApiModelProperty(value = "访问密码", example = "123456")
    private String accessPassword;

    /**
     * 文章来源名称
     */
    @ApiModelProperty(value = "文章来源名称", example = "掘金")
    private String sourceName;

    /**
     * 原作者
     */
    @ApiModelProperty(value = "原作者", example = "SunTao")
    private String sourceAuthor;

    /**
     * 原文地址
     */
    @ApiModelProperty(value = "原文地址", example = "https://example.com/article")
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
     * 封面类型
     * <p>
     * 1-单封面；2-多封面（轮询）；3-默认；
     * 4-文章标题；5-无封面；6-多封面（随机）
     */
    @ApiModelProperty(
            value = "封面类型：1-单封面；2-多封面（轮询）；3-默认；4-文章标题；5-无封面；6-多封面（随机）",
            example = "1"
    )
    private Integer coverImageType;

    /**
     * 封面图片
     * <p>
     * 实际图片信息可存储于扩展字段或独立资源表。
     */
    @ApiModelProperty(value = "封面图片")
    private String coverImage;

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