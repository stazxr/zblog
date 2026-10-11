package com.github.stazxr.zblog.content.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文章封面
 *
 * @author SunTao
 * @since 2026-10-11
 */
@Getter
@Setter
@TableName("article_image_relation")
public class ArticleImageRelation implements Serializable {
    private static final long serialVersionUID = -6026000270110491394L;

    /**
     * 文章序列
     */
    private Long articleId;

    /**
     * 文件序列
     */
    private String articleImage;
}
