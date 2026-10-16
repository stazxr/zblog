package com.github.stazxr.zblog.content.domain.vo;

import com.github.stazxr.zblog.base.domain.dto.UploadFileDto;
import com.github.stazxr.zblog.base.domain.vo.UploadFileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
    @ApiModelProperty(value = "分类ID")
    private Long categoryId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    /**
     * 标签id列表
     */
    @ApiModelProperty(value = "标签id列表")
    private List<Long> tags;

    /**
     * 作者ID
     */
    @ApiModelProperty(value = "作者ID")
    private Long authorId;

    /**
     * 作者名称
     */
    @ApiModelProperty(value = "作者名称")
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
     */
    @ApiModelProperty(value = "文章类型")
    private Integer articleType;

    /**
     * 文章状态
     */
    @ApiModelProperty(value = "文章状态")
    private Integer articleStatus;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记：0-正常；1-回收站；2-彻底删除")
    private Integer deleteFlag;

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
    private List<UploadFileVo> coverImages;

    /**
     * 总字数
     */
    @ApiModelProperty(value = "总字数")
    private Integer wordsCount;

    /**
     * 总浏览数
     */
    @ApiModelProperty(value = "总浏览数")
    private Long viewCount;

    /**
     * 总点赞数
     */
    @ApiModelProperty(value = "总点赞数")
    private Long likeCount;

    /**
     * 总评论数
     */
    @ApiModelProperty(value = "总评论数")
    private Long commentCount;

    /**
     * 总收藏数
     */
    @ApiModelProperty(value = "总收藏数")
    private Long favoriteCount;

    /**
     * 文章发布时间
     */
    @ApiModelProperty(value = "文章发布时间")
    private LocalDateTime publishTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人ID")
    private Long creatorId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 最后修改人
     */
    @ApiModelProperty(value = "最后修改人ID")
    private Long updaterId;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 文章下线时间
     */
    @ApiModelProperty(value = "文章下线时间")
    private LocalDateTime deleteTime;

    /**
     * 迭代版本
     */
    @ApiModelProperty(value = "迭代版本")
    private Integer version;

    /**
     * 扩展字段
     */
    @ApiModelProperty(value = "扩展字段")
    private String extraInfo;
}
