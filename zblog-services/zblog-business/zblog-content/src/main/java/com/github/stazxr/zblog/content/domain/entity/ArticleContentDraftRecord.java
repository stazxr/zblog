package com.github.stazxr.zblog.content.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文章内容自动保存草稿表
 *
 * @author SunTao
 * @since 2026-10-12
 */
@Getter
@Setter
@TableName("article_content_draft_record")
public class ArticleContentDraftRecord implements Serializable {
    private static final long serialVersionUID = 2726936032679191432L;

    /**
     * 草稿ID
     */
    @TableId
    private Long id;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 路径标识
     */
    private String slug;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章字数
     */
    private Integer wordCount;

    /**
     * 保存时间
     */
    private String saveTime;
}
