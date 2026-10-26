package com.github.stazxr.zblog.content.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文章版本封面关联
 *
 * @author SunTao
 * @since 2026-10-26
 */
@Getter
@Setter
@TableName("article_version_image_relation")
public class ArticleVersionImageRelation implements Serializable {
    private static final long serialVersionUID = 2857870954545357174L;

    /**
     * 文章版本id
     */
    private Long articleVersionId;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 封面访问地址
     */
    private String articleImage;
}
