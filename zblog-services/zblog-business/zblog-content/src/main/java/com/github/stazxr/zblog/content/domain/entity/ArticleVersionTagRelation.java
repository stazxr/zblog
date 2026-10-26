package com.github.stazxr.zblog.content.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文章版本标签关联
 *
 * @author SunTao
 * @since 2026-10-26
 */
@Getter
@Setter
@TableName("article_version_tag_relation")
public class ArticleVersionTagRelation implements Serializable {
    private static final long serialVersionUID = 8217095433710609546L;

    /**
     * 文章版本id
     */
    private Long articleVersionId;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 标签名称
     */
    private Long tagName;

    /**
     * 标签SLUG
     */
    private Long tagSlug;
}
