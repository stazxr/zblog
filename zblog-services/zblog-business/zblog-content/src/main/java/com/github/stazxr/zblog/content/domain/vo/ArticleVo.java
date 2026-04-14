package com.github.stazxr.zblog.content.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.stazxr.zblog.core.base.BaseVo;
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
@ApiModel("文章VO")
public class ArticleVo {
    private static final long serialVersionUID = -2823287174828491697L;

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
     * 文章状态
     *
     * @see com.github.stazxr.zblog.content.domain.enums.ArticleStatus
     */
    @ApiModelProperty("文章状态")
    private Integer articleStatus;

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
     * 是否置顶
     */
    @ApiModelProperty("")
    private Boolean topFlag;

    /**
     * 是否推荐
     */
    @ApiModelProperty("是否推荐")
    private Boolean recommendFlag;

    /**
     * 封面类型
     *
     * @see com.github.stazxr.zblog.content.domain.enums.ArticleCoverImgType
     */
    @ApiModelProperty("封面类型")
    private Integer coverImageType;

    /**
     * 总字数
     */
    @ApiModelProperty("总字数")
    private Integer wordsCount;

    /**
     * 总浏览数
     */
    @ApiModelProperty("总浏览数")
    private Integer viewCount;

    /**
     * 总点赞数
     */
    @ApiModelProperty("总点赞数")
    private Integer likeCount;

    /**
     * 总评论数
     */
    @ApiModelProperty("总评论数")
    private Integer commentCount;

    /**
     * 总收藏数
     */
    @ApiModelProperty("总收藏数")
    private Integer favoriteCount;

    /**
     * 文章创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("文章创建时间")
    private LocalDateTime createTime;

    /**
     * 文章发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("文章发布时间")
    private LocalDateTime publishTime;

    /**
     * 文章更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("文章更新时间")
    private LocalDateTime updateTime;

    /**
     * 文章下线时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("文章下线时间")
    private LocalDateTime deleteTime;

    /**
     * 迭代版本
     */
    @ApiModelProperty("迭代版本")
    private Integer version;

    /**
     * 扩展字段
     */
    @ApiModelProperty("扩展字段")
    private String extraInfo;
}
