package com.github.stazxr.zblog.content.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章信息
 *
 * @author SunTao
 * @since 2026-09-15
 */
@Getter
@Setter
@ApiModel(value = "ArticleVO", description = "文章信息")
public class ArticleVo implements Serializable {
    private static final long serialVersionUID = 2088450969322672429L;

    /**
     * 文章ID
     */
    @ApiModelProperty(value = "文章ID", example = "10001")
    private Long id;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题", example = "Spring Boot 开发实践")
    private String title;

    /**
     * 路径标识
     */
    @ApiModelProperty(value = "路径标识", example = "spring-boot-development")
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
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID", example = "10001")
    private Long categoryId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称", example = "Java")
    private String categoryName;

    /**
     * 作者ID
     */
    @ApiModelProperty(value = "作者ID", example = "1")
    private Long authorId;

    /**
     * 作者名称
     */
    @ApiModelProperty(value = "作者名称", example = "SunTao")
    private String authorName;

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
     * <p>
     * 1-原创；2-转载；3-翻译
     */
    @ApiModelProperty(value = "文章类型：1-原创；2-转载；3-翻译", example = "1")
    private Integer articleType;

    /**
     * 文章类型名称
     */
    @ApiModelProperty(value = "文章类型名称", example = "原创")
    private String articleTypeName;

    /**
     * 文章状态
     * <p>
     * 1-草稿；2-待审核；3-待审核（定时发布）；4-待发布；
     * 5-审核不通过；6-已发布；7-临时下线；8-待整改
     */
    @ApiModelProperty(value = "文章状态", example = "6")
    private Integer articleStatus;

    /**
     * 文章状态名称
     */
    @ApiModelProperty(value = "文章状态名称", example = "已发布")
    private String articleStatusName;

    /**
     * 删除标记
     * <p>
     * 0-正常；1-回收站；2-彻底删除
     */
    @ApiModelProperty(value = "删除标记：0-正常；1-回收站；2-彻底删除", example = "0")
    private Integer deleteFlag;

    /**
     * 文章权限
     * <p>
     * 1-公开；2-私密；3-密码
     */
    @ApiModelProperty(value = "文章权限：1-公开；2-私密；3-密码", example = "1")
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
     * 封面类型
     */
    @ApiModelProperty(value = "封面类型", example = "1")
    private Integer coverImageType;

    /**
     * 封面图片
     */
    @ApiModelProperty(value = "封面图片")
    private String coverImage;

    /**
     * 总字数
     */
    @ApiModelProperty(value = "总字数", example = "3500")
    private Integer wordsCount;

    /**
     * 总浏览数
     */
    @ApiModelProperty(value = "总浏览数", example = "1000")
    private Long viewCount;

    /**
     * 总点赞数
     */
    @ApiModelProperty(value = "总点赞数", example = "100")
    private Long likeCount;

    /**
     * 总评论数
     */
    @ApiModelProperty(value = "总评论数", example = "20")
    private Long commentCount;

    /**
     * 总收藏数
     */
    @ApiModelProperty(value = "总收藏数", example = "30")
    private Long favoriteCount;

    /**
     * 文章发布时间
     */
    @ApiModelProperty(value = "文章发布时间", example = "2026-09-15 10:00:00")
    private LocalDateTime publishTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人ID", example = "1")
    private Long creatorId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", example = "2026-09-15 10:00:00")
    private LocalDateTime createTime;

    /**
     * 最后修改人
     */
    @ApiModelProperty(value = "最后修改人ID", example = "1")
    private Long updaterId;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", example = "2026-09-15 10:00:00")
    private LocalDateTime updateTime;

    /**
     * 文章下线时间
     */
    @ApiModelProperty(value = "文章下线时间", example = "2026-09-15 10:00:00")
    private LocalDateTime deleteTime;

    /**
     * 迭代版本
     */
    @ApiModelProperty(value = "迭代版本", example = "1")
    private Integer version;

    /**
     * 扩展字段
     */
    @ApiModelProperty(value = "扩展字段")
    private String extraInfo;
}
