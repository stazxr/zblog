package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import com.github.stazxr.zblog.domain.enums.ArticlePerm;
import com.github.stazxr.zblog.domain.enums.ArticleStatus;
import com.github.stazxr.zblog.domain.enums.ArticleType;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章
 *
 * @author SunTao
 * @since 2021-01-18
 */
@Getter
@Setter
@TableName("article")
public class Article extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 概要
     */
    private String remark;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章内容（预留字段）
     */
    private String contentMd;

    /**
     * 文章关键字，SEO优化
     */
    private String keywords;

    /**
     * 文章略缩图
     */
    private String coverImage;

    /**
     * 文章分类
     */
    private Long categoryId;

    /**
     * 作者
     */
    private Long authorId;

    /**
     * 文章类型
     */
    private ArticleType articleType;

    /**
     * 文章状态
     */
    private ArticleStatus articleStatus;

    /**
     * 文章权限
     */
    private ArticlePerm articlePerm;

    /**
     * 是否允许评论
     */
    private Boolean commentAble;

    /**
     * 转载情况下原文的地址
     */
    private String reprintLink;

    /**
     * 转载说明
     */
    private String reprintDesc;

    /**
     * 是否已删除（使用逻辑操作，保护数据）
     */
    @TableLogic
    private Boolean deleted;
}
