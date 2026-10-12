package com.github.stazxr.zblog.content.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 文章内容自动保存信息
 *
 * @author SunTao
 * @since 2026-10-12
 */
@ApiModel(value = "文章内容自动保存VO")
public class ArticleAutoSaveVo implements Serializable {
    private static final long serialVersionUID = 5630811318256498503L;

    /**
     * 草稿ID
     */
    @ApiModelProperty(value = "草稿ID")
    private Long id;

    /**
     * 文章ID
     */
    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String title;

    /**
     * 文章路径标识
     */
    @ApiModelProperty(value = "文章路径标识")
    private String slug;

    /**
     * 文章摘要
     */
    @ApiModelProperty(value = "文章摘要")
    private String summary;

    /**
     * 文章内容
     */
    @ApiModelProperty(value = "文章内容")
    private String content;

    /**
     * 文章字数
     */
    @ApiModelProperty(value = "文章字数")
    private Integer wordCount;

    /**
     * 保存时间
     */
    @ApiModelProperty(value = "保存时间")
    private String saveTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }
}
