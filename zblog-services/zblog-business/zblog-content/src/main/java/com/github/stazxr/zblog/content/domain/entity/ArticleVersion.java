package com.github.stazxr.zblog.content.domain.entity;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章发布版本
 *
 * @author SunTao
 * @since 2026-10-26
 */
@Getter
@Setter
@TableName("article_version")
public class ArticleVersion implements Serializable {
    private static final long serialVersionUID = -5231723418387374948L;

    /**
     * 文章版本id
     */
    @TableId
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 文章版本
     */
    private Integer version;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 路径标识
     */
    private String slug;

    /**
     * 文章概要
     */
    private String summary;

    /**
     * 文章内容（Markdown）
     */
    private String contentMd;

    /**
     * 文章内容（HTML）
     */
    private String contentHtml;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * SEO标题
     */
    private String seoTitle;

    /**
     * SEO关键词
     */
    private String seoKeywords;

    /**
     * SEO描述
     */
    private String seoDescription;

    /**
     * 文章类型
     *
     * @see com.github.stazxr.zblog.content.domain.enums.ArticleType
     */
    private Integer articleType;

    /**
     * 文章权限
     *
     * @see com.github.stazxr.zblog.content.domain.enums.ArticlePerm
     */
    private Integer articlePerm;

    /**
     * 文章来源名称
     */
    private String sourceName;

    /**
     * 原作者
     */
    private String sourceAuthor;

    /**
     * 原文地址
     */
    private String sourceUrl;

    /**
     * 是否允许评论
     */
    private Boolean commentFlag;

    /**
     * 封面类型
     *
     * @see com.github.stazxr.zblog.content.domain.enums.ArticleCoverImageType
     */
    private Integer coverImageType;

    /**
     * 总字数
     */
    private Integer wordsCount;

    /**
     * 发布人
     */
    private Long publishUserId;

    /**
     * 版本发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 扩展字段
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONObject extraInfo;
}
