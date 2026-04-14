package com.github.stazxr.zblog.content.domain.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章
 *
 * @author SunTao
 * @since 2021-01-18
 */
@Getter
@Setter
@TableName("article")
public class Article implements Serializable {
    private static final long serialVersionUID = -7982622581367637277L;

    /**
     * 文章ID
     */
    @TableId
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * URL标识
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
     * 分类ID
     */
    private Long categoryId;

    /**
     * 作者ID
     */
    private Long authorId;

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
     * 文章状态
     *
     * @see com.github.stazxr.zblog.content.domain.enums.ArticleStatus
     */
    private Integer articleStatus;

    /**
     * 文章权限
     *
     * @see com.github.stazxr.zblog.content.domain.enums.ArticlePerm
     */
    private Integer articlePerm;

    /**
     * 访问密码
     */
    private String password;

    /**
     * 原文地址
     */
    private String reprintLink;

    /**
     * 转载说明
     */
    private String reprintDesc;

    /**
     * 是否允许评论
     */
    private Boolean commentFlag;

    /**
     * 是否置顶
     */
    private Boolean topFlag;

    /**
     * 是否推荐
     */
    private Boolean recommendFlag;

    /**
     * 封面类型
     *
     * @see com.github.stazxr.zblog.content.domain.enums.ArticleCoverImgType
     */
    private Integer coverImageType;

    /**
     * 总字数
     */
    private Integer wordsCount;

    /**
     * 总浏览数
     */
    private Integer viewCount;

    /**
     * 总点赞数
     */
    private Integer likeCount;

    /**
     * 总评论数
     */
    private Integer commentCount;

    /**
     * 总收藏数
     */
    private Integer favoriteCount;

    /**
     * 文章创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 文章发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;

    /**
     * 文章更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 文章下线时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

    /**
     * 迭代版本
     */
    private Integer version;

    /**
     * 扩展字段
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONObject extraInfo;
}
