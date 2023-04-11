package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章专栏关系表
 *
 * @author SunTao
 * @since 2022-05-31
 */
@Getter
@Setter
@TableName("article_column_relation")
public class ArticleColumnRelation extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 专栏序列
     */
    private Long columnId;

    /**
     * 文章序列
     */
    private Long articleId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 排序
     */
    private Integer sort;
}
