package com.github.stazxr.zblog.content.domain.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文章标签
 *
 * @author SunTao
 * @since 2026-10-10
 */
@Getter
@Setter
@TableName("article_tag_relation")
public class ArticleTagRelation implements Serializable {
    private static final long serialVersionUID = -2064419960199959545L;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 是否有效
     */
    @TableLogic
    private Boolean deleted;
}
