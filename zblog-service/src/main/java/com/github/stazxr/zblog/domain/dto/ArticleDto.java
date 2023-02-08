package com.github.stazxr.zblog.domain.dto;

import com.github.stazxr.zblog.base.domain.entity.File;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 文章信息
 *
 * @author SunTao
 * @since 2022-12-03
 */
@Getter
@Setter
@ToString
public class ArticleDto {
    /**
     * 操作类型：add/edit
     */
    private String action;

    /**
     * 主键
     */
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
     * 文章内容（去除标签）
     */
    private String contentMd;

    /**
     * 文章关键字
     */
    private String keywords;

    /**
     * 原文链接（转载或翻译需要填写该字段）
     */
    private String reprintLink;

    /**
     * 封面类型: see {@link com.github.stazxr.zblog.domain.enums.ArticleImgType}
     */
    private Integer coverImageType;

    /**
     * 文章类型: see {@link com.github.stazxr.zblog.domain.enums.ArticleType}
     */
    private Integer articleType;

    /**
     * 文章权限: see {@link com.github.stazxr.zblog.domain.enums.ArticlePerm}
     */
    private Integer articlePerm;

    /**
     * 文章状态: see {@link com.github.stazxr.zblog.domain.enums.ArticleStatus}
     */
    private Integer articleStatus;

    /**
     * 文章分类
     */
    private Long categoryId;

    /**
     * 作者
     */
    private Long authorId;

    /**
     * 是否允许评论
     */
    private Boolean commentFlag;

    /**
     * 文章标签
     */
    private List<String> articleTag;

    /**
     * 文章封面
     */
    private List<File> articleImg;

    /**
     * 编辑器类型
     */
    private String extend1;

    /**
     * 文章字数
     */
    private String extend2;

    /**
     * 自动发布时间
     */
    private String autoPublishTime;
}
